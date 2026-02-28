// k6 load configuration for TalkTo Blog
// Mixed traffic profile: 70% read, 30% write
// Total load: 10 RPS

export const options = {
  scenarios: {
    read_traffic: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 20,
      maxVUs: 50,
      stages: [
        { target: 7, duration: '30s' },   // 70% of 10 RPS
        { target: 7, duration: '5m' },
        { target: 0, duration: '30s' },
      ],
      exec: 'readScenario',
    },

    write_traffic: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 20,
      maxVUs: 50,
      stages: [
        { target: 3, duration: '30s' },   // 30% of 10 RPS
        { target: 3, duration: '5m' },
        { target: 0, duration: '30s' },
      ],
      exec: 'writeScenario',
    },
  },

  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<1500'],
  },
};