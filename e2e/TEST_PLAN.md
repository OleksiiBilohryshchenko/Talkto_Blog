# E2E Test Plan – TalkTo

**Tool:** Selenium IDE  
**Browser:** Firefox  
**Base URL:** `http://localhost:8080`  
**Stack:** Spring Boot + Thymeleaf

---

## Known Constraints

| # | Problem | Workaround |
|---|---------|------------|
| 1 | `type` fails in Firefox | Use `executeScript` and set `.value` directly |
| 2 | `${var}` not supported inside `executeScript` | Inline values directly |
| 3 | Selenium IDE cannot extract dynamic reset link | Reset flow split into two tests |
| 4 | Reset token is single-use | Always regenerate token |

**Rule:** For `executeScript`, JavaScript must be inside `Target`. `Value` stays empty.

---

## Selector Strategy

All tests use only `data-testid` selectors:

```
css=[data-testid="..."]
```

No class-based or DOM-position selectors are used.

---

## TC1 – Registration

**Email:** `testuser@test.com`

| # | Command | Target | Value |
|---|---------|--------|-------|
| 1 | open | `/` | |
| 2 | click | `css=[data-testid="login-register-link"]` | |
| 3 | executeScript | `document.querySelector('[data-testid="register-name-input"]').value = 'TestUser';` | |
| 4 | executeScript | `document.querySelector('[data-testid="register-email-input"]').value = 'testuser@test.com';` | |
| 5 | executeScript | `document.querySelector('[data-testid="register-password-input"]').value = 'Test12345';` | |
| 6 | click | `css=[data-testid="register-submit-btn"]` | |
| 7 | waitForElementPresent | `css=[data-testid="login-submit-btn"]` | `10000` |
| 8 | executeScript | `document.querySelector('[data-testid="login-email-input"]').value = 'testuser@test.com';` | |
| 9 | executeScript | `document.querySelector('[data-testid="login-password-input"]').value = 'Test12345';` | |
| 10 | click | `css=[data-testid="login-submit-btn"]` | |
| 11 | waitForElementPresent | `css=[data-testid="posts-list-title"]` | `10000` |

**Expected:** Posts page visible.

---

## TC2 – Login

**Email:** `testlogin@test.com`

| # | Command | Target | Value |
|---|---------|--------|-------|
| 1 | open | `/register` | |
| 2 | executeScript | `document.querySelector('[data-testid="register-name-input"]').value = 'TestUser';` | |
| 3 | executeScript | `document.querySelector('[data-testid="register-email-input"]').value = 'testlogin@test.com';` | |
| 4 | executeScript | `document.querySelector('[data-testid="register-password-input"]').value = 'Test12345';` | |
| 5 | click | `css=[data-testid="register-submit-btn"]` | |
| 6 | waitForElementPresent | `css=[data-testid="login-submit-btn"]` | `10000` |
| 7 | executeScript | `document.querySelector('[data-testid="login-email-input"]').value = 'testlogin@test.com';` | |
| 8 | executeScript | `document.querySelector('[data-testid="login-password-input"]').value = 'Test12345';` | |
| 9 | click | `css=[data-testid="login-submit-btn"]` | |
| 10 | waitForElementPresent | `css=[data-testid="posts-list-title"]` | `10000` |

**Expected:** Login successful, posts page visible.

---

## TC3 – Create Post

**Email:** `testpost@test.com`

| # | Command | Target | Value |
|---|---------|--------|-------|
| 1–10 | Register + Login | Use TC2 steps with email `testpost@test.com` | |
| 11 | open | `/posts/new` | |
| 12 | executeScript | `document.querySelector('[data-testid="post-title-input"]').value = 'Test Post Title';` | |
| 13 | executeScript | `document.querySelector('[data-testid="post-content-textarea"]').value = 'Test post content here';` | |
| 14 | click | `css=[data-testid="post-submit-btn"]` | |
| 15 | waitForElementPresent | `css=[data-testid="posts-list-title"]` | `10000` |

**Expected:** Redirect to posts page.

---

## TC4 – Add Comment

**Email:** `testcomment@test.com`

| # | Command | Target | Value |
|---|---------|--------|-------|
| 1–10 | Register + Login | Use TC2 steps with email `testcomment@test.com` | |
| 11 | open | `/posts/new` | |
| 12 | executeScript | `document.querySelector('[data-testid="post-title-input"]').value = 'Comment Test Post';` | |
| 13 | executeScript | `document.querySelector('[data-testid="post-content-textarea"]').value = 'Post for comment test';` | |
| 14 | click | `css=[data-testid="post-submit-btn"]` | |
| 15 | waitForElementPresent | `css=[data-testid="posts-list-title"]` | `10000` |
| 16 | click | `css=.post-card:first-child [data-testid="post-title-link"]` | |
| 17 | waitForElementPresent | `css=[data-testid="comment-textarea"]` | `10000` |
| 18 | executeScript | `document.querySelector('[data-testid="comment-textarea"]').value = 'My test comment';` | |
| 19 | click | `css=[data-testid="comment-submit-btn"]` | |
| 20 | waitForElementPresent | `css=[data-testid="comment-content"]` | `10000` |

**Expected:** Comment visible on post page.

---

## TC5 – Profile

**Email:** `testprofile@test.com`

| # | Command | Target | Value |
|---|---------|--------|-------|
| 1–10 | Register + Login | Use TC2 steps with email `testprofile@test.com` | |
| 11 | open | `/profile` | |
| 12 | waitForElementPresent | `css=[data-testid="profile-view-title"]` | `10000` |
| 13 | waitForElementPresent | `css=[data-testid="profile-user-email"]` | `10000` |
| 14 | assertText | `css=[data-testid="profile-user-email"]` | `testprofile@test.com` |

**Expected:** Profile loads and email matches registered value.

---

## TC6\_Password\_Reset

**Email:** `testreset@test.com`

| # | Command | Target | Value |
|---|---------|--------|-------|
| 1 | open | `/register` | |
| 2 | executeScript | `document.querySelector('[data-testid="register-name-input"]').value = 'TestUser';` | |
| 3 | executeScript | `document.querySelector('[data-testid="register-email-input"]').value = 'testreset@test.com';` | |
| 4 | executeScript | `document.querySelector('[data-testid="register-password-input"]').value = 'OldPass123';` | |
| 5 | click | `css=[data-testid="register-submit-btn"]` | |
| 6 | waitForElementPresent | `css=[data-testid="login-submit-btn"]` | `10000` |
| 7 | open | `/password/forgot` | |
| 8 | executeScript | `document.querySelector('[data-testid="forgot-email-input"]').value = 'testreset@test.com';` | |
| 9 | click | `css=[data-testid="forgot-submit-btn"]` | |
| 10 | open | `http://localhost:8025` | |

**Expected:** Reset email delivered to MailHog.

---

## TC6\_Reset\_Complete

**Note:** Must use a fresh token from MailHog. Always run `TC6_Password_Reset` immediately before this test.

| # | Command | Target | Value |
|---|---------|--------|-------|
| 1 | open | `/password/reset?token=<dynamic>` | |
| 2 | waitForElementPresent | `css=[data-testid="reset-password-input"]` | `10000` |
| 3 | executeScript | `document.querySelector('[data-testid="reset-password-input"]').value = 'NewPass123';` | |
| 4 | click | `css=[data-testid="reset-submit-btn"]` | |
| 5 | waitForElementPresent | `css=[data-testid="login-submit-btn"]` | `10000` |
| 6 | executeScript | `document.querySelector('[data-testid="login-email-input"]').value = 'testreset@test.com';` | |
| 7 | executeScript | `document.querySelector('[data-testid="login-password-input"]').value = 'NewPass123';` | |
| 8 | click | `css=[data-testid="login-submit-btn"]` | |
| 9 | waitForElementPresent | `css=[data-testid="posts-list-title"]` | `10000` |

**Expected:** Login successful with new password, posts page visible.

---

## Database Cleanup

Run before each full suite execution:

```sql
DELETE FROM users WHERE email LIKE 'test%@test.com';
```

---

## Project Structure

```
e2e/
  selenium-ide/
    talkto-e2e.side
    README.md
```

---

## Execution Order

1. TC1 – Registration
2. TC2 – Login
3. TC3 – Create Post
4. TC4 – Add Comment
5. TC5 – Profile
6. TC6\_Password\_Reset
7. TC6\_Reset\_Complete