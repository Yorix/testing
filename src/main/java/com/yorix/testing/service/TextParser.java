package com.yorix.testing.service;

import com.yorix.testing.model.Answer;
import com.yorix.testing.model.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;

@Service
public class TextParser {
    @Value("/static/tests.txt")
    private ClassPathResource resource;

    private final QuestionService questionService;
    private final AnswerService answerService;

    public TextParser(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    private String rawText;

    public void parse() {
        try {
            rawText = Files.lines(resource.getFile().toPath(), StandardCharsets.UTF_8).collect(Collectors.joining("\r\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] lines = rawText.split("(?=\r\n\\*?\\d{1,3}\\.\\s)");

        Question question = new Question();
        Answer answer;

        for (int i = 1; i < lines.length; i++) {
            if ((i - 1) % 5 == 0) {
                question = new Question();
                question.setText(lines[i].concat("\r\n"));
                questionService.create(question);
            } else {
                answer = new Answer();
                answer.setQuestion(question);
                if (lines[i].charAt(2) == '*') {
                    answer.setTrue_ans(true);
                    lines[i] = "\r\n".concat(lines[i].substring(3));
                }
                answer.setText(lines[i].concat("\r\n"));
                answerService.create(answer);
            }
        }
    }
}
