package be.hubertrm.cashflow.infra.entity;

import be.hubertrm.cashflow.domain.model.Category;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "creation_date", nullable = false)
    private LocalDate date;

    public static CategoryEntity from(Category category) {
        return new CategoryEntity(
                category.getId(),
                category.getName(),
                category.getDate()
        );
    }

    public Category fromThis() {
        return new Category(id, name, date);
    }

}
