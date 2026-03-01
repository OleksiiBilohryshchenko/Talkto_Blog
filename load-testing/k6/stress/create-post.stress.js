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

export default function () {
  const jar = http.cookieJar();

  const loginPage = http.get(`${BASE_URL}/login`, { jar });
  const csrf = extractCsrf(loginPage.body);

  const loginRes = http.post(
      `${BASE_URL}/login`,
      { username: USER_EMAIL, password: USER_PASSWORD, _csrf: csrf },
      { redirects: 0, jar }
  );

  if (loginRes.status !== 302 && loginRes.status !== 200) {
    failureRate.add(1);
    return;
  }

  const newPostPage = http.get(`${BASE_URL}/posts/new`, { jar });
  const formCsrf = extractCsrf(newPostPage.body);

  const createRes = http.post(
      `${BASE_URL}/posts`,
      {
        title: `Stress Title ${__ITER}`,
        content: `Stress Content ${__ITER}`,
        _csrf: formCsrf,
      },
      { redirects: 0, jar }
  );

  check(createRes, { 'post created 302': r => r.status === 302 }) || failureRate.add(1);

  sleep(1);
}