package com.marketplace.user.validator;

import java.time.LocalDate;

public interface IValidatorService {
    boolean emailIsValid(String email);
    boolean phoneNumberIsValid(String phoneNumber);
    boolean ageIsValid(LocalDate dob);
}
