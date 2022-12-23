package com.marketplace.user.userService;

import com.marketplace.user.User;

import java.util.List;

public interface IUserService {
    List<User> getUsers();
    void addNewUser(User user);
}
