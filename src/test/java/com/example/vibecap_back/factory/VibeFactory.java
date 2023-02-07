package com.example.vibecap_back.factory;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.vibe.domain.Vibe;

import java.util.Arrays;

public class VibeFactory {

    private static final String BASE_IMAGE_URI = "dummy image_uri ";
    private static final String BASE_YOUTUBE_LINK = "dummy youtube link ";
    private static final String BASE_KEYWORDS = "label season time feeling";
    public static final int TAG_SUFFIX = 3;

    /**
     * @param memberId
     * vibe를 생성한 member_id
     * @param i
     * @return
     */
    public static Vibe getVibe(Long memberId, Integer i) {
        String iStr = Long.toString(i % TAG_SUFFIX);
        String keywords = String.format("%s %s %s %s",
                "label"+iStr, "season"+iStr, "time"+iStr, "feeling"+iStr);

       Vibe dummy = Vibe.builder()
               .member(MemberFactory.selectMember(i))
               .vibeImage(BASE_IMAGE_URI + i.toString())
               .youtubeLink(BASE_YOUTUBE_LINK + i.toString())
               .vibeKeywords(keywords)
               .build();

           return dummy;
    }

    /**
     * getVibe(Long, Integer) 메서드로 생성한 vibe가 db에 저장된 형태를 반환
     * vibeId값을 가진다
     * @param memberId
     * vibe를 생성한 member_id
     * @param i
     * @return
     */
    public static Vibe selectVibe(Long memberId, Integer i) {

        String iStr = Long.toString(i % 3);
        String keywords = String.format("%s %s %s %s",
                "label"+iStr, "season"+iStr, "time"+iStr, "feeling"+iStr);

        Vibe vibe = Vibe.builder()
                .vibeId(i.longValue())
                .member(MemberFactory.selectMember(i))
                .vibeImage(BASE_IMAGE_URI + i.toString())
                .youtubeLink(BASE_YOUTUBE_LINK + i.toString())
                .vibeKeywords(keywords)
                .build();

        return vibe;
    }

}
