package com.example.vibecap_back.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExtraInfo {
    private String weather;
    private String time;
    private String feeling;

    @Override
    public String toString() {
        return ' ' + weather + ' ' + time + ' ' + feeling;
    }

    public ExtraInfo(String infoString) {
        String[] info = infoString.split(" ");
        this.weather = info[0];
        this.time = info[1];
        this.feeling = info[2];
    }
}
