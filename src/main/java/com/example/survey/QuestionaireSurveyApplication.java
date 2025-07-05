package com.example.survey;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.survey.mapper")
public class QuestionaireSurveyApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionaireSurveyApplication.class, args);
    }

}
