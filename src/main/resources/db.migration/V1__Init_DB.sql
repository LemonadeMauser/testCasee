CREATE SEQUENCE comment_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE task_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE usr_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE comment (
                         id BIGINT NOT NULL,
                         text VARCHAR(255),
                         user_id BIGINT,
                         task_id BIGINT,
                         PRIMARY KEY (id)
);

CREATE TABLE task (
                      id BIGINT NOT NULL,
                      header VARCHAR(255),
                      message VARCHAR(255),
                      priority VARCHAR(255),
                      status VARCHAR(255),
                      author_id BIGINT,
                      executor_id BIGINT,
                      PRIMARY KEY (id)
);

CREATE TABLE task_comments (
                               task_id BIGINT NOT NULL,
                               comment_id BIGINT NOT NULL
);

CREATE TABLE usr (
                     id BIGINT NOT NULL,
                     activation_code VARCHAR(255),
                     active BOOLEAN NOT NULL,
                     email VARCHAR(255),
                     password VARCHAR(255),
                     roles SMALLINT ARRAY,
                     username VARCHAR(255),
                     PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS task_comments ADD CONSTRAINT unique_comment_id UNIQUE (comment_id);
ALTER TABLE IF EXISTS comment ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES usr;
ALTER TABLE IF EXISTS comment ADD CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES task;
ALTER TABLE IF EXISTS task ADD CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES usr;
ALTER TABLE IF EXISTS task ADD CONSTRAINT fk_executor_id FOREIGN KEY (executor_id) REFERENCES usr;
ALTER TABLE IF EXISTS task_comments ADD CONSTRAINT fk_comment_id FOREIGN KEY (comment_id) REFERENCES comment;
ALTER TABLE IF EXISTS task_comments ADD CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES task;
