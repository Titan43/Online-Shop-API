package com.marketplace.user.userService;

import com.marketplace.user.User;
import com.marketplace.user.UserRepository;
import com.marketplace.user.UserRole;
import com.marketplace.constants.IAPIConstants;
import com.marketplace.validator.IValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Service
@Qualifier("firstImplementation")
@AllArgsConstructor
public class UserService implements IUserService{

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final IValidatorService validatorService;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public void addNewUser(User user) {
        if(validatorService.usernameIsNotValid(user.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username (CODE 400)");
        }
        else if(validatorService.emailIsNotValid(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email (CODE 400)");
        }
        else if(validatorService.phoneNumberIsNotValid(user.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number (CODE 400)");
        }
        else if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with such username already exists (CODE 400)");
        }
        else if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with such email already exists (CODE 400)");
        }
        else if(validatorService.ageIsNotValid(user.getDob())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid date of birth entered. You should be at least " +
                            IAPIConstants.VALID_AGE + " years old to use the marketplace (CODE 400)");
        }
        else if(user.getRole() == null){
            user.setRole(UserRole.BUYER);
        }

        if(user.getFName()==null){
            user.setFName("");
        }
        if(user.getSName()==null){
            user.setSName("");
        }

        userRepository.save(user);
    }

    @Override
    public void deleteUser(String username, Principal principal) {
        if(!principal.getName().equals(username))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Yor token is not valid for this user(CODE 403)");
        else if(validatorService.usernameIsNotValid(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username has to be provided (CODE 400)");
        }
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Such user does not exist(CODE 404)");

        userRepository.deleteById(user.get().getId());
    }

    @Override
    public User getUser(String username, Principal principal) {
        if(!principal.getName().equals(username))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Yor token is not valid for this user(CODE 403)");
        else if(validatorService.usernameIsNotValid(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username has to be provided (CODE 400)");
        }
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Such user does not exist(CODE 404)");

        return user.get();
    }

    @Override
    public void updateUser(String username, User user, Principal principal) {
        if(!principal.getName().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Yor token is not valid for this user(CODE 403)");
        }
        else if(user.toString().equals(new User().toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No data to update or wrong fields passed(CODE 400)");
        }
        User oldUserData = getUser(username, principal);
        if(user.getId() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserId cannot be changed(CODE 400)");
        }
        else if(user.getUsername()!=null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be changed(CODE 400)");
        }
        else if(user.getEmail()!=null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User email cannot be changed(CODE 400)");
        }
        else if(user.getRole() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User role cannot be changed(CODE 400)");
        }

        if(user.getDob() != null){
            if(validatorService.ageIsNotValid(user.getDob()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid date of birth entered. You should be at least " +
                                IAPIConstants.VALID_AGE + " years old to use the marketplace (CODE 400)");
            oldUserData.setDob(user.getDob());
        }
        if(user.getPhoneNumber()!= null){
            if(validatorService.phoneNumberIsNotValid(user.getPhoneNumber()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number (CODE 400)");
            oldUserData.setPhoneNumber(user.getPhoneNumber());
        }
        if(user.getFName()!=null)
            oldUserData.setFName(user.getFName());
        if(user.getSName()!=null)
            oldUserData.setSName(user.getSName());
        if(user.getPassword()!=null){
            oldUserData.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(oldUserData);
    }
}
