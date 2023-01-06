CREATE TABLE hashtag(
    member_id   INT     NOT NULL,
    tag_id      INT     NOT NULL,

    PRIMARY KEY (member_id, tag_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
            ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag(tag_id)
            ON DELETE CASCADE
);