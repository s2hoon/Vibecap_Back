package com.example.vibecap_back.domain.post.domain.Tag;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
@Table(name="tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Setter
    @Column(name="tag_name",length = 16, nullable = false)
    private String tagName;

}
