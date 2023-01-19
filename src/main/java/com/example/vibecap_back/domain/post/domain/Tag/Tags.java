package com.example.vibecap_back.domain.post.domain.Tag;

import com.example.vibecap_back.domain.post.domain.Posts;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
@Table(name="Tag")
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long id;

    @Setter
    @Column(length = 16, nullable = false)
    private String tag_name;

    public Tags(Long id, String tag_name) {
        this.id = id;
        this.tag_name = tag_name;
    }
}
