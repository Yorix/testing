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

    public MainController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/testing/{id}")
    public ModelAndView question(@PathVariable int id) {
        Question question = questionService.read(id);
        ModelAndView modelAndView = new ModelAndView("test");
        List<Question> questions = questionService.read();
        long trueAnswers = questions.stream()
                .filter(q -> q.getTrueAnswer() == q.getUserAnswer())
                .count();
        modelAndView.addObject("ques", question);
        modelAndView.addObject("qlength", questions.size());
        modelAndView.addObject("trueAnswers", trueAnswers);
        return modelAndView;
    }

    @PostMapping("/testing/{id}")
    public String answer(
            @PathVariable int id,
            @RequestParam int answer,
            @RequestParam String url
    ) {
        Question question = questionService.read(id);
        question.setUserAnswer(answer);
        questionService.update(question);
        return "redirect:".concat(url);
    }
}
