package com.marketplace.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("firstImplementation")
public class UserService implements IUserService{

    @Override
    public List<User> getUsers(){
        return List.of(new User());
    }
}
