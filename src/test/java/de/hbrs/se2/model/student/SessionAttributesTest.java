package de.hbrs.se2.model.student;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.util.SessionAttributes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionAttributesTest {
    @Mock
    private UI ui;

    @Mock
    private VaadinSession session;
    @Mock
    private Page page;

    @InjectMocks
    private SessionAttributes sessionAttributes;

    @Mock
    private User mockUser;

    @Before
    public void setUp() {
        // Mocking UI.getCurrent().getSession()
        when(UI.getCurrent()).thenReturn(ui);
        when(ui.getSession()).thenReturn(session);
    }

    @Test
    public void testGetCurrentUserWhenUserExists() {
        // Mocking UI.getCurrent().getSession().getAttribute()
        when(ui.getSession().getAttribute(Constant.Value.CURRENT_USER)).thenReturn(mockUser);

        User currentUser = SessionAttributes.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(mockUser, currentUser);
    }

    @Test
    public void testGetCurrentUserWhenUserDoesNotExist() {
        // Mocking UI.getCurrent().getSession().getAttribute() returning null
        when(ui.getSession().getAttribute(Constant.Value.CURRENT_USER)).thenReturn(null);

        User currentUser = SessionAttributes.getCurrentUser();

        assertNull(currentUser);
    }

    @Test
    public void testSetCurrentUser() {
        // Mocking UI.getCurrent().getSession().setAttribute()
        SessionAttributes.setCurrentUser(mockUser);

        // Verify that the method is called with the correct arguments
        verify(ui.getSession()).setAttribute(Constant.Value.CURRENT_USER, mockUser);
    }
}