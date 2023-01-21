package com.example.vibecap_back.domain.vibe.application;

import com.example.vibecap_back.domain.model.ExtraInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryMaker {

    private String[] weatherList = {
            "화창한", "흐린 날", "눈 내리는", "비오는", "쌀쌀한" ,"무더운"
    };
    private String[] timeList = {
            "아침", "낮", "저녁", "밤", "새벽"
    };
    // TODO 감정 단순화
    private String[] feelingList = {
            "신나는", "포근한", "신선한", "낭만적인", "잔잔한", "우울한",
            "공허한", "분노한", "심심한"
    };

    private static String PLAYLIST_KR = "플레이리스트";
    private static String SONG_KR = "노래";
    private static String MUSIC_KR = "음악";
    private static String PLAYLIST_EN = "playlist";

    /**
     * 사진과 추가 정보 모두 사용해서 query 생성
     * @param extraInfo
     * weather, time, feeling
     * @param label
     * 이미지에서 추출한 label
     * @return
     */
    public String assemble(ExtraInfo extraInfo, String label) {
        String query;
        String weather = extraInfo.getWeather();
        String time = extraInfo.getTime();
        String feeling = extraInfo.getFeeling();

        query = String.format("%s %s %s %s %s",
                weather, time, feeling, label, PLAYLIST_KR);

        System.out.println(query);

        return query;
    }

    /**
     * 추가 정보만 사용해서 query 생성
     * @param extraInfo
     * weather, time, feeling
     * @return
     */
    public String assemble(ExtraInfo extraInfo) {
        String query;
        String weather = extraInfo.getWeather();
        String time = extraInfo.getTime();
        String feeling = extraInfo.getFeeling();

        query = String.format("%s %s %s %s",
                weather, time, feeling, PLAYLIST_KR);

        System.out.println(query);

        return query;
    }
}
