CREATE TABLE notice(
    type            INT         NOT NULL ,
    payload_id      INT         NOT NULL ,
    created_time    DATETIME(6) NOT NULL ,
    receiver_id     INT         NOT NULL ,

    PRIMARY KEY (type, payload_id),
    FOREIGN KEY (receiver_id) REFERENCES member(member_id)
                   ON DELETE CASCADE
);