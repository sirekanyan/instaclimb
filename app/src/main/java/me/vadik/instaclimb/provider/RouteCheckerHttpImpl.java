package me.vadik.instaclimb.provider;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import me.vadik.instaclimb.login.LoginManager;
import me.vadik.instaclimb.model.RouteStatus;

/**
 * User: vadik
 * Date: 7/13/16
 */
public class RouteCheckerHttpImpl extends AbstractRouteChecker {

    public RouteCheckerHttpImpl(Context context) {
        super(LoginManager.getSessionId(context));
    }

    @Override
    public RouteStatus getRouteStatus(int routeId) throws IOException {
        Document doc = getRouteHttpDocument(routeId);
        if (isNotLoggedIn(doc)) {
            throw new UserNotLoggedInException();
        }
        Element statusBadge = doc.select("div.span8 > div.well > div.row span.badge").first();
        if (statusBadge == null) {
            throw new IOException("Cannot obtain data");
        } else if (statusBadge.classNames().contains("badge-success")) {
            return RouteStatus.CLIMBED;
        }
        return RouteStatus.NONE;
    }

    @Override
    public void setRouteStatus(int routeId, RouteStatus status) throws IOException {
        final String url;
        if (status == RouteStatus.NONE) {
            url = "http://instaclimb.ru/comment/unclimb/" + routeId;
        } else {
            url = "http://instaclimb.ru/comment/climb/" + routeId;
        }
        Jsoup.connect(url).followRedirects(false).cookie("PHPSESSID", sessionId).get();
    }

    private Document getRouteHttpDocument(int routeId) throws IOException {
        String url = "http://instaclimb.ru/route/" + routeId;
        return Jsoup.connect(url).cookie("PHPSESSID", sessionId).get();
    }

    private boolean isNotLoggedIn(Document doc) {
        Element needLogin = doc.select("div.span8 > div.well > div.row > div.span > a").first();
        return needLogin != null && "Залогиньтесь".equals(needLogin.text());
    }
}
