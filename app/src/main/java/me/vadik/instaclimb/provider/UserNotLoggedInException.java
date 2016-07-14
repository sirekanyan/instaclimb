package me.vadik.instaclimb.provider;

/**
 * User: vadik
 * Date: 7/14/16
 */
class UserNotLoggedInException extends RuntimeException {
    @Override
    public String getMessage() {
        return "You are not logged in";
    }
}
