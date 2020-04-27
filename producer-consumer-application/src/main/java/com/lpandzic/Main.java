package com.lpandzic;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.time.Duration;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(20).toMillis()); // waiting for topics
        new SpringApplicationBuilder(Main.class).run(args);
    }
}
