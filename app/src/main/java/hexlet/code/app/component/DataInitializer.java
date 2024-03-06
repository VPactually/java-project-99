package hexlet.code.app.component;

import hexlet.code.app.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.app.dto.users.UserCreateDTO;
import hexlet.code.app.services.TaskStatusService;
import hexlet.code.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskStatusService taskStatusService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        userService.create(new UserCreateDTO("Admin", "Admin", "hexlet@example.com", "qwerty"));

        for (int i = 1; i <= 5; i++) {
            var user = new UserCreateDTO();
            user.setEmail("vpactually" + i + "@gmail.com");
            user.setPassword("123123");
            user.setFirstName("Vladislav");
            user.setLastName("Pomozov");
            userService.create(user);
        }

        taskStatusService.create(new TaskStatusCreateDTO("Draft", "draft"));
        taskStatusService.create(new TaskStatusCreateDTO("ToReview", "to_review"));
        taskStatusService.create(new TaskStatusCreateDTO("ToBeFixed", "to_be_fixed"));
        taskStatusService.create(new TaskStatusCreateDTO("ToPublish", "to_publish"));
        taskStatusService.create(new TaskStatusCreateDTO("Published", "published"));
    }
}
