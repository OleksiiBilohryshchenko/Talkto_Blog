import http from 'k6/http';
import { check, sleep } from 'k6';
import { options } from './config.js';
import { login, logout } from './helpers/auth.js';
import { extractCsrfToken } from './helpers/csrf.js';

export { options };

const BASE_URL = 'http://localhost:8080';
const EMAIL = 'ci_test@mail.com';
const PASSWORD = 'test1234';

// Simple random post selector based on existing DB range
function getRandomPostId() {
  return Math.floor(Math.random() * 69) + 1;
}

/*
 * Read-heavy scenario
 * Flow: login → posts → post/{id} → logout
 */
export function readScenario() {
  const cookies = login(EMAIL, PASSWORD);

  const postsRes = http.get(`${BASE_URL}/posts`, { cookies });

  check(postsRes, {
    'posts page loaded': (r) => r.status === 200,
  });

  const postId = getRandomPostId();

  const postRes = http.get(`${BASE_URL}/posts/${postId}`, { cookies });

  check(postRes, {
    'post page loaded': (r) => r.status === 200,
  });

  logout(cookies);

  sleep(1);
}

/*
 * Write-heavy scenario
 * Flow: login → post/{id} → add comment → logout
 */
export function writeScenario() {
  const cookies = login(EMAIL, PASSWORD);

  const postId = getRandomPostId();

  const postRes = http.get(`${BASE_URL}/posts/${postId}`, { cookies });

  check(postRes, {
    'post page loaded': (r) => r.status === 200,
  });

  const csrfToken = extractCsrfToken(postRes.body);

  const commentRes = http.post(
      `${BASE_URL}/posts/${postId}/comments`,
      {
        content: `Load test comment ${Date.now()}`,
        _csrf: csrfToken,
      },
      { cookies, redirects: 0 }
  );

  check(commentRes, {
    'comment submission redirect': (r) => r.status === 302,
  });

  logout(cookies);

  sleep(1);
}