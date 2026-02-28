# Load Testing Report — TalkTo Blog

## 1. System Under Test

**Application:** TalkTo Blog  
**Stack:** Spring Boot 3.5, Thymeleaf, PostgreSQL  
**Authentication:** Form login + session cookie (JSESSIONID)  
**CSRF Protection:** Enabled (`_csrf` hidden input)  
**Base URL:** http://localhost:8080

**Test user:**  
ci_test@mail.com / test1234

**Dataset:**
- Users: 71
- Posts: 69
- Comments: 29

Seed data was not modified during testing.

---

## 2. Testing Objectives

The objectives of this laboratory work were:

1. Measure backend performance under controlled load (10 RPS).
2. Validate authentication and CSRF handling under concurrent load.
3. Evaluate read-heavy and write-heavy scenarios.
4. Measure browser lifecycle metrics separately.
5. Provide a realistic production-style performance evaluation.

---

## 3. Tools Used

### 3.1 k6 (HTTP-Level Load Testing)

k6 was used to simulate authenticated user traffic at the HTTP layer.

k6 measures:
- End-to-end request duration
- Backend waiting time (TTFB)
- Error rate
- Throughput
- Percentile latency (p90, p95, p99)

k6 does NOT measure:
- DOM rendering
- Layout calculation
- JavaScript execution
- Paint or visual readiness

k6 is therefore suitable for backend SLA validation.

---

### 3.2 Playwright (Browser-Level Performance)

Playwright was used to measure browser lifecycle metrics.

Measured metrics:
- DNS resolution
- TCP connection
- TLS handshake
- TTFB
- DOMContentLoaded
- Load event
- First Contentful Paint (if available)

Playwright measures user-perceived performance and completes the lifecycle visibility missing in HTTP-only tools.

---

## 4. Test Scenarios (k6)

### Scenario A — Read-Heavy (70%)

Flow:
1. GET `/login`
2. Extract `_csrf`
3. POST `/login`
4. GET `/posts`
5. GET `/posts/{id}`
6. POST `/logout`

Purpose: simulate authenticated content browsing.

---

### Scenario B — Write-Heavy (30%)

Flow:
1. GET `/login`
2. Extract `_csrf`
3. POST `/login`
4. GET `/posts/{id}`
5. Extract `_csrf`
6. POST `/posts/{id}/comments`
7. POST `/logout`

Purpose: simulate comment creation under load.

---

## 5. Load Profile

**Load type:** Constant arrival rate  
**Target:** 10 RPS

**Duration:**
- 30 seconds ramp-up
- 5 minutes steady load
- 30 seconds ramp-down

**Traffic distribution:**
- 70% Scenario A
- 30% Scenario B

---

## 6. k6 Results (Main Run)

Results file:
load-testing/k6/results/k6-main-run.json

### 6.1 Overall Statistics

- Total HTTP requests: 23,303
- Total iterations: 3,329
- Max VUs: 40

---

### 6.2 Reliability

| Metric | Value |
|--------|-------|
| Failed requests | 0 |
| http_req_failed | 0.00% |
| Check pass rate | 100% |

No HTTP 403, 500, or timeouts were detected.

---

### 6.3 Latency — http_req_duration

| Metric | Value |
|--------|-------|
| avg | 14.52 ms |
| median | 4.35 ms |
| p90 | 79.05 ms |
| p95 | 82.42 ms |
| max | 110.45 ms |

Defined threshold: p(95) < 1500 ms  
Actual p95: 82.42 ms → threshold satisfied.

---

### 6.4 Backend Waiting Time (TTFB)

Metric: http_req_waiting

| Metric | Value |
|--------|-------|
| avg | 13.86 ms |
| median | 2.84 ms |
| p95 | 82.40 ms |

Since the test was executed locally, most latency reflects backend processing time.

---

## 7. Playwright Browser Performance Results

Measured pages:
- `/posts`
- `/posts/{id}`

Collected metrics:
- DNS
- TCP
- TLS
- TTFB
- DOMContentLoaded
- Load event
- First Contentful Paint

Results show:
- Fast backend response.
- DOM rendering completes without delay.
- No visible UX degradation.

---

## 8. Failure Analysis

Observed failures: 0

- No 403 (CSRF failures)
- No 500 (server errors)
- No timeouts
- No authentication issues

All checks passed successfully.

This confirms:
- Correct CSRF extraction
- Stable session handling
- No concurrency-related failures
- Stable write operations under load

---

## 9. Measurement Validity & Lifecycle Mapping

### What k6 Covers

- DNS (partially)
- TCP connection
- Request sending
- Backend processing
- Response receiving

This validates backend SLA and network performance.

### What k6 Does Not Cover

- DOM parsing
- Rendering
- Paint
- JavaScript execution
- User-perceived readiness

### What Playwright Adds

Playwright measures:
- DOMContentLoaded
- Load event
- Visual readiness (FCP)

Lifecycle mapping:

| Lifecycle Phase | Tool |
|-----------------|------|
| DNS / Connect | k6 + Playwright |
| Backend processing | k6 |
| HTML received | k6 |
| DOM ready | Playwright |
| Render complete | Playwright |

This combined approach reflects real-world production testing methodology.

---

## 10. Conclusions

Under normal load (10 RPS):

- The system remains stable.
- No request failures occurred.
- p95 latency remains below 100 ms.
- Backend processing time is minimal.
- Comment submission remains stable under concurrent load.
- Browser lifecycle metrics show no UX degradation.

The application satisfies performance requirements under the defined normal load profile.