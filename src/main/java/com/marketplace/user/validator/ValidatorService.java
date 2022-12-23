package com.marketplace.user.validator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

import static com.marketplace.user.validator.IValidatorConstants.*;

@Service
@Qualifier("firstImplementation")
public class ValidatorService implements IValidatorService{

    private Pattern emailPattern = null;
    private Pattern phoneNumberPattern = null;

    public Pattern getEmailPattern() {
        if(emailPattern == null){
            emailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        }

        return emailPattern;
    }

    public Pattern getPhoneNumberPattern() {
        if(phoneNumberPattern == null){
            phoneNumberPattern = Pattern.compile(PHONE_NUMBER_REGEX, Pattern.CASE_INSENSITIVE);
        }

        return phoneNumberPattern;
    }

    @Override
    public boolean emailIsValid(String email){
        if(email != null){
            if(!email.strip().equals("")){
                return getEmailPattern().matcher(email).matches();
            }
        }
        return false;
    }

    @Override
    public boolean phoneNumberIsValid(String phoneNumber) {
        if(phoneNumber != null){
            if(!phoneNumber.strip().equals("")){
                return getPhoneNumberPattern().matcher(phoneNumber).matches();
            }
        }
        return false;
    }

    @Override
    public boolean ageIsValid(LocalDate dob) {
        if(dob != null){
            return Period.between(dob, LocalDate.now()).getYears() >= VALID_AGE;
        }
        return false;
    }
}
