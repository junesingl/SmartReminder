package com.smartreminder.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by LiJunxing on 6/16/14.
 */
public class SmartReminderAlertService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
