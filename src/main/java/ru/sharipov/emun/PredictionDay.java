package ru.sharipov.emun;

import java.util.Arrays;

public enum PredictionDay {
    D0 ("#D0", 0),
    D1 ("#D1", 1),
    D2 ("#D2", 2),
    D3 ("#D3", 3),
    D4 ("#D4", 4),
    D5 ("#D5", 5),
    D6 ("#D6", 6),
    DI("#Ignore", -1);

    private final String tag;
    private final int day;
    PredictionDay(String tag, int day) {
        this.tag = tag;
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public String getTag(){
        return tag;
    }
    public static PredictionDay encode(String text, PredictionDay currentDay) {
        if (text.contains("Dany")) {
            return currentDay;
        }
        return Arrays.stream(PredictionDay.values())
                .filter(p -> p.getTag().equals(text))
                .findAny()
                .orElse(currentDay);
    }




}
