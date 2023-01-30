CREATE TABLE notice(
    notice_id               INT             NOT NULL AUTO_INCREMENT ,
    target_type             INT             NOT NULL ,
    target_id               INT             NOT NULL ,
    event_type              INT             NOT NULL ,
    created_time            DATETIME(6)     NOT NULL ,
    receiver_id             INT             NOT NULL ,
    is_read                 BOOLEAN         DEFAULT FALSE ,

    PRIMARY KEY (notice_id) ,
    FOREIGN KEY (receiver_id) REFERENCES member(member_id)
                   ON DELETE CASCADE
);