export const options = {
  // Include extended percentile stats in the final summary output
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],

  scenarios: {
    read_only: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 20,
      maxVUs: 50,
      stages: [
        // Ramp up to steady read traffic
        { target: 3, duration: '30s' },
        { target: 3, duration: '5m' },
        { target: 0, duration: '30s' },
      ],
      exec: 'readOnlyScenario',
    },

    read_comment: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 20,
      maxVUs: 50,
      stages: [
        // Mixed read + write interaction load
        { target: 3, duration: '30s' },
        { target: 3, duration: '5m' },
        { target: 0, duration: '30s' },
      ],
      exec: 'readAndCommentScenario',
    },

    create_post: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 20,
      maxVUs: 50,
      stages: [
        // Write-heavy scenario (post creation)
        { target: 2, duration: '30s' },
        { target: 2, duration: '5m' },
        { target: 0, duration: '30s' },
      ],
      exec: 'createPostScenario',
    },

    update_profile: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 20,
      maxVUs: 50,
      stages: [
        // Profile update workload
        { target: 2, duration: '30s' },
        { target: 2, duration: '5m' },
        { target: 0, duration: '30s' },
      ],
      exec: 'updateProfileScenario',
    },
  },

  thresholds: {
    // System reliability requirement: less than 1% request failure rate
    http_req_failed: ['rate<0.01'],

    // Global latency constraints (all HTTP requests)
    http_req_duration: [
      'p(95)<1500',
      'p(99)<2000',
    ],

    // Latency constraints for successful responses only
    'http_req_duration{expected_response:true}': [
      'p(95)<1500',
      'p(99)<2000',
    ],
  },
};