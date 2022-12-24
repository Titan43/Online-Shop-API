package com.marketplace.user.validator;

public interface IValidatorConstants {

    String EMAIL_REGEX = "\\w+@\\w+.\\w+$";
    String PHONE_NUMBER_REGEX = "\\+38\\d{10}$";
    String ID_REGEX = "^\\d+";
    int VALID_AGE = 14;
}
