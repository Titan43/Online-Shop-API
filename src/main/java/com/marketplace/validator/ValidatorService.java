package com.marketplace.validator;

import com.marketplace.constants.IAPIConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Service
@Qualifier("firstImplementation")
public class ValidatorService implements IValidatorService{

    private Pattern emailPattern = null;
    private Pattern usernamePattern = null;
    private Pattern phoneNumberPattern = null;
    private Pattern idPattern = null;

    public Pattern getEmailPattern() {
        if(emailPattern == null){
            emailPattern = Pattern.compile(IAPIConstants.EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        }

        return emailPattern;
    }

    public Pattern getUsernamePattern() {
        if(usernamePattern == null){
            usernamePattern = Pattern.compile(IAPIConstants.USERNAME_REGEX, Pattern.CASE_INSENSITIVE);
        }

        return usernamePattern;
    }

    public Pattern getPhoneNumberPattern() {
        if(phoneNumberPattern == null){
            phoneNumberPattern = Pattern.compile(IAPIConstants.PHONE_NUMBER_REGEX, Pattern.CASE_INSENSITIVE);
        }

        return phoneNumberPattern;
    }

    public Pattern getIdPattern() {
        if(idPattern == null){
            idPattern = Pattern.compile(IAPIConstants.ID_REGEX);
        }

        return idPattern;
    }

    @Override
    public boolean emailIsNotValid(String email){
        if(email != null){
            if(!email.strip().equals("")){
                return !getEmailPattern().matcher(email).matches();
            }
        }
        return true;
    }

    @Override
    public boolean usernameIsNotValid(String username) {
        if(username != null){
            if(!username.strip().equals("")){
                return !getUsernamePattern().matcher(username).matches();
            }
        }
        return true;
    }

    @Override
    public boolean phoneNumberIsNotValid(String phoneNumber) {
        if(phoneNumber != null){
            if(!phoneNumber.strip().equals("")){
                return !getPhoneNumberPattern().matcher(phoneNumber).matches();
            }
        }
        return true;
    }

    @Override
    public boolean ageIsNotValid(LocalDate dob) {
        if(dob != null){
            return Period.between(dob, LocalDate.now()).getYears() < IAPIConstants.VALID_AGE;
        }
        return true;
    }

    @Override
    public boolean idIsValid(String id) {
        if(id != null){
            if(!id.strip().equals("")){
                return getIdPattern().matcher(id).matches();
            }
        }
        return false;
    }
}
