package com.yorix.testing.service;

import com.yorix.testing.model.Answer;
import com.yorix.testing.storage.AnswerRepository;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void create(Answer answer) {
        answerRepository.save(answer);
    }
}
