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
    private Long valueId; //�������������
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PREDICTOR_ID", nullable = false)
    private Predictor predictor; //��������� �� ���������
    @Column(name="ENDPOINT", length = 150)
    private String endpoint; //�������� (������ url) ��� ��������������� ������� (��� �������������)
    @Column(name="REGEX", nullable = false, length = 150)
    private String regex; //���������� ��������� ��� ��������� ������ ������
    @Column(name="TAGS", nullable = false, length = 400)
    private String tags; //���� ��� ������������ (����������� - �������)

    @Column(name="SERVICE", length = 400)
    private String service; //��������������� ��������� ����

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "PredictorValue{" +
                "valueId=" + valueId +
                ", endpoint='" + endpoint + '\'' +
                ", regex='" + regex + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
