package de.hbrs.se2.util;

import com.vaadin.flow.component.UI;
import de.hbrs.se2.model.user.User;

import javax.annotation.Nullable;

public class SessionAttributes {
    public static final String ADVERTISEMENT = "advertisement";
    public static final String STUDENT = "student";
    public static final String APPLY = "apply";

    public static @Nullable User getCurrentUser() {
        try {
            return (User) UI.getCurrent().getSession().getAttribute(Constant.Value.CURRENT_USER);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static void setCurrentUser(User user) {
        UI.getCurrent().getSession().setAttribute(Constant.Value.CURRENT_USER, user);
    }
}
