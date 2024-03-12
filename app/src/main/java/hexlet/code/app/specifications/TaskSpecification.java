package hexlet.code.app.specifications;

import hexlet.code.app.dto.tasks.TaskParamsDTO;
import hexlet.code.app.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withStatus(params.getStatus()))
                .and(withAssignee(params.getAssigneeId()))
                .and(withLabel(params.getLabelId()));
    }

    private Specification<Task> withTitleCont(String substring) {
        return (root, query, cb) -> substring == null ? cb.conjunction()
                : cb.like(root.get("name"), "%" + substring + "%");
    }

    private Specification<Task> withStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction()
                : cb.equal(root.join("taskStatus").get("slug"), status);
    }

    private Specification<Task> withAssignee(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null ? cb.conjunction()
                : cb.equal(root.join("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withLabel(Long labelId) {
        return (root, query, cb) -> labelId == null ? cb.conjunction()
                : cb.equal(root.join("labels").get("id"), labelId);
    }
}
