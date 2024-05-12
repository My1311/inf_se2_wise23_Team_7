package de.hbrs.se2.model.student;

import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.Encryption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    private Student student;
    private User user;
    @BeforeEach
    void SetUp(){
        user = User.builder().email("test@test.te").password(Encryption.sha256("test")).build();
        student = new Student("first_name", "last_name", "Java", "CS", "Bachelor", user);
    }
    @Test
    public void testEquals(){
        User user2 = User.builder().email("test23@test.te").password(Encryption.sha256("test23")).build();;
        Student student2 = new Student("first_name", "last_name", "Java", "CS", "Bachelor", user2);

        assertFalse(student.equals(student2));

        Student student3= new Student("first_name", "last_name", "Java", "CS", "Bachelor", user);
        assertTrue(student.equals(student3));
    }

    @Test
    public void testHashCode(){
        assertNotEquals(student.hashCode(),10);
    }

}