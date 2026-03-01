import http from 'k6/http';
import { check, sleep } from 'k6';
import { options, failureRate, BASE_URL } from '../stress.config.js';

export { options };

const USER_EMAIL = 'ci_test@mail.com';
const USER_PASSWORD = 'test1234';

export default function () {
  const jar = http.cookieJar();

  // Load login page to extract CSRF token
  const loginPage = http.get(`${BASE_URL}/login`);

  const loginPageOk = check(loginPage, {
    'login page status 200': (r) => r.status === 200,
  });

  if (!loginPageOk) {
    failureRate.add(1);
    return;
  }

  const csrfMatch = loginPage.body.match(/name="_csrf"\s+value="(.+?)"/);
  if (!csrfMatch) {
    failureRate.add(1);
    return;
  }

  const csrfToken = csrfMatch[1];

  // Submit login form
  const loginRes = http.post(
      `${BASE_URL}/login`,
      {
        username: USER_EMAIL,
        password: USER_PASSWORD,
        _csrf: csrfToken,
      },
      { redirects: 0 }
  );

  const loginOk = check(loginRes, {
    'login redirect success': (r) => r.status === 302 || r.status === 200,
  });

  if (!loginOk) {
    failureRate.add(1);
    return;
  }

  // Extract CSRF for logout
  const logoutPage = http.get(`${BASE_URL}/login`);

  const logoutCsrfMatch = logoutPage.body.match(/name="_csrf"\s+value="(.+?)"/);
  if (!logoutCsrfMatch) {
    failureRate.add(1);
    return;
  }

  const logoutCsrf = logoutCsrfMatch[1];

  const logoutRes = http.post(
      `${BASE_URL}/logout`,
      { _csrf: logoutCsrf },
      { redirects: 0 }
  );

  const logoutOk = check(logoutRes, {
    'logout redirect success': (r) => r.status === 302 || r.status === 200,
  });

  if (!logoutOk) {
    failureRate.add(1);
  }

  sleep(1);
}