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
import java.util.Collections;
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

        String[] lines = rawText.split("\r\n(?=\\*?\\d{1,3}\\.\\s)");

        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        Question question = new Question();
        Answer answer;

        for (int i = 1; i < lines.length; i++) {
            if ((i - 1) % 5 == 0) {
                question = new Question();
                question.setText(lines[i]);
                questions.add(question);
            } else {
                answer = new Answer();
                answer.setQuestion(question);
                if (lines[i].charAt(0) == '*') {
                    int trueAnswerNumber = Integer.parseInt(lines[i].substring(1, 2));
                    question.setTrueAnswer(trueAnswerNumber);
                }
                lines[i] = lines[i].replaceAll("^\\*?|Джерело:.*$", "");
                answer.setText(lines[i]);
                answers.add(answer);
            }
        }

        Collections.shuffle(questions);
        questions.forEach(questionService::create);
        answers.forEach(answerService::create);
    }
}
