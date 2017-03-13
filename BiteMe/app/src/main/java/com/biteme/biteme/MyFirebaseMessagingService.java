package com.biteme.biteme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by den_d on 7/01/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    String messagenotif,notifid,notifname;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.v("remotemessage is " , String.valueOf(remoteMessage.getData()));
        try {
            JSONObject   json = new JSONObject(remoteMessage.getData());
            messagenotif = (String) json.get("body");
            notifid = (String) json.get("id");
            notifname = (String) json.get("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, Notification.class);
        intent.putExtra("FROM_USER", MainActivity.profile_id);
        intent.putExtra("TO_USER", notifid);
        intent.putExtra("LOG_IN_USER", MainActivity.profile_id);
        intent.putExtra("profilename",notifname);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/biteme-noty.mp3");

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.splash_img)
                        .setContentTitle("New message from: "+notifname)
                        .setVibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500})
                        .setContentIntent(pendingIntent)//doet niks
                        .setSound(sound)//doet niks
                        .setContentText(""+messagenotif);



        // message, here is where that should be initiated.
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }


}