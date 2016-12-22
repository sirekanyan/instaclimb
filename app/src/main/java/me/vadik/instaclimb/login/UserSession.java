package me.vadik.instaclimb.login;

/**
 * User: vadik
 * Date: 6/16/16
 */
public class UserSession {

    private final String sessionId;
    private final int userId;
    private final UserCredentials credentials;

    UserSession(String sessionId, int userId, UserCredentials credentials) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.credentials = credentials;
    }

    String getSessionId() {
        return sessionId;
    }

    public int getUserId() {
        return userId;
    }

    public UserCredentials getCredentials() {
        return credentials;
    }
}
