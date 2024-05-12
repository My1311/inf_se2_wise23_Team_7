package de.hbrs.se2.model.UndefinedUser;

import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "unfinished_user", schema = "public")
public class UnfinishedUser extends BaseEntity {
    @NotNull
    @Column(name = "userType", nullable = false) //unique prevents multiple user having the same email
    String userType;
    @OneToOne(optional = false)// User gets deleted, that Entity too and it is not nullable
    @JoinColumn(
            name = "fk_user", //creates the foreign key in Company with "user_id" as name. it is not bound to the actul name of the pimary key of User
            //referencedColumnName = "id", // the name of the referenced column for User in the DB
            foreignKey = @ForeignKey(name = "fk_from_unfinished_user_to_user")// sets the name of the foreign key constraint in the db
    )
    private User user;
}
