package com.jotangi.NumberHealthy.Module.Response;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jotangi.NumberHealthy.Module.NotificationMethod;
import com.jotangi.NumberHealthy.R;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private String TAG = "(TAG)" + getClass().getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            Log.w(TAG, "title " + remoteMessage.getNotification().getTitle());
            Log.w(TAG, "body " + remoteMessage.getNotification().getBody());
        }

        //need
        new NotificationMethod(this).sendNormalNotification(
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),
                R.drawable.ic_numhealth_icon,
                null
        );
    }

    public static String token = "";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.w(TAG, "onNewToken " + s);
        token = s;
    }
}
