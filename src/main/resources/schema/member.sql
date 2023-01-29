CREATE TABLE member(
    member_id       INT         NOT NULL AUTO_INCREMENT,
    email           CHAR(64)    NOT NULL,
    password        CHAR(255)   NOT NULL,
    google_email    CHAR(64),
    role            CHAR(16)    NOT NULL,
    nickname        CHAR(64)    NOT NULL,
    state           CHAR(16)    NOT NULL,
    profile_image   VARCHAR(2048),

    PRIMARY KEY (member_id)
);