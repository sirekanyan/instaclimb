package me.vadik.instaclimb.datasync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * User: vadik
 * Date: 4/25/16
 */
public class SyncService extends Service {
    private static SyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
