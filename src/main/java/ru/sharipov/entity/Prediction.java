package ru.sharipov.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.sharipov.enums.TextSentiment;

@Entity
@Table(name = "PREDICTIONS")
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREDICTION_ID", nullable = false)
    private long predictionId; //Идентификатор сайта для предсказания
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUEST_ID", nullable = false)
    private Request request; //ИД запроса
    @Column(name = "PREDICTION", nullable = false, length = 2000)
    private String prediction; //Текст предсказания
    @Column(name = "PREDICTION_ENCODE", length = 10)
    @Enumerated(EnumType.STRING)
    private TextSentiment predictionEncode; //Анализ предсказания
    @Column(name = "TAGS", length = 500)
    private String tags; //Тэги предсказания

    public long getPredictionId() {
        return predictionId;
    }

    public void setPredictionId(long predictionId) {
        this.predictionId = predictionId;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        if (prediction.length() > 2000) {
            prediction = prediction.substring(0, 1999);
        }
        this.prediction = prediction;
    }

    public TextSentiment getPredictionEncode() {
        return this.predictionEncode;
    }

    public void setPredictionEncode(TextSentiment predictionEncode) {
        this.predictionEncode = predictionEncode;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "predictionId=" + predictionId +
                ", request=" + request.getRequestId() +
                ", prediction='" + prediction + '\'' +
                ", predictionEncode='" + predictionEncode + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
