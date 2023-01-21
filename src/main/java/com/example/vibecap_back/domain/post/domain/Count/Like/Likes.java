package com.example.vibecap_back.domain.post.domain.Count.Like;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name="Like")
@IdClass(PostLikeCount.class)
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    /*@Column(table = "post")
    private Long like_number;*/
}