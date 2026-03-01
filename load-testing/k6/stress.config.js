import { Rate } from 'k6/metrics';

export const BASE_URL = 'http://app:8080';
export const failureRate = new Rate('failures');

export const options = {
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],

  scenarios: {
    stress_test: {
      executor: 'ramping-arrival-rate',
      startRate: 50,
      timeUnit: '1s',
      stages: [
        { target: 50, duration: '30s' },   // warm-up
        { target: 200, duration: '30s' },  // load
        { target: 500, duration: '60s' },  // stress
      ],
      preAllocatedVUs: 200,
      maxVUs: 1000,
    },
  },

  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<1500'],
    failures: ['rate<0.01'],
  },
};