package com.marketplace.constants;

public interface APIConstants {

    String ITEM_LINK_START = "localhost:8080/";
    String EMAIL_REGEX = "\\w+@\\w+.\\w+$";
    String PHONE_NUMBER_REGEX = "\\+38\\d{10}$";
    String USERNAME_REGEX = "^[a-z0-9_]+$";
    String ID_REGEX = "^\\d+";
    int VALID_AGE = 14;
    String SECRET_KEY = "secret-token";
    long TOKEN_LIFETIME = 1000 * 60 * 60 * 6;
    String API_PREFIX = "api/v1/";
    String AUTH_PATH = "/"+API_PREFIX+"auth/authenticate";
    String REGISTER_PATH = "/"+API_PREFIX+"user/register";
    String PRODUCTS_PATH = "/"+API_PREFIX+"product";
}
