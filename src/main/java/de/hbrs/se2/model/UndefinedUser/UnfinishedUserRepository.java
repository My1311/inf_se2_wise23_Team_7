package de.hbrs.se2.model.UndefinedUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface UnfinishedUserRepository extends JpaRepository<UnfinishedUser, UUID> {

    //looks if a  user has a unfinished profile
    //@Query("SELECT CASE WHEN COUNT(unfinished) > 0 THEN TRUE ELSE FALSE END FROM UnfinishedUser unfinished, User u WHERE u.email = :email AND unfinished.user.id = u.id")
    //boolean hasUnfinishedUser(@Param("email") String email);


    @Query("SELECT unfinished from UnfinishedUser  unfinished, User u  WHERE u.email = :email AND  unfinished.user.id = u.id")
    Optional<UnfinishedUser> getUnfinishedUser(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM UnfinishedUser u WHERE u.user IN (SELECT user FROM User user WHERE user.email = :email)")
    void deleteUnfinishedUserByEmail(@Param("email") String email);


}
