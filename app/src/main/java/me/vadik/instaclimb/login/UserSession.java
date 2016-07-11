package me.vadik.instaclimb.login;

/**
 * User: vadik
 * Date: 6/16/16
 */
public class UserSession {

    private final String sessionId;
    private final int userId;

    public UserSession(String sessionId, int userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getUserId() {
        return userId;
    }
}
