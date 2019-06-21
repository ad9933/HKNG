package com.honeymade.hkng;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.HashSet;
import java.util.Vector;

public class KtalkGrabber extends NotificationListenerService {

    static Vector<Notification> ktNotification = new Vector<Notification>();
    static Vector<Notification> currentNotification = ktNotification;

    static Vector<String> senderVector = new Vector<String>();
    static HashSet<String> senderSet = new HashSet<String>();

    static String selectedChatroom = "All";

    final static String NOTIFICATION_CHANNEL_ID = "HKNG_NOTIFICATION_SERVICE";
    final static String NOTIFICATION_CHANNEL_TITLE = "Notification Capture Service";

    NotificationManager nM;
    NotificationManagerCompat nMC;
    NotificationChannel channel;
    NotificationCompat.Builder builder;

    @Override
    public void onCreate() {

        channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_TITLE, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Describes that the service is running.");

        nMC = NotificationManagerCompat.from(this);
        nM = getSystemService(NotificationManager.class);

        nM.createNotificationChannel(channel);

        builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        builder.setSmallIcon(R.drawable.web_hi_res_512).setContentTitle("HKNG Service Running")
                .setContentText("HONEY JAM").setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        nMC.notify(1, builder.build());

        senderVector.add("All");

    }
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        Bundle nExtras = sbn.getNotification().extras;

        if((nExtras.getString(Notification.EXTRA_TITLE) != null) && sbn.getPackageName().equals("com.kakao.talk")) {

            if(nExtras.getString(Notification.EXTRA_SUB_TEXT) != null) {    //단톡일때

                if(senderSet.add(nExtras.getString(Notification.EXTRA_SUB_TEXT))) {
                    senderVector.add(nExtras.getString(Notification.EXTRA_SUB_TEXT));
                    System.out.println("added " + nExtras.getString(Notification.EXTRA_SUB_TEXT));
                }

            } else {                                                        //갠톡일때

                if(senderSet.add("[개인]" + nExtras.getString(Notification.EXTRA_TITLE))) {
                    senderVector.add("[개인]" + nExtras.getString(Notification.EXTRA_TITLE));
                }

            }

            ktNotification.add(sbn.getNotification());

            //if(MainActivity.autoRefresh)
            //    MainActivity.mRecyclerView.setAdapter(MainActivity.myAdapter);

            MainActivity.spinner.setAdapter(MainActivity.spinneradapter);

        }

    }

    public void onListenerConnected() {
        super.onListenerConnected();
        System.out.println("Listener Connected");
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    public void onDestory() {
        System.out.println("Service Destoryed");
        nMC.cancel(1);
    }

    public static String getChatRoom(Notification n) {
        return n.extras.getString(Notification.EXTRA_SUB_TEXT);
    }

    public static String getSender(Notification n) {
        return n.extras.getString(Notification.EXTRA_TITLE);
    }

    public static String getMessage(Notification n) {
        return n.extras.getString(Notification.EXTRA_TEXT);
    }

}
