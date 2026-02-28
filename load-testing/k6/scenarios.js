import http from 'k6/http';
import { check } from 'k6';
import { options } from './config.js';
import { login } from './helpers/auth.js';
import { extractCsrfToken } from './helpers/csrf.js';

export { options };

const BASE_URL = 'http://localhost:8080';
const EMAIL = 'ci_test@mail.com';
const PASSWORD = 'test1234';

function getRandomPostId() {
  return Math.floor(Math.random() * 69) + 1;
}

function logout(cookies, csrfToken) {
  return http.post(
      `${BASE_URL}/logout`,
      { _csrf: csrfToken },
      { cookies, redirects: 0 }
  );
}

/* Scenario 1 */
export function readOnlyScenario() {
  const cookies = login(EMAIL, PASSWORD);

  const postId = getRandomPostId();
  const postRes = http.get(`${BASE_URL}/posts/${postId}`, { cookies });

  check(postRes, {
    'read post (200)': (r) => r.status === 200,
  });

  const csrfToken = extractCsrfToken(postRes.body);
  logout(cookies, csrfToken);
}

/* Scenario 2 */
export function readAndCommentScenario() {
  const cookies = login(EMAIL, PASSWORD);

  const postId = getRandomPostId();
  const postRes = http.get(`${BASE_URL}/posts/${postId}`, { cookies });

  check(postRes, {
    'open post (200)': (r) => r.status === 200,
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
    'comment created (302)': (r) => r.status === 302,
  });

  logout(cookies, csrfToken);
}

/* Scenario 3 */
export function createPostScenario() {
  const cookies = login(EMAIL, PASSWORD);

  const newPostPage = http.get(`${BASE_URL}/posts/new`, { cookies });

  check(newPostPage, {
    'new post form (200)': (r) => r.status === 200,
  });

  const csrfToken = extractCsrfToken(newPostPage.body);

  const createRes = http.post(
      `${BASE_URL}/posts`,
      {
        title: `test_post_${Date.now()}`,
        content: 'automated content',
        _csrf: csrfToken,
      },
      { cookies, redirects: 0 }
  );

  check(createRes, {
    'post created (302)': (r) => r.status === 302,
  });

  logout(cookies, csrfToken);
}

/* Scenario 4 */
export function updateProfileScenario() {
  const cookies = login(EMAIL, PASSWORD);

  const profileEditPage = http.get(`${BASE_URL}/profile/edit`, { cookies });

  check(profileEditPage, {
    'profile edit form (200)': (r) => r.status === 200,
  });

  const csrfToken = extractCsrfToken(profileEditPage.body);

  const updateRes = http.post(
      `${BASE_URL}/profile/edit`,
      {
        name: `updated_${Date.now()}`,
        _csrf: csrfToken,
      },
      { cookies, redirects: 0 }
  );

  check(updateRes, {
    'profile updated (302)': (r) => r.status === 302,
  });

  logout(cookies, csrfToken);
}