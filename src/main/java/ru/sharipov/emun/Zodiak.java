package ru.sharipov.emun;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

public enum Zodiak {
    CAPRICORN("Козерог", LocalDate.of(1899, 12, 22), LocalDate.of(1900, 1, 19)),
    AQUARIUS("Водолей", LocalDate.of(1900, 1, 20), LocalDate.of(1900, 2, 18)),
    PISCES("Рыбы", LocalDate.of(1900, 2, 19), LocalDate.of(1900, 3, 20)),
    ARIES("Овен", LocalDate.of(1900, 3, 21), LocalDate.of(1900, 4, 19)),
    TAURUS("Телец", LocalDate.of(1900, 4, 20), LocalDate.of(1900, 5, 20)),
    GEMINI("Близнецы", LocalDate.of(1900, 5, 21), LocalDate.of(1900, 6, 20)),
    CANCER("Рак", LocalDate.of(1900, 6, 21), LocalDate.of(1900, 7, 22)),
    LEO("Лев", LocalDate.of(1900, 7, 23), LocalDate.of(1900, 8, 22)),
    VIRGO("Дева", LocalDate.of(1900, 8, 23), LocalDate.of(1900, 9, 22)),
    LIBRA("Весы", LocalDate.of(1900, 9, 23), LocalDate.of(1900, 10, 22)),
    SCORPIO("Скорпион", LocalDate.of(1900, 10, 23), LocalDate.of(1900, 11, 21)),
    SAGITTARIUS("Стрелец", LocalDate.of(1900, 11, 22), LocalDate.of(1900, 12, 21));
    private String rus;
    private LocalDate validFrom;
    private LocalDate validTo;

    Zodiak(String rus, LocalDate validFrom, LocalDate validTo) {
        this.rus = rus;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getRus() {
        return rus;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public static Zodiak decode(LocalDate birthday) {
        Optional<Zodiak> zodiak = Arrays.stream(Zodiak.values())
                .filter(v -> v.validFrom.getDayOfYear() <= birthday.getDayOfYear() && birthday.getDayOfYear() < v.validTo.getDayOfYear())
                .findAny();
        if (zodiak.isPresent()) {
            return zodiak.get();
        }

        if (birthday.getDayOfYear() < CAPRICORN.validTo.getDayOfYear() || birthday.getDayOfYear() >= CAPRICORN.validFrom.getDayOfYear())
            return CAPRICORN;

        throw new RuntimeException(
                String.format("Не удается определить знак зодиака для даты рождения %s",
                        birthday.toString()));
    }
}
