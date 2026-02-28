# Load Testing — How to Run

## 1. Prerequisites

Ensure the following components are installed and running:

- Java 21+
- PostgreSQL
- Spring Boot application running at: `http://localhost:8080`
- k6 installed and available in PATH
- Node.js 20+ (for Playwright tests)

Verify that the application is accessible:

- `http://localhost:8080/login`
- `http://localhost:8080/posts`

Ensure the test user exists:

```
ci_test@mail.com / test1234
```

---

## 2. Environment Preparation

Before executing load tests:

- Close heavy background applications.
- Do not run Postman, Newman, or other API testing tools concurrently.
- Ensure only one load test is running at a time.
- Manually verify that login and comment submission work in the browser.
- Confirm that Flyway migrations have been applied successfully.

---

## 3. Run k6 Main Load Test (Official Run)

This is the full 10 RPS load profile:

- 30 seconds ramp-up
- 5 minutes steady load
- 30 seconds ramp-down

Execute:

```bash
k6 run load-testing/k6/scenarios.js \
  --summary-export=load-testing/k6/results/k6-main-run.json \
  > load-testing/k6/results/k6-main-run.txt
```

Results will be stored in:

```
load-testing/k6/results/
  k6-main-run.json
  k6-main-run.txt
```

- `.json` contains structured metrics used in the report.
- `.txt` contains console output including threshold validation.

---

## 4. Run k6 Sanity Test (Optional)

For quick validation before the full test:

```bash
k6 run load-testing/k6/scenarios.js --duration 60s
```

This verifies:

- Authentication flow
- CSRF extraction
- Scenario execution stability

---

## 5. Load Profile Details

**Load model:**

- Constant arrival rate
- Target: 10 RPS

**Scenario distribution:**

| Scenario         | RPS |
|------------------|-----|
| Read-only        | 3   |
| Read + Comment   | 3   |
| Create Post      | 2   |
| Update Profile   | 2   |
| **Total**        | **10** |

Each scenario includes:

- Login
- CSRF extraction
- Authenticated action
- Logout

**Thresholds:**

- `http_req_failed` < 1%
- p95 latency < 1500 ms
- p99 latency < 2000 ms

---

## 6. Run Playwright Browser Performance Test

Navigate to the Playwright directory:

```bash
cd load-testing/playwright
```

Install dependencies (first run only):

```bash
npm install
```

Execute browser performance tests:

```bash
npx playwright test
```

Results will be stored in:

```
load-testing/playwright/results/
```

These results include:

- DNS timing
- TCP connection time
- TLS handshake
- TTFB
- DOMContentLoaded
- Load event
- First Contentful Paint (if supported)

---

## 7. Important Notes

- k6 measures backend and network performance only.
- k6 does not execute JavaScript or measure rendering.
- Playwright is used to measure user-perceived browser performance.
- The test environment is local; therefore, network latency is minimal.
- Write scenarios create additional posts and comments in the database.

This setup reflects a structured laboratory-level performance validation under defined normal load conditions (10 RPS).