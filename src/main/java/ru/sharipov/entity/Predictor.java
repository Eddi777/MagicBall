package ru.sharipov.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "PREDICTORS")
public class Predictor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREDICTOR_ID", nullable = false)
    private long predictorId; //Идентификатор сайта для предсказания
    @Column(name = "NAME", unique = true, nullable = false)
    private String name; //Имя астропрогноза, уникальное значение
    @Column(name = "COMMENT")
    private String comment; //комментарий
    @Column(name = "HOST", nullable = false)
    private String host; //Хост источника
    @Column(name = "CHECK_DATA")
    private String checkData; //Контрольная строка для проверки правильности ответа источника (напр Title)
//    @OneToMany(mappedBy = "valueId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PredictorValue> values;

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

//    public List<PredictorValue> getValues() {
//        return values;
//    }
//
//    public void setValues(List<PredictorValue> values) {
//        this.values = values;
//    }
}
