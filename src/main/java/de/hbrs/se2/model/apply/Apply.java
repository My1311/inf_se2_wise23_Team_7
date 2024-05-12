package de.hbrs.se2.model.apply;

import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.student.Student;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import javax.persistence.*;
import java.time.Instant;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apply", schema = "public")
public class Apply extends BaseEntity {
    @NotNull
    @Builder.Default
    @Column(name = "text", length = 10000)
    private String text = "";


    @NotNull
    @Builder.Default
    @Column(name = "state")
    private ApplicationState state = ApplicationState.PENDING;

    @NotNull
    @Builder.Default
    @Column(name = "appliedTime")
    private Instant applied_time = Instant.now();

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "student",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_student_id")
    )
    private Student student;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "advertisement",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_advertisement_id")
    )
    private Advertisement advertisement;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Apply apply = (Apply) o;

        if (getText() != null ? !getText().equals(apply.getText()) : apply.getText() != null) return false;
        if (!getApplied_time().equals(apply.getApplied_time())) return false;
        if (!getStudent().equals(apply.getStudent())) return false;
        return getAdvertisement().equals(apply.getAdvertisement());
    }

    @Override
    public int hashCode() {
        int result = getText() != null ? getText().hashCode() : 0;
        result = 31 * result + getApplied_time().hashCode();
        result = 31 * result + getStudent().hashCode();
        result = 31 * result + getAdvertisement().hashCode();
        return result;
    }
}
