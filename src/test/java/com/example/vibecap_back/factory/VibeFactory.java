package com.example.vibecap_back.factory;

import com.example.vibecap_back.domain.vibe.domain.Vibe;

public class VibeFactory {

    private static final String BASE_IMAGE_URI = "dummy image_uri ";
    private static final String BASE_YOUTUBE_LINK = "dummy youtube link ";
    private static final String BASE_KEYWORDS = "label season time feeling";

    /**
     * @param memberId
     * vibe를 생성한 member_id
     * @param i
     * @return
     */
    public static Vibe getVibe(Long memberId, Integer i) {
           Vibe dummy = Vibe.builder()
                   .memberId(memberId)
                   .vibeImage(BASE_IMAGE_URI + i.toString())
                   .youtubeLink(BASE_YOUTUBE_LINK + i.toString())
                   .vibeKeywords(BASE_KEYWORDS)
                   .build();

           return dummy;
    }

    /**
     * getVibe(Long, Integer) 메서드로 생성한 vibe가 db에 저장된 형태를 반환
     * @param memberId
     * vibe를 생성한 member_id
     * @param i
     * @return
     */
    public static Vibe selectVibe(Long memberId, Integer i) {

        Vibe vibe = Vibe.builder()
                .vibeId(i.longValue())
                .memberId(memberId)
                .vibeImage(BASE_IMAGE_URI + i.toString())
                .youtubeLink(BASE_YOUTUBE_LINK + i.toString())
                .vibeKeywords(BASE_KEYWORDS)
                .build();

        return vibe;
    }
}
