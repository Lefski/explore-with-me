package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@ComponentScan("ru.practicum.emw.client") //todo check if works
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
