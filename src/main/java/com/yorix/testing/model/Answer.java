package com.yorix.testing.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Lob
    private String text;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
