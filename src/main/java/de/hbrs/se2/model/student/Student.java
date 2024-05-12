package de.hbrs.se2.model.student;

import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.model.user.User;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Student", schema = "public")
public class Student extends BaseEntity {
    @Builder.Default
    @Column(name = "first_name", nullable = false)
    private String first_name = "";

    @Getter
    @Builder.Default
    @Column(name = "last_name", nullable = false)
    private String last_name = "";

    @Getter
    @Builder.Default
    @Column(name = "list_of_skills")
    private String list_of_skills = "";

    @Getter
    @Builder.Default
    @Column(name = "major_study", nullable = false)
    private String major_study = "";

    @Getter
    @Builder.Default
    @Column(name = "degree")
    private String degree = "";

    // Wenn Student geloescht wird, wird das entsprechende Location geloescht.


    @Getter
    @Builder.Default
    @NotNull
    @OneToOne(orphanRemoval = true, optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user = null;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Student student = (Student) o;

        //if (!Objects.equals(user, student.user)) return false;
        if (!Objects.equals(first_name, student.first_name)) return false;
        if (!Objects.equals(last_name, student.last_name)) return false;

        if (!Objects.equals(major_study, student.major_study)) return false;
        return Objects.equals(degree, student.degree);
    }

    @Override
    public int hashCode() {
        int result = getFirst_name() != null ? getFirst_name().hashCode() : 0;
        result = 31 * result + (getLast_name() != null ? getLast_name().hashCode() : 0);
        result = 31 * result + (getList_of_skills() != null ? getList_of_skills().hashCode() : 0);
        result = 31 * result + (getMajor_study() != null ? getMajor_study().hashCode() : 0);
        result = 31 * result + (getDegree() != null ? getDegree().hashCode() : 0);
        result = 31 * result + getUser().hashCode();
        return result;
    }
}