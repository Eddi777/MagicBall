package ru.sharipov.emun;

import java.util.Arrays;

public enum PredictionEncode {
    POSITIVE_VERY ("Positive+"),
    POSITIVE ("Positive"),
    NEUTRAL ("Neutral"),
    NEGATIVE ("Negative"),
    NEGATIVE_VERY ("Negative-");
    private final String text;

    PredictionEncode(String text) {
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public static PredictionEncode encode(String text) {
        return Arrays.stream(PredictionEncode.values())
                .filter(p -> p.text.equals(text))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Ошибка дешифровки PredictionEncode"));
    }
}
