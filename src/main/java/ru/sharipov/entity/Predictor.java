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
    private long predictorId; //»дентификатор сайта дл€ предсказани€
    @Column(name = "NAME", unique = true, nullable = false, length = 100)
    private String name; //»м€ астропрогноза, уникальное значение
    @Column(name = "COMMENT", length = 500)
    private String comment; //комментарий
    @Column(name = "HOST", nullable = false, length = 500)
    private String host; //’ост источника
    @Column(name = "CHECK_DATA", length = 500)
    private String checkData; // онтрольна€ строка дл€ проверки правильности ответа источника (напр Title)
    @Column(name="IS_SUPPORT_LINK", length = 1, nullable = false)
    private String isSupport; //"Y" - данный предиктор используетс€ дл€ вспомогательных задач (не получение аспекта предсказани€)
    @Column(name="PREDICTOR_SERVICE_NAME", length=50, nullable = false)
    private String predictorServiceName; //»м€ PredictorService с которым работает предиктор
    @Column(name="WEIGHT", length=50, nullable = false)
    private int weight; //¬ес предиктора при вычислении ответа пользователю
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

    public boolean isSupportLink() {
        return this.isSupport.equals("Y");
    }

    public void setSupportLink(boolean isSupport) {
        this.isSupport = (isSupport) ? "Y" : "N";
    }

    public String getPredictorServiceName() {
        return predictorServiceName;
    }

    public void setPredictorServiceName(String predictorServiceName) {
        this.predictorServiceName = predictorServiceName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Predictor{" +
                "predictorId=" + predictorId +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", host='" + host + '\'' +
                ", checkData='" + checkData + '\'' +
                ", isSupport='" + isSupport + '\'' +
                ", predictorServiceName='" + predictorServiceName + '\'' +
                ", weight=" + weight +
                ", values=" + Arrays.toString(values.toArray()) +
                '}';
    }
}
