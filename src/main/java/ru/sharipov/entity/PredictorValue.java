package ru.sharipov.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PREDICTOR_VALUES")
public class PredictorValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VALUE_ID")
    private Long valueId; //»дентификатор
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name", nullable = false)
    private Predictor predictor; //”казатель на предиктор
    @Column(name="ENDPOINT")
    private String endpoint; //Ёндпоинт (полный url) дл€ дополнительного запроса (при необходимости)
    @Column(name="REGEX", nullable = false)
    private String regex; //–егул€рное выражение дл€ получени€ нужных данных
    @Column(name="TAG", nullable = false)
    private String tag; //“эги дл€ предсказани€ (разделитель - зап€та€)

    public Long getValueId() {
        return valueId;
    }

    public void setValueId(Long valueId) {
        this.valueId = valueId;
    }

    public Predictor getPredictor() {
        return predictor;
    }

    public void setPredictor(Predictor predictor) {
        this.predictor = predictor;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
