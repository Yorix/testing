package com.yorix.testing.controller;

import com.yorix.testing.model.Question;
import com.yorix.testing.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    private final QuestionService questionService;
    private final List<Question> questions;

    public MainController(QuestionService questionService) {
        this.questionService = questionService;
        questions = questionService.read();
    }

    @GetMapping
    public String index() {
        Collections.shuffle(questions);
        return "index";
    }

    @GetMapping("/testing/{qnum}")
    public ModelAndView question(@PathVariable int qnum) {
        ModelAndView modelAndView = new ModelAndView("question");
        modelAndView.addObject("question", questions.get(qnum));
        modelAndView.addObject("qnum", qnum);
        modelAndView.addObject("qlength", questions.size());
        return modelAndView;
    }
}
