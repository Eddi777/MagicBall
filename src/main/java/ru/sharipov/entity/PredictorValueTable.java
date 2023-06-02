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
import ru.sharipov.predictor.Predictor;

import java.time.LocalDate;

@Entity
@Table(name = "PREDICTOR_VALUES")
public class PredictorValueTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VALUE_ID")
    private Long valueId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name", nullable = false)
    private PredictorsTable predictor;
    @Column(name = "BIRTH_FROM")
    private LocalDate birthFrom;
    @Column(name = "BIRTH_TO")
    private LocalDate birthTo;
    private String name; //Знак по гороскопу
    @Column(name="URL", nullable = false)
    private String url;
    @Column(name="CHECK_DATA", nullable = true)
    private String checkData; //Контрольная строка источника (напр Title)
    private String regex; //Регулярное выражение для получения нужных данных
}
