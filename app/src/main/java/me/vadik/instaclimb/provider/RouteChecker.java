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
public class RouteChecker {
    private final String sessionId;

    public RouteChecker(Context context) {
        this.sessionId = LoginManager.getSessionId(context);
    }

    public RouteStatus getRouteStatus(int routeId) throws IOException {
        Document doc = getDocument(routeId);
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

    private Document getDocument(int routeId) throws IOException {
        String url = "http://instaclimb.ru/route/" + routeId;
        return Jsoup.connect(url).cookie("PHPSESSID", sessionId).get();
    }

    private boolean isNotLoggedIn(Document doc) {
        Element needLogin = doc.select("div.span8 > div.well > div.row > div.span > a").first();
        return needLogin != null && "Залогиньтесь".equals(needLogin.text());
    }
}
