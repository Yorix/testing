package com.yorix.testing.config;

import com.yorix.testing.service.TextParser;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Initialisation {
    private final TextParser textParser;

    public Initialisation(TextParser textParser) {
        this.textParser = textParser;
    }

    @PostConstruct
    public void init() {
        textParser.parse();
    }
}
