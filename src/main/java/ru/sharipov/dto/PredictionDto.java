package ru.sharipov.dto;

import ru.sharipov.entity.Request;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PredictionDto {

    private String predictor;
    private String prediction;
    private Set<String> tags = new HashSet<>();
    private Request request;

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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
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
