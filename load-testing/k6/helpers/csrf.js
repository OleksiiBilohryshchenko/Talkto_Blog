// Extract CSRF token from HTML form input
// Assumes hidden input: <input type="hidden" name="_csrf" value="...">

export function extractCsrfToken(html) {
  const match = html.match(/name="_csrf"\s+value="([^"]+)"/);

  if (!match) {
    throw new Error('CSRF token not found in HTML response');
  }

  return match[1];
}