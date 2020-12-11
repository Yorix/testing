package com.yorix.testing.controller;

import com.yorix.testing.model.Question;
import com.yorix.testing.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MainController {
    private final QuestionService questionService;
    private List<Question> questions;

    public MainController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/testing/{num}")
    public ModelAndView question(@PathVariable int num) {
        Question question = questions.get(num - 1);
        ModelAndView modelAndView = new ModelAndView("test");
        int trueAnswers = (int) questions.stream()
                .filter(q -> q.getUserAnswer() == q.getTrueAnswer())
                .count();
        int falseAnswers = (int) questions.stream()
                .filter(q -> q.getUserAnswer() != q.getTrueAnswer() && q.getUserAnswer() != 0)
                .count();
        modelAndView.addObject("ques", question);
        modelAndView.addObject("index", num);
        modelAndView.addObject("questionsSize", questions.size());
        modelAndView.addObject("trueAnswers", trueAnswers);
        modelAndView.addObject("falseAnswers", falseAnswers);
        return modelAndView;
    }

    @PostMapping("/testing")
    public String setQuestionsNumber(int num) {
        questions = questionService.readRandom(num);
        return "redirect:/testing/1";
    }

    @PostMapping("/testing/{num}")
    public String answer(
            @PathVariable int num,
            @RequestParam char answer,
            @RequestParam String url
    ) {
        Question question = questions.get(num - 1);
        question.setUserAnswer(answer - 1039);
        questionService.update(question);
        return "redirect:".concat(url);
    }
}
