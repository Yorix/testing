package com.yorix.testing.service;

import com.yorix.testing.model.Question;
import com.yorix.testing.storage.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> read() {
        return questionRepository.findAll();
    }

    public Question read(int id) {
        return questionRepository.getOne(id);
    }

    public void create(Question question) {
        questionRepository.save(question);
    }

    public void update(Question question) {
        create(question);
    }
}
