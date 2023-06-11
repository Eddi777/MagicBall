package ru.sharipov.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Arrays;
import java.util.Set;

@Entity
@Table(name = "PREDICTORS")
public class Predictor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREDICTOR_ID", nullable = false)
    private long predictorId; //Идентификатор сайта для предсказания
    @Column(name = "NAME", unique = true, nullable = false, length = 100)
    private String name; //Имя астропрогноза, уникальное значение
    @Column(name = "COMMENT", length = 500)
    private String comment; //комментарий
    @Column(name = "HOST", nullable = false, length = 50)
    private String host; //Хост источника
    @Column(name = "CHECK_DATA", length = 500)
    private String checkData; //Контрольная строка для проверки правильности ответа источника (напр Title)
    @OneToMany(mappedBy = "predictor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PredictorValue> values;

    public long getPredictorId() {
        return predictorId;
    }

    public void setPredictorId(long predictorId) {
        this.predictorId = predictorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCheckData() {
        return checkData;
    }

    public void setCheckData(String checkData) {
        this.checkData = checkData;
    }

    public Set<PredictorValue> getValues() {
        return values;
    }

    public void setValues(Set<PredictorValue> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Predictor{" +
                "predictorId=" + predictorId +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", host='" + host + '\'' +
                ", checkData='" + checkData + '\'' +
                ", values=" + Arrays.toString(values.toArray()) +
                '}';
    }
}
