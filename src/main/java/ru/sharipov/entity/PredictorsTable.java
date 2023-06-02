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
public class PredictorsTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREDICTOR_ID")
    private long id; //���������� ����� ������
    private String name; //��� �������������
    private String comment; //�����������
    @Column(name = "HOST")
    private String host; //url ���������
    @OneToMany(mappedBy = "valueId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PredictorValueTable> values;


}
