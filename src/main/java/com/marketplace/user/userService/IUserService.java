package com.marketplace.user.userService;

import com.marketplace.user.User;

import java.util.Optional;

public interface IUserService {
    void addNewUser(User user);
    void deleteUser(String id);
    User getUser(String id);
    void updateUser(String email, User user);
}
