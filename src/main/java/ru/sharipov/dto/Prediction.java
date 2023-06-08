package ru.sharipov.dto;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Prediction {

    private String predictor;
    private String prediction;
    private Set<String> tags = new HashSet<>();

    public String getPredictor() {
        return predictor;
    }

    public void setPredictor(String predictor) {
        this.predictor = predictor;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void addAllTags(Set<String> tags) {
        this.tags.addAll(tags);
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "predictor='" + predictor + '\'' +
                ", prediction='" + prediction + '\'' +
                ", tags=" + Arrays.toString(tags.toArray()) +
                '}';
    }
}
