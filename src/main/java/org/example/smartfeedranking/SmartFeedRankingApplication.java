package org.example.smartfeedranking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartFeedRankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFeedRankingApplication.class, args);
    }

}
