package com.marketplace.user.userService;

import com.marketplace.user.User;
import com.marketplace.user.UserRepository;
import com.marketplace.user.UserRole;
import com.marketplace.user.validator.IValidatorConstants;
import com.marketplace.user.validator.IValidatorService;
import com.marketplace.user.validator.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Qualifier("firstImplementation")
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final IValidatorService validatorService;

    @Autowired
    public UserService(UserRepository userRepository, ValidatorService validatorService) {
        this.userRepository = userRepository;
        this.validatorService = validatorService;
    }

    @Override
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @Override
    public void addNewUser(User user) {

        if(!validatorService.emailIsValid(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email (CODE 400)");

        else if(!validatorService.phoneNumberIsValid(user.getPhoneNumber()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number (CODE 400)");

        else if (userRepository.findUserByEmail(user.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with such email already exists (CODE 400)");

        else if(!validatorService.ageIsValid(user.getDob()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid date of birth entered. You should be at least " +
                    IValidatorConstants.VALID_AGE + " years old to use the marketplace (CODE 400)");

        else if(user.getRole() == null){
            user.setRole(UserRole.BUYER);
        }

        userRepository.save(user);
    }
}
