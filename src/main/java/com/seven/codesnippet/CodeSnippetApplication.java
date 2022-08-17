package com.seven.codesnippet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CodeSnippetApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeSnippetApplication.class, args);
    }

}
