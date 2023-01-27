CREATE TABLE post_like(
    like_id     INT     NOT NULL AUTO_INCREMENT,
    member_id   INT     NOT NULL,
    post_id     INT     NOT NULL,

    PRIMARY KEY (like_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
                 ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES post(post_id)
                 ON DELETE CASCADE
);