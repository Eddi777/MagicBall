package ru.sharipov.dto;

import java.util.ArrayList;
import java.util.List;

public class Prediction {

    private String predictor;
    private String prediction;
    private List<String> tags = new ArrayList<>();

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
