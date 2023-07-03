package ru.sharipov.dto;

import ru.sharipov.emun.PredictionDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PredictionMap {

    private final ConcurrentHashMap<PredictionDay, List<PredictionDto>> map = new ConcurrentHashMap<>();

    public HashMap<PredictionDay, List<PredictionDto>> getAll() {
        return new HashMap<>(map);
    }

    public void put(PredictionDay day, List<PredictionDto> predictionDtos) {
        this.map.put(day, predictionDtos);
    }

    public void add(PredictionDay day, PredictionDto predictionDto) {
        checkAndInitilyzeValue(day);
        this.map.get(day).add(predictionDto);
    }

    public void addAll(PredictionDay day, List<PredictionDto> predictions) {
        checkAndInitilyzeValue(day);
        this.map.get(day).addAll(predictions);
    }

    public void merge(PredictionMap map) {
        map.keySet().forEach(k -> this.addAll(k, map.get(k)));
    }

    public Set<PredictionDay> keySet() {
        return map.keySet();
    }

    public List<PredictionDto> get(PredictionDay day) {
        return map.get(day);
    }

    private void checkAndInitilyzeValue(PredictionDay day) {
        this.map.computeIfAbsent(day, k -> new ArrayList<>());
    }

    public List<PredictionDto> values() {
        return this.map.values().stream().flatMap(List::stream).toList();
    }
}
