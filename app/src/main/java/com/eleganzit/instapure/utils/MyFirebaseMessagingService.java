package com.eleganzit.instapure.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eleganzit.instapure.HomeActivity;
import com.eleganzit.instapure.NotificationActivity;
import com.eleganzit.instapure.R;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import androidx.core.app.NotificationCompat;


/**
 * Created by Uv on 2/7/2018.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    long[] vib;
    SharedPreferences sharedPreferences;
    String notification_type,message,image,type;
    Bitmap imagee;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("mssgggggggg", "" + remoteMessage.getData().toString());

        // TODO(developer): Handle FCM messages here.
        Log.d("mssgggggggg", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("mssgggggggg", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("mssgggggggg", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        try {
            JSONObject jsonObject=new JSONObject(remoteMessage.getData().get("message")+"");
            type=jsonObject.getString("type");
            message=jsonObject.getString("message");

            Log.d("mssgggggggg", "Messageeeeee" + message);

            /*notification_type=jsonObject.getString("type");
            if(notification_type.equalsIgnoreCase("go_online"))
            {
            }*/
            if(type.equalsIgnoreCase("image_notification"))
            {
                image = jsonObject.getString("image_path");

                showImageNotification(remoteMessage.getData().get("title"), message,image);
            }
            else
            {
                showNotification(remoteMessage.getData().get("title"), message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showImageNotification(String title, String text,String image) {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        try {
            URL url = new URL(""+image);
            imagee = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }

        Intent i = new Intent(this, NotificationActivity.class).putExtra("from","notification");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("from","notification");
        //i.putExtra("type","go_online");
        i.putExtra("title",title);
        i.putExtra("content",text);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle(title)
                    .setSmallIcon(getNotificationIcon())
                    .setContentText(text)
                    .setLargeIcon(imagee)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(imagee)
                            .bigLargeIcon(null))
                    .setSound(uri)
                    .setVibrate(new long[]{1000,500})
                    .setContentIntent(pendingIntent)
                    .setColor(getResources().getColor(R.color.colorPrimary));

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String id = "id_product";
                // The user-visible name of the channel.
                CharSequence name = "Instapure";
                // The user-visible description of the channel.
                String description = text;
                int importance = NotificationManager.IMPORTANCE_MAX;
                @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                // Configure the notification channel.
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                mChannel.setLightColor(Color.RED);
                assert manager != null;
                manager.createNotificationChannel(mChannel);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),"id_product")
                        .setSmallIcon(getNotificationIcon())//your app icon
                        .setBadgeIconType(getNotificationIcon()) //your app icon
                        .setChannelId(id)
                        .setSound(uri)
                        .setVibrate(new long[]{1000,500})
                        .setContentTitle(title)
                        .setLargeIcon(imagee)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(imagee)
                                .bigLargeIcon(null))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setNumber(1)
                        .setColor(255)
                        .setContentText(text)
                        .setWhen(System.currentTimeMillis());

                manager.notify(m, notificationBuilder.build());
            }


            manager.notify(m, builder.build());


    }

    private void showNotification(String title, String text) {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        Intent i = new Intent(this, NotificationActivity.class).putExtra("from","notification");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("from","notification");
        i.putExtra("type","go_online");
        i.putExtra("title",title);
        i.putExtra("content",text);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle(title)
                    .setSmallIcon(getNotificationIcon())
                    .setContentText(text)

                    .setSound(uri)
                    .setVibrate(new long[]{1000,500})
                    .setContentIntent(pendingIntent)
                    .setColor(getResources().getColor(R.color.colorPrimary));

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String id = "id_product";
                // The user-visible name of the channel.
                CharSequence name = "Instapure";
                // The user-visible description of the channel.
                String description = text;
                int importance = NotificationManager.IMPORTANCE_MAX;
                @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                // Configure the notification channel.
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                mChannel.setLightColor(Color.RED);
                assert manager != null;
                manager.createNotificationChannel(mChannel);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),"id_product")
                        .setSmallIcon(getNotificationIcon())//your app icon
                        .setBadgeIconType(getNotificationIcon()) //your app icon
                        .setChannelId(id)
                        .setSound(uri)
                        .setVibrate(new long[]{1000,500})
                        .setContentTitle(title)

                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setNumber(1)
                        .setColor(255)
                        .setContentText(text)
                        .setWhen(System.currentTimeMillis());

                manager.notify(m, notificationBuilder.build());
            }


            manager.notify(m, builder.build());


    }

    private int getNotificationIcon () {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.app_icon : R.drawable.app_icon;
    }
}
