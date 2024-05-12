package de.hbrs.se2.model.rating;

import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.student.Student;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "rating", schema = "public")
public class Rating extends BaseEntity {
    @NotNull
    @Builder.Default
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp = Instant.now();

    @Builder.Default
    @Column(name = "value", nullable = false)
    private int value = 0;

    @Nullable
    @Builder.Default
    @Column(name = "text", length = 1000)
    private String text = "";

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "student",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_rating_student"))
    private Student student;
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "company",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_rated_company"))
    private Company company;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Rating rating = (Rating) o;
        return value == rating.value && Objects.equals(timestamp, rating.timestamp) && Objects.equals(student, rating.student) && Objects.equals(company, rating.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, value, student, company);
    }
}
