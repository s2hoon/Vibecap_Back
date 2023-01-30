CREATE TABLE comment(
                        comment_id          INT         NOT NULL AUTO_INCREMENT,
                        post_id             INT         NOT NULL,
                        comment_body        CHAR(255)   NOT NULL,

                        PRIMARY KEY (comment_id)
);

CREATE TABLE hashtag(
                        member_id   INT     NOT NULL,
                        tag_id      INT     NOT NULL,

                        PRIMARY KEY (member_id, tag_id),
                        FOREIGN KEY (member_id) REFERENCES member(member_id)
                            ON DELETE CASCADE,
                        FOREIGN KEY (tag_id) REFERENCES tag(tag_id)
                            ON DELETE CASCADE
);

CREATE TABLE post_like(
                      member_id   INT     NOT NULL,
                      post_id     INT     NOT NULL,

                      PRIMARY KEY (member_id, post_id),
                      FOREIGN KEY (member_id) REFERENCES member(member_id)
                          ON DELETE CASCADE,
                      FOREIGN KEY (post_id) REFERENCES post(post_id)
                          ON DELETE CASCADE
);

CREATE TABLE member(
                       member_id       INT         NOT NULL AUTO_INCREMENT,
                       email           CHAR(64)    NOT NULL,
                       password        CHAR(255)   NOT NULL,
                       google_email    CHAR(64),
                       role            CHAR(16)    NOT NULL,
                       nickname        CHAR(64)    NOT NULL,
                       state           CHAR(16)    NOT NULL,
                       profile_image   MEDIUMBLOB  NOT NULL,

                       PRIMARY KEY (member_id)
);

CREATE TABLE post(
                     post_id         INT         NOT NULL AUTO_INCREMENT,
                     member_id       INT         NOT NULL ,
                     title           CHAR(32)    NOT NULL ,
                     body            TEXT        NOT NULL ,
                     vibe_id         INT         NOT NULL ,
                     like_number     INT         DEFAULT 0,
                     scrap_number    INT         DEFAULT 0,
                     tag_number      INT         DEFAULT 0,

                     PRIMARY KEY (post_id),
                     FOREIGN KEY (member_id) REFERENCES member(member_id)
                         ON DELETE CASCADE
);

CREATE TABLE scrap(
                      member_id   INT     NOT NULL,
                      post_id     INT     NOT NULL,

                      PRIMARY KEY (member_id, post_id),
                      FOREIGN KEY (member_id) REFERENCES member(member_id)
                          ON DELETE CASCADE,
                      FOREIGN KEY (post_id) REFERENCES post(post_id)
                          ON DELETE CASCADE
);

CREATE TABLE tag (
                     tag_id          INT         NOT NULL AUTO_INCREMENT,
                     tag_name        CHAR(16)    NOT NULL,

                     PRIMARY KEY (tag_id)
);

CREATE TABLE vibe(
                     vibe_id         INT         NOT NULL AUTO_INCREMENT,
                     member_id       INT         NOT NULL,
                     vibe_image      MEDIUMBLOB  NOT NULL,
                     youtube_link    CHAR(255)   NOT NULL,
                     vibe_keywords   CHAR(255)   NOT NULL,

                     PRIMARY KEY (vibe_id),
                     FOREIGN KEY (member_id) REFERENCES member(member_id)
                         ON DELETE CASCADE
);