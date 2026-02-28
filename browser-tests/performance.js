// Browser-level performance measurement using Playwright
// Captures navigation timing and key lifecycle metrics

const { chromium } = require('playwright');
const fs = require('fs');

const BASE_URL = 'http://localhost:8080';
const EMAIL = 'ci_test@mail.com';
const PASSWORD = 'test1234';

(async () => {
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();

  // Start navigation
  await page.goto(`${BASE_URL}/login`, { waitUntil: 'load' });

  // Perform login
  await page.fill('input[name="username"]', EMAIL);
  await page.fill('input[name="password"]', PASSWORD);
  await Promise.all([
    page.waitForNavigation({ waitUntil: 'load' }),
    page.click('button[type="submit"]'),
  ]);

  // Navigate to posts page
  await page.goto(`${BASE_URL}/posts`, { waitUntil: 'load' });

  // Extract Navigation Timing metrics
  const metrics = await page.evaluate(() => {
    const timing = performance.getEntriesByType('navigation')[0];

    return {
      dns: timing.domainLookupEnd - timing.domainLookupStart,
      tcp: timing.connectEnd - timing.connectStart,
      tls: timing.secureConnectionStart > 0
          ? timing.connectEnd - timing.secureConnectionStart
          : 0,
      ttfb: timing.responseStart - timing.requestStart,
      domContentLoaded:
          timing.domContentLoadedEventEnd - timing.startTime,
      loadEvent:
          timing.loadEventEnd - timing.startTime,
    };
  });

  console.log('Browser performance metrics:');
  console.table(metrics);

  // Save results
  fs.writeFileSync(
      'browser-tests/results/performance-results.json',
      JSON.stringify(metrics, null, 2)
  );

  await browser.close();
})();