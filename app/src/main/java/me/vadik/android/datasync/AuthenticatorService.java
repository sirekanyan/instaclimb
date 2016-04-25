package me.vadik.android.datasync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * User: vadik
 * Date: 4/25/16
 */
public class AuthenticatorService extends Service {
    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
