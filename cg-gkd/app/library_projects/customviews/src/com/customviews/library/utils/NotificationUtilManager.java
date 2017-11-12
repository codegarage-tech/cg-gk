package com.customviews.library.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.customviews.library.R;

import java.lang.reflect.Method;

/**
 * Created by rashed on 3/22/16.
 */
public class NotificationUtilManager {

    private static NotificationManager mNotificationManager = null;
    public static int NOTIFICATION_ID = 0;
    public static int PENDING_INTENT_REQUEST_ID = 0;

    public static NotificationManager getNotificationManager(Context applicationContext) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    public static class NotificationBuilder {

        private Intent intent = null;
        private String title = "";
        private String text = "";
        private int appIconID = -1;
        private static NotificationBuilder notificationBuilder;
        private boolean isSoundEnabled = false;
        private boolean isVibrationEnabled = false;
        private boolean isRemovedNotificationOnClick = false;
        private boolean isNotificationAlertForOnce=false;
        private int notificationSound = -1;
        private int notificationID = NOTIFICATION_ID;
        private int pendingIntentRequestID = PENDING_INTENT_REQUEST_ID;

        private NotificationBuilder() {
        }

        public static NotificationBuilder getInstance() {
            if (notificationBuilder == null) {
                notificationBuilder = new NotificationBuilder();
            }
            return notificationBuilder;
        }

        public NotificationBuilder setIntent(Intent intent) {
            this.intent = intent;
            return this;
        }

        public NotificationBuilder setSound(int sound) {
            this.notificationSound = sound;
            this.isSoundEnabled = true;
            return this;
        }

        public NotificationBuilder setOnlyAlertOnce(boolean onlyAlertOnce) {
            this.isNotificationAlertForOnce = onlyAlertOnce;
            return this;
        }

        public NotificationBuilder setDefaultSound() {
            this.isSoundEnabled = true;
            return this;
        }

        public NotificationBuilder setVibration() {
            this.isVibrationEnabled = true;
            return this;
        }

        public NotificationBuilder setRemoveNotificationOnClick(boolean isNotificationRemoved) {
            this.isRemovedNotificationOnClick = isNotificationRemoved;
            return this;
        }

        public NotificationBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public NotificationBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public NotificationBuilder setAppIconID(int resID) {
            this.appIconID = resID;
            return this;
        }

        public NotificationBuilder setNotificationID(int notificationID) {
            this.notificationID = notificationID;
            return this;
        }

        public NotificationBuilder setPendingIntentRequestID(int pendingIntentRequestID) {
            this.pendingIntentRequestID = pendingIntentRequestID;
            return this;
        }

        public void create(Context context) {
            PendingIntent contentIntent = PendingIntent.getActivity(context, pendingIntentRequestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = createNotification(context, contentIntent, title, text, appIconID);
            if (isSoundEnabled) {
                if (notificationSound != -1) {
                    notification.sound = AllSettingsManager.getRawURIPath(context, R.raw.isntit);
                } else {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                }
            }
            if (isVibrationEnabled) {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }
            if (isRemovedNotificationOnClick) {
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
            }

            if(isNotificationAlertForOnce){
                notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
            }
            getNotificationManager(context).notify(notificationID, notification);
        }
    }

    public static void clearNotificationFromNotificationBar(Context context,int notificationID){
        getNotificationManager(context).cancel(notificationID);
    }

    public static Notification createNotification(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification notification;
        if (isNotificationBuilderSupported()) {
            notification = buildNotificationWithBuilder(context, pendingIntent, title, text, iconId);
        } else {
            notification = buildNotificationPreHoneycomb(context, pendingIntent, title, text, iconId);
        }
        return notification;
    }

    public static void showNotification(Context context, String title, String text, int iconId, Class<?> openClassOnTap) {

        Intent showTaskIntent = new Intent(context, openClassOnTap);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = createNotification(context, contentIntent, title, text, iconId);

        notificationManager.notify(0, notification);

    }

    public static boolean isNotificationBuilderSupported() {
        try {
            return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) && Class.forName("android.app.Notification.Builder") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    private static Notification buildNotificationPreHoneycomb(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification notification = new Notification(iconId, "", System.currentTimeMillis());
        try {
            // try to call "setLatestEventInfo" if available
            Method m = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
            m.invoke(notification, context, title, text, pendingIntent);
        } catch (Exception e) {
            // do nothing
        }
        return notification;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    private static Notification buildNotificationWithBuilder(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(iconId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

}
