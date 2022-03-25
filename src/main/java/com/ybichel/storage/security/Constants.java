package com.ybichel.storage.security;

public final class Constants {
    private Constants() {}

    public static final String REGISTER_URL = "account/register";
    public static final String REGISTER_URL_STARS = "account/register/**";
    public static final String LOGIN_URL = "account/login";
    public static final String LOGIN_URL_STARS = "account/login/**";
    public static final String REGISTRATION_CONFIRM_URL = "account/registration-confirm";
    public static final String REGISTRATION_CONFIRM_URL_STARS = "account/registration-confirm/**";

    public static final String PERMISSION_READ = "READ";
    public static final String PERMISSION_CREATE = "CREATE";
    public static final String PERMISSION_UPDATE = "UPDATE";
    public static final String PERMISSION_DELETE = "DELETE";

    public static final String AUTHORITY_STORAGE_SYSTEM = "STORAGE_SYSTEM";
    public static final String AUTHORITY_STORAGE_ADMIN = "STORAGE_ADMIN";
    public static final String AUTHORITY_STORAGE_USER = "STORAGE_USER";

    public static final String AUTHORIZATION_HEADER_PREFIX_VALUE = "Bearer ";
    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";

    public static final String JWT_CLAIMS_KEY_ACCOUNT_ID = "accountId";
    public static final String JWT_CLAIMS_KEY_EMAIL = "email";
    public static final String JWT_CLAIMS_KEY_ROLE = "roles";
    public static final String JWT_STORAGE_SUBJECT = "Packford's Storage";
    public static final String JWT_HEADER_ALG_VALUE = "HS512";
    public static final String JWT_HEADER_TYP_VALUE = "JWT";
}
