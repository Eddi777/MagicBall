package ru.sharipov.emun;

import java.util.Arrays;

public enum PredictionDay {
    D0 ("#D0"),
    D1 ("#D1"),
    D2 ("#D2"),
    D3 ("#D3"),
    D4 ("#D4"),
    D5 ("#D5"),
    D6 ("#D6"),
    DI("#Ignore");

    private final String tag;
    PredictionDay(String tag) {
        this.tag = tag;
    }

    public String getTag(){
        return tag;
    }
    public static PredictionDay encode(String text) {
        return Arrays.stream(PredictionDay.values())
                .filter(p -> p.getTag().equals(text))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Ошибка дешифровки PredictionDay"));
    }
}
