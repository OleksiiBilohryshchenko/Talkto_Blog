# Load Testing Report — TalkTo Blog

## 1. System Under Test

**Application:** TalkTo Blog  
**Stack:** Spring Boot 3.5, Thymeleaf, PostgreSQL  
**Authentication:** Form login + session cookie (JSESSIONID)  
**CSRF Protection:** Enabled (`_csrf` hidden input)  
**Base URL:** http://localhost:8080

**Test user:**  
ci_test@mail.com / test1234

**Initial dataset before test execution:**
- Users: 71
- Posts: 69
- Comments: 29

Additional posts and comments were created during load testing as part of write scenarios.

---

## 2. Testing Objectives

The objectives of this laboratory work were:

1. Measure backend performance under controlled load (10 RPS).
2. Validate authentication and CSRF handling under concurrent load.
3. Evaluate read and write scenarios under normal operating conditions.
4. Measure browser lifecycle metrics separately.
5. Provide a structured backend SLA evaluation aligned with real-world testing methodology.

---

## 3. Tools Used

### 3.1 k6 (HTTP-Level Load Testing)

k6 was used to simulate authenticated traffic at the HTTP layer.

k6 measures:
- End-to-end request duration
- Backend waiting time (TTFB)
- Error rate
- Throughput
- Percentile latency (p90, p95, p99)

k6 does NOT measure:
- DOM parsing
- Rendering
- JavaScript execution
- Paint timing
- Interactive readiness

k6 validates backend SLA and network performance.

---

### 3.2 Playwright (Browser-Level Performance)

Playwright was used to measure browser lifecycle metrics.

Measured metrics:
- DNS resolution
- TCP connection
- TLS handshake
- Time to First Byte (TTFB)
- DOMContentLoaded
- Load event
- First Contentful Paint (when available)

Playwright complements k6 by measuring user-perceived performance.

---

## 4. Test Scenarios (k6)

Four authenticated scenarios were implemented:

### Scenario 1 — Read Post

1. GET `/login`
2. Extract `_csrf`
3. POST `/login`
4. GET `/posts/{id}`
5. POST `/logout`

---

### Scenario 2 — Read + Comment

1. GET `/login`
2. Extract `_csrf`
3. POST `/login`
4. GET `/posts/{id}`
5. Extract `_csrf`
6. POST `/posts/{id}/comments`
7. POST `/logout`

---

### Scenario 3 — Create Post

1. GET `/login`
2. Extract `_csrf`
3. POST `/login`
4. GET `/posts/new`
5. Extract `_csrf`
6. POST `/posts`
7. POST `/logout`

---

### Scenario 4 — Update Profile

1. GET `/login`
2. Extract `_csrf`
3. POST `/login`
4. GET `/profile/edit`
5. Extract `_csrf`
6. POST `/profile/edit`
7. POST `/logout`

---

## 5. Load Profile

**Load type:** Constant arrival rate  
**Target load:** 10 RPS

**Execution stages:**
- 30 seconds ramp-up
- 5 minutes steady load
- 30 seconds ramp-down

Scenario distribution:

- Read-only: 3 RPS
- Read + Comment: 3 RPS
- Create Post: 2 RPS
- Update Profile: 2 RPS

Total target: 10 RPS

---

## 6. k6 Results (Main Run)

Results file:  
`load-testing/k6/results/k6-main-run.json`

### 6.1 Overall Statistics

- Total HTTP requests: 15,786
- Total iterations: 3,358
- Configured max VUs: 80
- Observed max concurrent VUs: 4
- Effective iteration rate: ~9.32 iterations/sec

Although the target was 10 RPS, the sustained effective rate was approximately 9.3 iterations per second, which is within expected scheduling variance.

---

### 6.2 Reliability

| Metric | Value |
|--------|-------|
| Failed requests | 0 |
| http_req_failed | 0.00% |
| Check pass rate | 100% |

No HTTP 403 (CSRF errors), 500 (server errors), or timeouts occurred.

Authentication, session handling, and write operations remained stable under load.

---

### 6.3 Latency — http_req_duration

| Metric | Value |
|--------|-------|
| avg | 23.11 ms |
| median | 2.75 ms |
| p90 | 104.98 ms |
| p95 | 106.61 ms |
| p99 | 107.58 ms |
| max | 111.29 ms |

Defined thresholds:

- p(95) < 1500 ms
- p(99) < 2000 ms

Actual results:

- p95 = 106.61 ms → threshold satisfied
- p99 = 107.58 ms → threshold satisfied

The system remains significantly below defined SLA limits.

---

### 6.4 Backend Waiting Time (TTFB)

Metric: `http_req_waiting`

| Metric | Value |
|--------|-------|
| avg | 22.59 ms |
| median | 2.72 ms |
| p95 | 106.55 ms |

Since the test was executed locally, most latency reflects backend processing time rather than network delay.

No latency spikes or long-tail degradation were observed.

---

## 7. Playwright Browser Performance Results

Measured pages:
- `/posts`
- `/posts/{id}`

Collected lifecycle metrics:
- DNS
- TCP
- TLS
- TTFB
- DOMContentLoaded
- Load event
- First Contentful Paint

Results indicate:

- Fast backend response.
- Stable DOM construction.
- No visible UX degradation.
- No blocking behavior during rendering.

---

## 8. Failure Analysis

Observed failures: 0

- No CSRF violations
- No authentication failures
- No 500 errors
- No timeouts
- No concurrency-related write conflicts

All checks passed successfully.

---

## 9. Measurement Validity & Lifecycle Mapping

### What k6 Covers

- TCP connection establishment
- Request transmission
- Backend processing time
- Response transfer

This validates backend SLA and network-layer performance.

### What k6 Does Not Cover

- DOM parsing
- JavaScript execution
- Layout & rendering
- Interactive readiness

k6 does not execute browser JavaScript and therefore cannot measure client-side rendering performance.

### What Playwright Adds

Playwright measures:

- DOMContentLoaded
- Load event
- First Contentful Paint
- Full browser lifecycle

Lifecycle mapping:

| Lifecycle Phase | Tool |
|-----------------|------|
| DNS / TCP / TLS | k6 + Playwright |
| Backend processing | k6 |
| HTML transfer | k6 |
| DOM ready | Playwright |
| Render complete | Playwright |

This combined methodology provides both backend SLA validation and user-perceived performance visibility.

---

## 10. Conclusions

Under normal load (10 RPS):

- The system remains stable.
- No request failures occurred.
- p95 latency remains around 106 ms.
- Backend processing time is minimal.
- Write operations remain stable under concurrent load.
- No performance degradation was observed.
- Browser lifecycle metrics show no UX impact.

The application satisfies defined performance requirements under the specified normal load profile.