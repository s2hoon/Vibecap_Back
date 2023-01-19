package com.example.vibecap_back.domain.post.domain.Scrap;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name="Scrap")
@IdClass(PostScrapCount.class)
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    /*@Column(table = "post")
    private Long scrap_number;*/
}
