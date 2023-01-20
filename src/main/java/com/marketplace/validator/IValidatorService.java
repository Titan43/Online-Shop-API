package com.marketplace.validator;

import java.time.LocalDate;

public interface IValidatorService {
    boolean emailIsNotValid(String email);
    boolean usernameIsNotValid(String email);
    boolean phoneNumberIsNotValid(String phoneNumber);
    boolean ageIsNotValid(LocalDate dob);
    boolean idIsNotValid(String id);
}
