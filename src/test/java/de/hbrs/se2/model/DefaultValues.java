package de.hbrs.se2.model;

import de.hbrs.se2.model.UndefinedUser.UnfinishedUser;
import de.hbrs.se2.model.apply.ApplicationState;
import de.hbrs.se2.model.apply.Apply;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.rating.Rating;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.Encryption;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public final class DefaultValues {

    public static final User DEFAULT_USER_STUDENT;
    public static final User DEFAULT_USER_COMPANY;
    public static final Student DEFAULT_STUDENT;
    public static final Company DEFAULT_COMPANY;
    public static final Advertisement DEFAULT_ADVERTISEMENT;

    public static final UnfinishedUser DEFAULT_UNFINISHED_USER;

    public static final Apply DEFAULT_APPLY;
    public static final Rating DEFAULT_RATING;
    // Zum Testen: Wir geben jedes Entity eine fixed ID, sonst haben wir immer andere IDs fur gleiche Objekte

    static {
        DEFAULT_USER_COMPANY = User.builder()
                .email("company@gmail.com")
                .password(Encryption.sha256("company")).build();
        DEFAULT_USER_COMPANY.setId(UUID.fromString("00000000-2f1b-4199-87d0-fd8b5242e901"));
        DEFAULT_USER_STUDENT = User.builder()
                .email("student@gmail.com")
                .password(Encryption.sha256("student")).build();
        DEFAULT_USER_STUDENT.setId(UUID.fromString("00000000-2f1b-4199-87d0-fd8b5242e902"));
        DEFAULT_STUDENT = Student.builder()
                .first_name("student")
                .last_name("student")
                .major_study("student")
                .degree("student")
                .list_of_skills("student")
                .user(DEFAULT_USER_STUDENT).build();
        DEFAULT_STUDENT.setId(UUID.fromString("00000000-2f1b-4199-87d0-fd8b5242e903"));
        DEFAULT_COMPANY = Company.builder()
                .logo(new byte[32])
                .description("Company")
                .phoneNumber("+4910010010")
                .industry("Company")
                .user(DEFAULT_USER_COMPANY)
                .name("Company").build();
        DEFAULT_COMPANY.setId(UUID.fromString("00000000-2f1b-4199-87d0-fd8b5242e904"));
        DEFAULT_ADVERTISEMENT = Advertisement.builder()
                .deadline(LocalDate.now())
                .description("Advertisement")
                //.jobType("Advertisement")
                .name("Advertisement")
                .ort("KoelnNichtBonn")
                .hourlyWage(14.00)
                .isActive(true)
                .requirements("IT, Java")
                .company(DEFAULT_COMPANY).build();
        // DEFAULT_COMPANY.setAdvertisements(new ArrayList<>(){{add(DEFAULT_ADVERTISEMENT);}});
        DEFAULT_ADVERTISEMENT.setId(UUID.fromString("00000000-6f3a-43df-80ee-873525a3c5d4"));
        DEFAULT_UNFINISHED_USER = UnfinishedUser.builder()
                .user(DEFAULT_USER_COMPANY)
                .userType("company").build();
        DEFAULT_UNFINISHED_USER.setId(UUID.fromString("00000000-2f1b-4199-87d0-fd8b5242e905"));
        DEFAULT_APPLY= Apply.builder()
                .text("I want to apply for this job.")
                .state(ApplicationState.PENDING)
                .applied_time(Instant.now())
                .advertisement(DEFAULT_ADVERTISEMENT)
                .student(DEFAULT_STUDENT)
                .build();
        DEFAULT_APPLY.setId(UUID.fromString("00000000-6f3a-43df-80ee-873525a3c5d5"));
        DEFAULT_RATING = Rating.builder()
                .company(DEFAULT_COMPANY)
                .student(DEFAULT_STUDENT)
                .text("I like this company.")
                .value(5)
                .timestamp(Instant.now())
                .build();
        DEFAULT_RATING.setId(UUID.fromString("00000000-6f3a-43df-80ee-873525a3c5d6"));
    }

}