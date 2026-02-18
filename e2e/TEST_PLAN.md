# E2E Test Plan – TalkTo

## Test Strategy

All E2E tests are:
- Independent
- Data-isolated
- Using only data-testid selectors
- Designed to run in CI
- Creating their own test data (unique email per test)

---

## 1. Registration Flow

Goal: Verify user can register successfully.

Flow:
1. Open /register
2. Generate unique email
3. Fill name
4. Fill email
5. Fill password
6. Submit form
7. Verify redirect to posts page

Expected:
User is registered and redirected to posts list.

---

## 2. Login Flow

Goal: Verify user can log in.

Flow:
1. Generate unique email
2. Register user
3. Open /login
4. Enter email
5. Enter password
6. Submit
7. Verify posts page

Expected:
User is authenticated successfully.

---

## 3. Create Post Flow

Goal: Verify authenticated user can create a post.

Flow:
1. Generate unique email
2. Register
3. Login
4. Open /posts/new
5. Enter title
6. Enter content
7. Submit
8. Verify post is visible

Expected:
Post appears in posts list.

---

## 4. Add Comment Flow

Goal: Verify authenticated user can comment.

Flow:
1. Generate unique email
2. Register
3. Login
4. Create post
5. Open post
6. Add comment
7. Verify comment is visible

Expected:
Comment is successfully added.

---

## 5. Profile Flow

Goal: Verify profile page works.

Flow:
1. Generate unique email
2. Register
3. Login
4. Open /profile
5. Verify user block visible

Expected:
Profile page displays user info.

---

## 6. Full Password Reset Flow

Goal: Verify password reset end-to-end.

Flow:
1. Generate unique email
2. Register
3. Open /password/forgot
4. Submit email
5. Open MailHog (localhost:8025)
6. Open latest email
7. Extract reset link
8. Open reset link
9. Set new password
10. Login with new password
11. Verify successful login

Expected:
Password reset works correctly.