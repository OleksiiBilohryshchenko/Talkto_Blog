import http from 'k6/http';
import { check } from 'k6';
import { extractCsrfToken } from './csrf.js';

const BASE_URL = 'http://localhost:8080';

// Perform full login flow (GET login page → extract CSRF → POST credentials)
// Returns cookies for authenticated session
export function login(email, password) {
  // Step 1: Load login page to retrieve CSRF token
  const loginPage = http.get(`${BASE_URL}/login`);

  check(loginPage, {
    'login page loaded': (r) => r.status === 200,
  });

  const csrfToken = extractCsrfToken(loginPage.body);

  // Step 2: Submit login form
  const loginRes = http.post(
      `${BASE_URL}/login`,
      {
        username: email,
        password: password,
        _csrf: csrfToken,
      },
      {
        redirects: 0, // We want to manually verify redirect behavior
      }
  );

  check(loginRes, {
    'login response is redirect': (r) => r.status === 302 || r.status === 200,
  });

  return loginRes.cookies;
}

// Perform logout (requires CSRF)
export function logout(cookies) {
  // Load any page to get fresh CSRF token inside authenticated session
  const page = http.get(`${BASE_URL}/posts`, { cookies });

  const csrfToken = extractCsrfToken(page.body);

  const logoutRes = http.post(
      `${BASE_URL}/logout`,
      { _csrf: csrfToken },
      { cookies }
  );

  check(logoutRes, {
    'logout successful': (r) => r.status === 302 || r.status === 200,
  });
}