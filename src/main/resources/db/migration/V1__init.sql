---------------------------
-- Create the Table      --
---------------------------
--      ACCOUNTS
CREATE TABLE ACCOUNTS
(
    id         UUID               NOT NULL
                         DEFAULT gen_random_uuid() PRIMARY KEY,

    email      VARCHAR(50) UNIQUE NOT NULL,
    password   VARCHAR(500)       NOT NULL,

    user_name  VARCHAR(50)        NOT NULL,

    created_at TIMESTAMP DEFAULT current_TIMESTAMP,
    update_at  TIMESTAMP DEFAULT current_TIMESTAMP,
    deleted    BOOLEAN   DEFAULT FALSE
);

--      TASK
CREATE TABLE TASK
(
    id          UUID         NOT NULL
                          DEFAULT gen_random_uuid() PRIMARY KEY,

    status      VARCHAR(10)  NOT NULL,
    priority      VARCHAR(10)  NOT NULL,
    header      VARCHAR(255) NOT NULL,
    text        VARCHAR(500) NOT NULL,
    executor_id UUID REFERENCES ACCOUNTS (id),
    author_id   UUID         NOT NULL REFERENCES ACCOUNTS (id),

    created_at  TIMESTAMP DEFAULT current_TIMESTAMP,
    update_at   TIMESTAMP DEFAULT current_TIMESTAMP,
    deleted     BOOLEAN   DEFAULT FALSE
);

--      COMMENT
CREATE TABLE COMMENT
(
    id         UUID         NOT NULL
                         DEFAULT gen_random_uuid() PRIMARY KEY,

    text       VARCHAR(500) NOT NULL,
    author_id  UUID         NOT NULL REFERENCES ACCOUNTS (id),
    task_id    UUID         NOT NULL REFERENCES TASK (id),

    created_at TIMESTAMP DEFAULT current_TIMESTAMP,
    update_at  TIMESTAMP DEFAULT current_TIMESTAMP,
    deleted    BOOLEAN   DEFAULT FALSE
);