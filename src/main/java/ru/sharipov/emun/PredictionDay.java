package ru.sharipov.emun;

public enum PredictionDay {
    D0 ("#Day0"),
    D1 ("#Day1"),
    D2 ("#Day2"),
    D3 ("#Day3"),
    D4 ("#Day4"),
    D5 ("#Day5"),
    D6 ("#Day6"),
    DE ("#Error"); //Error

    private final String tag;
    PredictionDay(String tag) {
        this.tag = tag;
    }

    public String getTag(){
        return tag;
    }
}
