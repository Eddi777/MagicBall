package ru.sharipov.dto;

import ru.sharipov.emun.PredictionDay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PredictionMap {

    private ConcurrentHashMap<PredictionDay, List<Prediction>> map = new ConcurrentHashMap<>();

    public HashMap<PredictionDay, List<Prediction>> getAll() {
        return new HashMap<>(map);
    }

    public void put(PredictionDay day, List<Prediction> predictions) {
        this.map.put(day, predictions);
    }

    public Set<PredictionDay> keySet() {
        return map.keySet();
    }

    public List<Prediction> get(PredictionDay day) {
        return map.get(day);
    }

}
