package com.app.koran.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.koran.ActivityMain;
import com.app.koran.ActivityPostDetails;
import com.app.koran.ActivitySplash;
import com.app.koran.R;
import com.app.koran.data.Constant;
import com.app.koran.data.SharedPref;
import com.app.koran.model.FcmNotif;
import com.app.koran.model.Post;
import com.app.koran.utils.CallbackImageNotif;
import com.app.koran.utils.PermissionUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;

public class FcmMessagingService extends FirebaseMessagingService {

    private static int VIBRATION_TIME = 500; // in millisecond
    private SharedPref sharedPref;
    private int retry_count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPref = new SharedPref(this);
        retry_count = 0;
        if (sharedPref.getNotification() && PermissionUtil.isStorageGranted(this)) {
            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();
                FcmNotif fcmNotif = new FcmNotif();
                fcmNotif.post_id = Integer.parseInt(data.get("post_id"));
                fcmNotif.title = data.get("title");
                fcmNotif.content = data.get("content");
                fcmNotif.image = data.get("image");

                // display notification
                prepareImageNotification(fcmNotif);
            }
        }
    }

    private void prepareImageNotification(final FcmNotif fcmNotif) {
        if (fcmNotif.image != null && !fcmNotif.image.equals("")) {
            loadImageFromUrl(this, fcmNotif.image, new CallbackImageNotif() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    displayNotificationIntent(fcmNotif, bitmap);
                }

                @Override
                public void onFailed(String string) {
                    Log.e("onFailed", string);
                    if (retry_count <= Constant.LOAD_IMAGE_NOTIF_RETRY) {
                        retry_count++;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                prepareImageNotification(fcmNotif);
                            }
                        }, 500);
                    } else {
                        displayNotificationIntent(fcmNotif, null);
                    }
                }
            });
        } else {
            displayNotificationIntent(fcmNotif, null);
        }
    }

    private void displayNotificationIntent(FcmNotif fcmNotif, Bitmap bitmap) {
        Intent intent = new Intent(this, ActivitySplash.class);
        if (fcmNotif.post_id != -1) {
            intent = new Intent(this, ActivityPostDetails.class);
            Post post = new Post();
            post.title = fcmNotif.title;
            post.id = fcmNotif.post_id;
//            boolean from_notif = !ActivityMain.active;
//            intent.putExtra(ActivityPostDetails.EXTRA_OBJC, post);
//            intent.putExtra(ActivityPostDetails.EXTRA_NOTIF, from_notif);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(fcmNotif.title);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(fcmNotif.content));
        builder.setContentText(fcmNotif.content);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setDefaults(Notification.DEFAULT_LIGHTS);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(fcmNotif.content));
        if (bitmap != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(fcmNotif.content));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int unique_id = (int) System.currentTimeMillis();
        notificationManager.notify(unique_id, builder.build());
        playVibrationAndRingtone();
    }

    private void playVibrationAndRingtone() {
        try {
            // play vibration
            if (sharedPref.getVibration()) {
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_TIME);
            }
            RingtoneManager.getRingtone(this, Uri.parse(sharedPref.getRingtone())).play();
        } catch (Exception e) {
        }
    }

    // load image with callback
    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable;
    private void loadImageFromUrl(final Context ctx, final String url, final CallbackImageNotif callback) {
        myRunnable = new Runnable() {
            @Override
            public void run() {
                Picasso.with(ctx).load(url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        callback.onSuccess(bitmap);
                        mainHandler.removeCallbacks(myRunnable);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        callback.onFailed("onBitmapFailed");
                        mainHandler.removeCallbacks(myRunnable);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        };
        mainHandler.post(myRunnable);
    }

}
