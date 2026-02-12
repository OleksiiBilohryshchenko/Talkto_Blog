-- USERS
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(150) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP
);

-- POSTS
CREATE TABLE posts (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       content TEXT NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       author_id BIGINT NOT NULL,
                       CONSTRAINT fk_post_author
                           FOREIGN KEY (author_id)
                               REFERENCES users(id)
                               ON DELETE CASCADE
);

-- COMMENTS
CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          content TEXT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          author_id BIGINT NOT NULL,
                          post_id BIGINT NOT NULL,
                          CONSTRAINT fk_comment_author
                              FOREIGN KEY (author_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE,
                          CONSTRAINT fk_comment_post
                              FOREIGN KEY (post_id)
                                  REFERENCES posts(id)
                                  ON DELETE CASCADE
);

-- INDEXES
CREATE INDEX idx_posts_author_id ON posts(author_id);
CREATE INDEX idx_comments_post_id ON comments(post_id);
CREATE INDEX idx_comments_author_id ON comments(author_id);