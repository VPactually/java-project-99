package hexlet.code.component;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.services.LabelService;
import hexlet.code.services.TaskStatusService;
import hexlet.code.services.UserService;
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

    @Autowired
    private LabelService labelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        var admin = new UserCreateDTO("Admin", "Admin", "hexlet@example.com", "qwerty");
        userService.create(admin);

        var user = new UserCreateDTO("user", "user", "user@gmail.com", "password");
        userService.create(user);

        taskStatusService.create(new TaskStatusCreateDTO("Draft", "draft"));
        taskStatusService.create(new TaskStatusCreateDTO("ToReview", "to_review"));
        taskStatusService.create(new TaskStatusCreateDTO("ToBeFixed", "to_be_fixed"));
        taskStatusService.create(new TaskStatusCreateDTO("ToPublish", "to_publish"));
        taskStatusService.create(new TaskStatusCreateDTO("Published", "published"));

        labelService.create(new LabelCreateDTO("bug"));
        labelService.create(new LabelCreateDTO("feature"));

    }
}
