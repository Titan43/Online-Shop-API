package com.marketplace.validator;

import java.time.LocalDate;

public interface IValidatorService {
    boolean emailIsNotValid(String email);
    boolean phoneNumberIsNotValid(String phoneNumber);
    boolean ageIsNotValid(LocalDate dob);
    boolean idIsValid(String id);
}
