INSERT INTO users (created_at, updated_at, name, email, password)
SELECT
    NOW(),
    NOW(),
    'Stress User ' || g,
    'stress_user_' || g || '@test.com',
    '$2a$10$7QJzZP1jR9Wm3OePZ8cZGeW4wKX6xP1jO7Z7mT0ZpX0lZz7bX8lG2'
FROM generate_series(1, 500) AS g;