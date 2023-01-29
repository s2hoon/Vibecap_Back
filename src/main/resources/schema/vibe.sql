CREATE TABLE vibe(
    vibe_id         INT             NOT NULL AUTO_INCREMENT,
    member_id       INT             NOT NULL,
    vibe_image      VARCHAR(2048)   NOT NULL,
    youtube_link    CHAR(255)       NOT NULL,
    vibe_keywords   CHAR(255)       NOT NULL,

    PRIMARY KEY (vibe_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
                  ON DELETE CASCADE
);