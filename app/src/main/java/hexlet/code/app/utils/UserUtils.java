package hexlet.code.app.utils;

import hexlet.code.app.model.User;
import hexlet.code.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        var email = authentication.getName();
        return userRepository.findByEmail(email).get();
    }


    public boolean checkUserPermission(Long id) {
        var userEmail = userRepository.findById(id).get().getEmail();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return userEmail.equals(authentication.getName());
    }

    public boolean checkUserPermission() {
        return getCurrentUser() != null && getCurrentUser().isEnabled();
    }

}
