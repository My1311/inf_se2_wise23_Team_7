package de.hbrs.se2.model;

import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.Encryption;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class BaseEntityTest {

    @Test
    void testEquals() {
        BaseEntity entity1 = new BaseEntityImpl();
        BaseEntity entity2 = new BaseEntityImpl();

        // Zwei verschiedene Instanzen mit unterschiedlichen IDs sollten nicht gleich sein
        assertNotEquals(entity1, entity2);

        // Zwei Instanzen mit der gleichen ID sollten gleich sein
        entity2.setId(entity1.getId());
        assertEquals(entity1, entity2);
    }

    @Test
    void testSetId() {
        BaseEntity entity = new BaseEntityImpl();
        UUID newId = UUID.randomUUID();

        // Setze die ID auf einen neuen Wert
        entity.setId(newId);

        // Überprüfe, ob die ID korrekt gesetzt wurde
        assertEquals(newId, entity.getId());
    }

    // Eine konkrete Implementierung für Tests
    private static class BaseEntityImpl extends BaseEntity {
    }
}