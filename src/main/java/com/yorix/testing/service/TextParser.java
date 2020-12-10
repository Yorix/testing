package com.yorix.testing.service;

import com.yorix.testing.model.Answer;
import com.yorix.testing.model.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class TextParser {
    @Value("static/tests.txt")
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
            byte[] data = FileCopyUtils.copyToByteArray(resource.getInputStream());
            rawText = new String(data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] lines = rawText.split("\r\n(?=\\*?(\\d{1,3}\\.|[АБВГ]\\))\\s)");

        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        Question question = new Question();
        Answer answer;

        for (String line : lines) {
            line = line.trim().replaceAll("\r\n.*$", "");
            if (line.matches("^\\d{1,3}\\.\\s.+")) {
                question = new Question();
                question.setText(line);
                questions.add(question);
            } else if (line.matches("^\\*?[АБВГ]\\)\\s.+")) {
                answer = new Answer();
                answer.setQuestion(question);
                if (line.charAt(0) == '*') {
                    line = line.substring(1);
                    int trueAnswerNumber = line.charAt(0) - 1039;
                    question.setTrueAnswer(trueAnswerNumber);
                }
                answer.setText(line);
                answers.add(answer);
            }
        }

        questions.forEach(questionService::create);
        answers.forEach(answerService::create);
    }
}
