CREATE TABLE comment(
    comment_id          INT         NOT NULL AUTO_INCREMENT,
    post_id             INT         NOT NULL,
    commend_body        CHAR(255)   NOT NULL,

    PRIMARY KEY (comment_id)
);