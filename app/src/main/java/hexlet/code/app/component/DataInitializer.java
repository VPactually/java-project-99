package hexlet.code.app.component;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new UserCreateDTO();
        userData.setEmail(email);
        userData.setPassword("qwerty");
        userData.setFirstName("Admin");
        userData.setLastName("adminov");
        userService.create(userData);

        for (int i = 1; i <= 6; i++) {
            var user = new UserCreateDTO();
            user.setEmail("vpactually" + i + "@gmail.com");
            user.setPassword("123123");
            user.setFirstName("Vladislav");
            user.setLastName("Pomozov");
            userService.create(user);
        }
    }
}
