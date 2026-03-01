import http from 'k6/http';
import { check, sleep } from 'k6';
import { options, failureRate, BASE_URL } from '../stress.config.js';

export { options };

const USER_EMAIL = 'ci_test@mail.com';
const USER_PASSWORD = 'test1234';

function extractCsrf(body) {
  const match = body.match(/name="_csrf"\s+value="(.+?)"/);
  return match ? match[1] : null;
}

function extractPostIds(body) {
  const regex = /\/posts\/(\d+)/g;
  const ids = [];
  let match;
  while ((match = regex.exec(body)) !== null) ids.push(match[1]);
  return [...new Set(ids)];
}

export function setup() {
  const jar = http.cookieJar();

  const loginPage = http.get(`${BASE_URL}/login`, { jar });
  const csrf = extractCsrf(loginPage.body);

  const loginRes = http.post(
      `${BASE_URL}/login`,
      { username: USER_EMAIL, password: USER_PASSWORD, _csrf: csrf },
      { redirects: 0, jar }
  );

  if (loginRes.status !== 302 && loginRes.status !== 200) {
    throw new Error('Login failed');
  }

  const listRes = http.get(`${BASE_URL}/posts`, { jar });
  const postIds = extractPostIds(listRes.body);

  return { cookies: jar.cookiesForURL(BASE_URL), postIds };
}

export default function (data) {
  const jar = http.cookieJar();

  Object.entries(data.cookies).forEach(([name, values]) => {
    values.forEach(c => jar.set(BASE_URL, name, c.value));
  });

  const randomId = data.postIds[Math.floor(Math.random() * data.postIds.length)];

  const listRes = http.get(`${BASE_URL}/posts`, { jar });
  check(listRes, { 'posts list 200': r => r.status === 200 }) || failureRate.add(1);

  const viewRes = http.get(`${BASE_URL}/posts/${randomId}`, { jar });
  check(viewRes, { 'post view 200': r => r.status === 200 }) || failureRate.add(1);

  sleep(1);
}