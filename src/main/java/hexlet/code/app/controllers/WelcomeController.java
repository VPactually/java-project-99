package hexlet.code.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomeController {

    @GetMapping("welcome")
    String home() {
        return "Welcome to Spring";
    }

    @GetMapping("exception")
    public String trigger() {
        throw new RuntimeException("Exception was triggered");
    }

}