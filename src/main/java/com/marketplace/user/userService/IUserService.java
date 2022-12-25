package com.marketplace.user.userService;

import com.marketplace.user.User;

import java.security.Principal;
import java.util.Optional;

public interface IUserService {
    void addNewUser(User user);
    void deleteUser(String id, Principal principal);
    User getUser(String id, Principal principal);
    void updateUser(String username, User user, Principal principal);
}
