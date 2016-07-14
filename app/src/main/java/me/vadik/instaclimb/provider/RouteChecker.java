package me.vadik.instaclimb.provider;

import java.io.IOException;

import me.vadik.instaclimb.model.RouteStatus;

/**
 * User: vadik
 * Date: 7/14/16
 */
public interface RouteChecker {

    RouteStatus getRouteStatus(int routeId) throws IOException;

    void setRouteStatus(int routeId, RouteStatus status) throws IOException;
}
