package de.hbrs.se2.control.UnfinshedUserService;

import de.hbrs.se2.model.UndefinedUser.UnfinishedUser;
import de.hbrs.se2.model.UndefinedUser.UnfinishedUserRepository;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UnfinishedUserService {

    @Autowired
    UnfinishedUserRepository unfinishedUserRepository;
    @Autowired
    UserRepository userRepository;

    //returns ture if a user is undefined
    public boolean checkIfUserIsUnfinishedUser(String email) {
        return unfinishedUserRepository.getUnfinishedUser(email).isPresent();
    }

    public UnfinishedUser getUnfineshedUser(String email) {
        Optional<UnfinishedUser> undefiendUserOptoinal = unfinishedUserRepository.getUnfinishedUser(email);
        if (undefiendUserOptoinal.isPresent()) {
            return undefiendUserOptoinal.get();
        }
        throw new NoSuchElementException("There is no UndefiendUser");
    }

    @Transactional
    public void createUnfinishedUser(String userEmail, String userType) {
        User user = userRepository.getByEmail(userEmail);
        UnfinishedUser unfinishedUser = new UnfinishedUser(userType, user);
        unfinishedUserRepository.save(unfinishedUser);

    }

    @Transactional
    public void deleteUnfinishedUser(String email) {
        //UnfinishedUser unfinishedUser = getUnfineshedUser(email);
        //unfinishedUserRepository.delete(unfinishedUser);
        unfinishedUserRepository.deleteUnfinishedUserByEmail(email);
    }
}
