package me.vadik.instaclimb.provider;

/**
 * User: vadik
 * Date: 7/14/16
 */
public abstract class AbstractRouteChecker implements RouteChecker {

    protected final String sessionId;

    protected AbstractRouteChecker(String sessionId) {
        this.sessionId = sessionId;
    }
}
