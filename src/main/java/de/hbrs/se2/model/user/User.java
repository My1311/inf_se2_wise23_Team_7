package de.hbrs.se2.model.user;

import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.util.Encryption;
import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Builder
@ToString
@Entity
@Table(name = "user", schema = "public")
public class User extends BaseEntity {

    @Setter
    @Builder.Default
    @NotNull
    @Column(name = "email", nullable = false, unique = true) //unique prevents multiple user having the same email
    private String email = "";

    @Setter
    @NotNull
    @Builder.Default
    @Column(name = "password", nullable = false)
    private String password = "";

    @Builder.Default
    @Column(name = "securityQuestion", nullable = true)
    private String securityQuestion = "";

    @Builder.Default
    @Column(name = "securityAnswer", nullable = true)
    private String securityAnswer = "";

   /* @NotNull
    @Builder.Default
    @Column(name = "role", nullable = false)
    private String role = "";*/


    public User() {
    }

    public User(String email, String password/*, String role*/) {
        this.email = email.toLowerCase(); // standardizes the eamil to prevent multiple emails, which are the same but have uppercase or lowercase, which makes them different
        this.password = Encryption.sha256(password);
        /*this.role = role;*/
    }

    public User(String email, String password, String securityQuestion, String securityAnswer) {
        this.email = email.toLowerCase();
        this.password = Encryption.sha256(password);
        if (!securityQuestion.isEmpty() && !securityAnswer.isEmpty()) {
            this.securityQuestion = securityQuestion.trim(); // if accidentally spaces are before and after the string
            this.securityAnswer = securityAnswer.trim();
        }
    }

    public boolean checkEmail(String email) {
        return this.email.equals(email);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * @return the hashed password
     */
    @Override
    public int hashCode() {
        return password.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null) return false;
        return getPassword() != null ? getPassword().equals(user.getPassword()) : user.getPassword() == null;
    }
}
