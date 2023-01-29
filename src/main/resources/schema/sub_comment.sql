CREATE TABLE sub_comment
(
    sub_comment_id   INT       NOT NULL AUTO_INCREMENT,
    comment_id       INT       NOT NULL,
    sub_comment_body CHAR(255) NOT NULL DEFAULT 'empty comment',
    member_id        INT       NOT NULL,
    post_id          INT       NOT NULL,

    PRIMARY KEY (sub_comment_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id)
);