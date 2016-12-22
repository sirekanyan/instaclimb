package me.vadik.instaclimb.login;

/**
 * User: vadik
 * Date: 6/16/16
 */
public class UserCredentials {

    public static final UserCredentials EMPTY = new UserCredentials("", "", false);
    private final String email;
    private final String password;
    private final boolean rememberMe;

    public UserCredentials(String email, String password, boolean rememberMe) {
        this.email = email;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }
}
