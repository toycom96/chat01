package com.example.ethan.share01;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    // web server 에서 받을 extra key (web server 와 동일해야 함)
    static final String TITLE_EXTRA_KEY = "Title";
    static final String MSG_EXTRA_KEY = "Msg";
    static final String TYPE_EXTRA_CODE = "Type";
    // web server 에서 받을 extras key

    public GCMIntentService() {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GCMIntentService(String name) {
        super(name);
        System.out.println("************************************************* GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        System.out.println("************************************************* messageType : " + messageType);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                // 메시지를 받은 후 작업 시작
                System.out.println("************************************************* Working........................... ");

                // Post notification of received message.
                System.out.println("************************************************* 상태바 알림 호출");
                sendNotification(extras);
                refreshChat(extras);
                System.out.println("************************************************* Received toString : " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    private void refreshChat(Bundle extras) {

        /*
         * 채팅방을 refresh 할 것인지에 대해 체크하는 함수
         *
         * chatRoom_id값도 추가 되면 id값을 intent로 넘겨준다.
         * intent를 activity에서 받은 뒤 자신의 현재 위치와 비교한 뒤
         * 동일한 chatroom일 경우 refresh 한다.
         *
         */


        /*if (extras.getString(MSG_EXTRA_KEY).equals("hi")) {

        }*/

        Intent i = new Intent();
        i.setAction("appendChatScreenMsg");
        i.putExtra("Msg", extras.getString(MSG_EXTRA_KEY));
        this.sendBroadcast(i);

    }
    // 상태바에 공지
    private void sendNotification(Bundle extras) {
        // 혹시 모를 사용가능한 코드
        String typeCode = extras.getString(TYPE_EXTRA_CODE);
        PendingIntent contentIntent;



        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (typeCode.equals("1")) {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, ChatListActivity.class), 0);
        } else {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);
        }


        NotificationCompat.Builder mBuilder =
                null;
        try {
            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(URLDecoder.decode(extras.getString(TITLE_EXTRA_KEY), "UTF-8"))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(URLDecoder.decode(extras.getString(MSG_EXTRA_KEY), "UTF-8")))
                    .setContentText(URLDecoder.decode(extras.getString(MSG_EXTRA_KEY), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //mBuilder.setVibrate(new long[]{0,3000}); // 진동 효과 (퍼미션 필요)
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true); // 클릭하면 삭제

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
