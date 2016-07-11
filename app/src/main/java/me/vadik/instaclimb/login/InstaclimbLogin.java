package me.vadik.instaclimb.login;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * User: vadik
 * Date: 6/15/16
 */
public class InstaclimbLogin {

    private final OnPostExecuteListener listener;

    public InstaclimbLogin(OnPostExecuteListener listener) {
        this.listener = listener;
    }

    public void execute(String email, String password) {

        String url = "http://instaclimb.ru/user/auth/login";

        FormBody formBody = new FormBody.Builder()
                .add("YumUserLogin[username]", email)
                .add("YumUserLogin[password]", password)
                .add("YumUserLogin[rememberMe]", "1")
                .add("yt0", "Вход").build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        new InstaclimbLoginTask(listener).execute(request);
    }

    public interface OnPostExecuteListener {

        void onPostExecute();

        void onSuccessLogin(UserSession session);

        void onErrorLogin(Exception ex);
    }
}
