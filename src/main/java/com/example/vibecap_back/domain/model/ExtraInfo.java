package com.example.vibecap_back.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExtraInfo {
    private String season;
    private String time;
    private String feeling;

    @Override
    public String toString() {
        return ' ' + season + ' ' + time + ' ' + feeling;
    }

    public ExtraInfo(String infoString) {
        String[] info = infoString.split(" ");
        this.season = info[0];
        this.time = info[1];
        this.feeling = info[2];
    }
}
