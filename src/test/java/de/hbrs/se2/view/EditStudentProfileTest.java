package de.hbrs.se2.view;

import static org.mockito.Mockito.*;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.SessionAttributes;
import de.hbrs.se2.views.routes.profile.EditStudentProfile;
import lombok.RequiredArgsConstructor;
import org.junit.Before;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class EditStudentProfileTest {
    @Mock
    private StudentService studentService;
    @Mock
    private LoginService userService;
    @Mock
    private UI ui;
    @Mock
    private Page page;
    @Mock
    private SessionAttributes sessionAttributes;
    @InjectMocks
    private EditStudentProfile editStudentProfile;

    private User mockUser;
    private Student mockStudent;
    private User currentUser;
    private Student currentStudent;

    @Before
    public void setUp() {
        mockUser = new User();
        mockStudent = new Student();

        when(sessionAttributes.getCurrentUser()).thenReturn(mockUser);
        when(studentService.findStudentByUser(mockUser)).thenReturn(mockStudent);

        // Mocking UI.getCurrent().getPage()
        when(UI.getCurrent()).thenReturn(ui);
        when(ui.getPage()).thenReturn(page);
    }

//    @Test
//    public void testInitPageWithCurrentUser() {
//        this.currentUser = SessionAttributes.getCurrentUser();
//        // Test the case when there is a logged-in user
//        editStudentProfile.initPage();
//        assertNotNull(SessionAttributes.getCurrentUser());
//        assertNotNull(studentService.findStudentByUser(this.currentUser));
//    }
//
//    @Test
//    public void testInitPageWithoutCurrentUser() {
//        // Test the case when there is not a logged-in user
//        when(sessionAttributes.getCurrentUser()).thenReturn(null);
//        editStudentProfile.initPage();
//        verify(ui.getPage()).setLocation(Constant.Value.Route.LOGIN);
//        verify(Notification).show(eq("Please Login!"), eq(5), eq(Notification.Position.TOP_CENTER));
//    }

//    @Test
//    public void testSaveListener() {
//        // Mocking the requiredFieldsAreFilled() method to always return true for simplicity
//        when(editStudentProfile.requiredFieldsAreFilled()).thenReturn(true);

        // Testing the saveListener method
//        Button.ClickEvent<Button> mockEvent = mock(Button.ClickEvent.class);
//        editStudentProfile.saveListener(mockEvent);
//
//        // Verify that the appropriate methods are called based on the currentStudent being null or not
//        if (editStudentProfile.getCurrentStudent() != null) {
//            verify(editStudentProfile.getCurrentStudent(), times(1)).setDegree(any());
//            verify(editStudentProfile.getCurrentStudent(), times(1)).setFirst_name(any());
//            verify(editStudentProfile.getCurrentStudent(), times(1)).setLast_name(any());
//            verify(editStudentProfile.getCurrentStudent(), times(1)).setMajor_study(any());
//            verify(editStudentProfile.getCurrentStudent(), times(1)).setList_of_skills(any());
//            verify(studentService, times(1)).addStudent(any());
//            verify(Notification, times(1)).show(eq("Student updated"));
//        } else {
//            verify(userService, times(1)).addUser(any());
//            verify(studentService, times(1)).addStudent(any());
//            verify(Notification, times(1)).show(eq("Student added"));
//        }
    }

 //   @Test
//    public void testRequiredFieldsAreFilled() {
//        // Test the case when required fields are filled
//        mockStudent.setFirst_name("John");
//        mockStudent.setLast_name("Doe");
//        mockStudent.setMajor_study("Computer Science");
//
//        assertTrue(editStudentProfile.requiredFieldsAreFilled();
//
//        // Test the case when a required field is empty
//        mockStudent.setFirst_name("");
//        assertFalse(editStudentProfile.requiredFieldsAreFilled());
//    }
//
//}