package me.vadik.instaclimb.model;

/**
 * User: vadik
 * Date: 7/13/16
 */
public enum RouteStatus {
    NONE, CLIMBED, FLASHED;

    public static RouteStatus negative(RouteStatus status) {
        if (status == CLIMBED || status == FLASHED) {
            return NONE;
        } else {
            return CLIMBED;
        }
    }
}
