package me.vadik.instaclimb.login;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static me.vadik.instaclimb.login.InstaclimbLogin.*;
import static me.vadik.instaclimb.login.SessionCookieJar.*;

/**
 * User: vadik
 * Date: 6/15/16
 */

class InstaclimbLoginTask extends AsyncTask<Request, Void, UserSession> {

    private final OnPostExecuteListener listener;
    private Exception exception;

    public InstaclimbLoginTask(OnPostExecuteListener listener) {
        this.listener = listener;
    }

    @Override
    protected UserSession doInBackground(Request... requests) {
        try {
            return login(requests[0]);
        } catch (Exception e) {
            Log.e("me", e.getMessage(), e);
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(UserSession session) {
        listener.onPostExecute();
        if (exception == null) {
            listener.onSuccessLogin(session);
        } else {
            listener.onErrorLogin(exception);
        }
    }

    private UserSession login(Request request) throws IOException, CookieException {
        SessionCookieJar sessionCookieJar = new SessionCookieJar(request.url());
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .cookieJar(sessionCookieJar.getCookieJar())
                .followRedirects(false)
                .build();
        Response response = client.newCall(request).execute();
        response.close(); // todo: do i need 'finally' block here?
        int code = response.code();
        if (code != 302) {
            throw new UnexpectedResponseException(code);
        }
        return new UserSession(
                sessionCookieJar.getSessionId(),
                sessionCookieJar.getUserId());
    }

    private static class UnexpectedResponseException extends IOException {
        private final int code;

        public UnexpectedResponseException(int code) {
            this.code = code;
        }

        @Override
        public String getMessage() {
            return "Unexpected http code " + code;
        }
    }
}
