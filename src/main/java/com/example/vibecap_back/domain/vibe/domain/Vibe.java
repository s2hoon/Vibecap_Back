package com.example.vibecap_back.domain.vibe.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "vibe")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vibe {

    // vibe 식별자
    @Column(name = "vibe_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vibeId;

    // vibe를 만든 회원 식별자
    @Column(name = "member_id")
    private Long memberId;

    // vibe 생성에 사용된 이미지
    @Column(name = "vibe_image")
    private String vibeImage;

    // 추천된 플레이리스트 링크
    @Column(name = "youtube_link")
    private String youtubeLink;

    /**
     * vibe 생성에 사용된 키워드
     * "label weather time feeling" 으로 구성된 문자열
     * ' '(공백)으로 split 했을 때 첫 번째 원소가 label, 마지막 원소가 feeling.
     * label + extraInfo.toString() 으로 생성 가능
     */
    @Column(name = "vibe_keywords")
    private String vibeKeywords;
}
