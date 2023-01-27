CREATE TABLE post(
    post_id         INT         NOT NULL AUTO_INCREMENT,
    member_id       INT         NOT NULL ,
    post_title           CHAR(32)    NOT NULL ,
    post_body            TEXT        NOT NULL ,
    vibe_id         INT         NOT NULL ,
    like_number     INT         DEFAULT 0,
    scrap_number    INT         DEFAULT 0,
    comment_number      INT         DEFAULT 0,

    PRIMARY KEY (post_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
                 ON DELETE CASCADE
);