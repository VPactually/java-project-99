package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "labels")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Label implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 1000)
    private String name;

    @CreatedDate
    private LocalDate createdAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Task> tasks = new HashSet<>();

    public Label(Long id) {
        this.id = id;
    }
}
