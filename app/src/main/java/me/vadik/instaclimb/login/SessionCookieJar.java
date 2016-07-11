package me.vadik.instaclimb.login;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * User: vadik
 * Date: 6/15/16
 */
public class SessionCookieJar {

    private final CookieJar cookieJar;
    private final HttpUrl httpUrl;

    public SessionCookieJar(HttpUrl url) {
        this.httpUrl = url;
        this.cookieJar = new CookieJar() {
            private List<Cookie> cookieStore = new ArrayList<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (httpUrl.equals(url)) {
                    cookieStore.addAll(cookies);
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                if (httpUrl.equals(url)) {
                    return cookieStore;
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    private List<Cookie> getSavedCookiesReversedOrder() {
        List<Cookie> cookies = new ArrayList<>(cookieJar.loadForRequest(httpUrl));
        Collections.reverse(cookies);
        return cookies;
    }

    public String getSessionId() throws CookieNotFoundException {
        for (Cookie cookie : getSavedCookiesReversedOrder()) {
            if (CookieHelper.isSessionIdCookie(cookie)) {
                return cookie.value();
            }
        }
        throw new CookieNotFoundException();
    }

    public int getUserId() throws CookieException, UnsupportedEncodingException {
        for (Cookie cookie : getSavedCookiesReversedOrder()) {
            if (CookieHelper.isUserIdCookie(cookie)) {
                return CookieHelper.parseUserId(cookie.value());
            }
        }
        throw new CookieNotFoundException();
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    private static class CookieHelper {

        private static final String SESSION_COOKIE_NAME = "PHPSESSID";
        private static final String USER_ID_COOKIE_VALUE = "%7Bs%3A2%3A%22id%22%3Bs"; // encoded "{s:2:\"id\";s:"
        private static final Pattern USER_ID_COOKIE_PATTERN = Pattern.compile("\\{s:2:\"id\";s:\\d:\"(\\d*?)\";\\}");

        private static boolean isSessionIdCookie(Cookie cookie) {
            return SESSION_COOKIE_NAME.equals(cookie.name());
        }

        private static boolean isUserIdCookie(Cookie cookie) {
            String value = cookie.value();
            return value != null && value.contains(USER_ID_COOKIE_VALUE);
        }

        public static int parseUserId(String userIdCookieValueEncoded) throws CookieParserException, UnsupportedEncodingException {
            String userIdCookieValue = URLDecoder.decode(userIdCookieValueEncoded, "UTF-8");
            Matcher matcher = USER_ID_COOKIE_PATTERN.matcher(userIdCookieValue);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
            throw new CookieParserException();
        }
    }

    static class CookieException extends Exception {
    }

    static class CookieNotFoundException extends CookieException {
    }

    static class CookieParserException extends CookieException {
    }
}
