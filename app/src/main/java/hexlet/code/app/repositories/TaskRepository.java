package hexlet.code.app.repositories;

import hexlet.code.app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByName(String name);
}
