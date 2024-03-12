package hexlet.code.specifications;

import hexlet.code.dto.users.UserParamsDTO;
import hexlet.code.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserSpecification {
    public Specification<User> build(UserParamsDTO params) {
        return withFirstNameCont(params.getFirstNameCont())
                .and(withLastNameCont(params.getLastNameCont()))
                .and(withCreatedAtGt(params.getCreatedAtGt()))
                .and(withCreatedAtLt(params.getCreatedAtLt()));
    }

    private Specification<User> withFirstNameCont(String substring) {
        return (root, query, cb) -> substring == null ? cb.conjunction() : cb.like(root.get("firstName"), substring);
    }

    private Specification<User> withLastNameCont(String substring) {
        return (root, query, cb) -> substring == null ? cb.conjunction() : cb.like(root.get("lastName"), substring);
    }

    private Specification<User> withCreatedAtGt(LocalDate date) {
        return (root, query, cb) -> date == null ? cb.conjunction() : cb.greaterThan(root.get("createdAt"), date);
    }

    private Specification<User> withCreatedAtLt(LocalDate date) {
        return (root, query, cb) -> date == null ? cb.conjunction() : cb.lessThan(root.get("createdAt"), date);
    }
}
