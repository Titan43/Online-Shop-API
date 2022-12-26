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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> addNewUser(User user) {
        if(validatorService.usernameIsNotValid(user.getUsername())){
            return new ResponseEntity<>("Invalid username (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(validatorService.emailIsNotValid(user.getEmail())) {
            return new ResponseEntity<>("Invalid email (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(validatorService.phoneNumberIsNotValid(user.getPhoneNumber())) {
            return new ResponseEntity<>("Invalid phone number (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>("User with such username already exists (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>( "User with such email already exists (CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(validatorService.ageIsNotValid(user.getDob())) {
            return new ResponseEntity<>( "Invalid date of birth entered. You should be at least " +
                    IAPIConstants.VALID_AGE + " years old to use the marketplace (CODE 400)", HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>("User was successfully created(CODE 201)",HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteUser(String username, Principal principal) {
        if(!principal.getName().equals(username))
            return new ResponseEntity<>("Yor token is not valid for this user(CODE 403)", HttpStatus.FORBIDDEN);
        else if(validatorService.usernameIsNotValid(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username has to be provided (CODE 400)");
        }
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Such user does not exist(CODE 404)");

        userRepository.deleteById(user.get().getId());
        return new ResponseEntity<>("User deleted successfully(CODE 200)", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUser(String username, Principal principal) {
        if(!principal.getName().equals(username))
            return new ResponseEntity<>("Yor token is not valid for this user(CODE 403)", HttpStatus.FORBIDDEN);
        else if(validatorService.usernameIsNotValid(username)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateUser(String username, User user, Principal principal) {
        if(!principal.getName().equals(username)) {
            return new ResponseEntity<>("Yor token is not valid for this user(CODE 403)", HttpStatus.FORBIDDEN);
        }
        else if(user.toString().equals(new User().toString())) {
            return new ResponseEntity<>( "No data to update or wrong fields passed(CODE 400)",HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<?> oldUser = getUser(username, principal);
        if(!oldUser.hasBody() || oldUser.getBody() ==null)
            return new ResponseEntity<>( "Such user does not exist(CODE 404)", HttpStatus.NOT_FOUND);
        User oldUserData = (User) oldUser.getBody();
        if(user.getId() != null){
            return new ResponseEntity<>("UserId cannot be changed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(user.getUsername()!=null){
            return new ResponseEntity<>("Username cannot be changed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(user.getEmail()!=null){
            return new ResponseEntity<>( "User email cannot be changed(CODE 400)", HttpStatus.BAD_REQUEST);
        }
        else if(user.getRole() != null){
            return new ResponseEntity<>("User role cannot be changed(CODE 400)", HttpStatus.BAD_REQUEST);
        }

        if(user.getDob() != null){
            if(validatorService.ageIsNotValid(user.getDob()))
                return new ResponseEntity<>( "Invalid date of birth entered. You should be at least " +
                        IAPIConstants.VALID_AGE + " years old to use the marketplace (CODE 400)", HttpStatus.BAD_REQUEST);
            oldUserData.setDob(user.getDob());
        }
        if(user.getPhoneNumber()!= null){
            if(validatorService.phoneNumberIsNotValid(user.getPhoneNumber()))
                return new ResponseEntity<>("Invalid phone number (CODE 400)", HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>("User updated successfully(CODE 200)", HttpStatus.OK);
    }
}
