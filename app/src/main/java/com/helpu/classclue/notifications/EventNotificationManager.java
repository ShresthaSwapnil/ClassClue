package com.helpu.classclue.notifications;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.helpu.classclue.models.Event;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventNotificationManager {
  private static final String TAG = "EventNotifManager";
  private static EventNotificationManager instance;
  private final Context context;
  private final NotificationManagerCompat notificationManager;
  private final AlarmManager alarmManager;
  private static final String CHANNEL_ID = "event_channel";

  public static class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      String title = intent.getStringExtra("title");
      String message = intent.getStringExtra("message");
      int notificationId = intent.getIntExtra("notification_id", 0);

      Log.d(TAG, "Received notification trigger - Title: " + title +
          ", Message: " + message +
          ", ID: " + notificationId);

      NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
          .setSmallIcon(android.R.drawable.ic_dialog_info)
          .setContentTitle(title)
          .setContentText(message)
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setAutoCancel(true);

      try {
        NotificationManagerCompat.from(context).notify(notificationId, builder.build());
        Log.d(TAG, "Notification shown successfully - ID: " + notificationId);
      } catch (Exception e) {
        Log.e(TAG, "Failed to show notification: " + e.getMessage());
      }
    }
  }

  private EventNotificationManager(Context context) {
    Log.d(TAG, "Initializing EventNotificationManager");
    this.context = context.getApplicationContext();
    this.notificationManager = NotificationManagerCompat.from(context);
    this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    createNotificationChannel();
  }

  public static EventNotificationManager getInstance(Context context) {
    if (instance == null) {
      Log.d(TAG, "Creating new EventNotificationManager instance");
      instance = new EventNotificationManager(context);
    }
    return instance;
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Log.d(TAG, "Creating notification channel");
      NotificationChannel channel = new NotificationChannel(
          CHANNEL_ID,
          "Event Notifications",
          NotificationManager.IMPORTANCE_HIGH
      );
      notificationManager.createNotificationChannel(channel);
      Log.d(TAG, "Notification channel created successfully");
    }
  }

  public void cancelScheduledReminders(Event event) {
    Log.d(TAG, "Scheduled reminders for event: " + event.getTitle());
    try {
      // Cancel 24h reminder
      if (event.isReminder24h()) {
        int notificationId24h = event.hashCode() + 24;
        Intent intent24h = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent24h = PendingIntent.getBroadcast(
            context,
            notificationId24h,
            intent24h,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        // Cancel the alarm
        alarmManager.cancel(pendingIntent24h);
        // Cancel the notification if it's showing
        notificationManager.cancel(notificationId24h);
        Log.d(TAG, "Cancelled 24h reminder for event: " + event.getTitle());
      }

      // Cancel 2h reminder
      if (event.isReminder2h()) {
        int notificationId2h = event.hashCode() + 2;
        Intent intent2h = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent2h = PendingIntent.getBroadcast(
            context,
            notificationId2h,
            intent2h,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        // Cancel the alarm
        alarmManager.cancel(pendingIntent2h);
        // Cancel the notification if it's showing
        notificationManager.cancel(notificationId2h);
        Log.d(TAG, "Cancelled 2h reminder for event: " + event.getTitle());
      }

      // Cancel any confirmation notifications
      notificationManager.cancel(event.hashCode() + 1000);

      // Show cancellation confirmation
      showNotification(
          "Reminders Cancelled",
          "All reminders cancelled for: " + event.getTitle(),
          event.hashCode() + 3000 // Different ID for cancellation notification
      );

      Log.d(TAG, "Successfully cancelled all reminders for event: " + event.getTitle());
    } catch (Exception e) {
      Log.e(TAG, "Error cancelling reminders for event: " + event.getTitle(), e);
    }
  }

  public void scheduleEventReminders(Event event) {
    Log.d(TAG, "Starting to schedule reminders for event: " + event.getTitle());

    try {
      // Initial validation
      if (event == null || event.getDate() == null || event.getTime() == null) {
        Log.e(TAG, "Invalid event data");
        return;
      }

      // Get and validate date string
      String dateStr = event.getDate().trim();
      if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
        Log.e(TAG, "Invalid date format. Expected yyyy-MM-dd, got: " + dateStr);
        return;
      }

      // Get and validate time string
      String timeStr = event.getTime().trim();
      if (!timeStr.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
        Log.e(TAG, "Invalid time format. Expected HH:mm or H:mm, got: " + timeStr);
        return;
      }

      // Standardize time format (add leading zero if needed)
      if (timeStr.length() == 4) {
        timeStr = "0" + timeStr;
      }

      Log.d(TAG, "Processing event - Date: " + dateStr + ", Time: " + timeStr);

      // Parse date and time
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

      Date date = dateFormat.parse(dateStr);
      Date time = timeFormat.parse(timeStr);

      if (date == null || time == null) {
        Log.e(TAG, "Failed to parse date or time");
        return;
      }

      // Combine date and time
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);

      Calendar timeCalendar = Calendar.getInstance();
      timeCalendar.setTime(time);

      calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
      calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      long eventTime = calendar.getTimeInMillis();
      long currentTime = System.currentTimeMillis();

      Log.d(TAG, "Event time: " + calendar.getTime());
      Log.d(TAG, "Current time: " + new Date(currentTime));

      if (eventTime <= currentTime) {
        Log.w(TAG, "Event is in the past, skipping notifications");
        return;
      }

      // Schedule 24h reminder
      if (event.isReminder24h()) {
        long reminder24Time = eventTime - (24 * 60 * 60 * 1000);
        if (reminder24Time > currentTime) {
          Log.d(TAG, "Scheduling 24h reminder for: " + event.getTitle() +
              " at " + new Date(reminder24Time));
          scheduleNotification(
              event.getTitle(),
              "Event starts in 24 hours at " + timeStr,
              reminder24Time,
              event.hashCode() + 24
          );
        } else {
          Log.w(TAG, "24h reminder time has already passed");
        }
      }

      // Schedule 2h reminder
      if (event.isReminder2h()) {
        long reminder2Time = eventTime - (2 * 60 * 60 * 1000);
        if (reminder2Time > currentTime) {
          Log.d(TAG, "Scheduling 2h reminder for: " + event.getTitle() +
              " at " + new Date(reminder2Time));
          scheduleNotification(
              event.getTitle(),
              "Event starts in 2 hours at " + timeStr,
              reminder2Time,
              event.hashCode() + 2
          );
        } else {
          Log.w(TAG, "2h reminder time has already passed");
        }
      }

    } catch (Exception e) {
      Log.e(TAG, "Error scheduling reminders for event: " + event.getTitle());
      Log.e(TAG, "Date: " + event.getDate() + ", Time: " + event.getTime());
      Log.e(TAG, "Error details: " + e.getMessage());
      e.printStackTrace();
    }
  }
  // Helper method to check if a string is in time format
  private boolean isTimeFormat(String str) {
    if (str == null || str.isEmpty()) return false;

    // Check for both "HH:mm" and "H:mm" formats
    return str.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
  }

  @SuppressLint("ScheduleExactAlarm")
  private void scheduleNotification(String title, String message, long triggerTime, int notificationId) {
    Log.d(TAG, String.format("Scheduling notification - Title: %s, TriggerTime: %d, ID: %d",
        title, triggerTime, notificationId));

    Intent intent = new Intent(context, NotificationReceiver.class);
    intent.putExtra("title", title);
    intent.putExtra("message", message);
    intent.putExtra("notification_id", notificationId);

    PendingIntent pendingIntent = PendingIntent.getBroadcast(
        context,
        notificationId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        );
      } else {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        );
      }
      Log.d(TAG, "Alarm scheduled successfully for ID: " + notificationId);
    } catch (Exception e) {
      Log.e(TAG, "Failed to schedule alarm: " + e.getMessage());
    }
  }

  public void showNotification(String title, String message, int notificationId) {
    Log.d(TAG, String.format("Showing notification - Title: %s, Message: %s, ID: %d",
        title, message, notificationId));

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true);

    try {
      notificationManager.notify(notificationId, builder.build());
      Log.d(TAG, "Notification shown successfully - ID: " + notificationId);
    } catch (Exception e) {
      Log.e(TAG, "Failed to show notification: " + e.getMessage());
    }
  }

  public void cancelReminder(Event event) {
    Log.d(TAG, "Canceling reminders for event: " + event.getTitle());
    // Cancel both 24h and 2h reminders if they exist
    cancelNotification(event.hashCode() + 24);
    cancelNotification(event.hashCode() + 2);
  }

  private void cancelNotification(int notificationId) {
    Log.d(TAG, "Canceling notification with ID: " + notificationId);
    try {
      Intent intent = new Intent(context, NotificationReceiver.class);
      PendingIntent pendingIntent = PendingIntent.getBroadcast(
          context,
          notificationId,
          intent,
          PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
      );
      alarmManager.cancel(pendingIntent);
      notificationManager.cancel(notificationId);
      Log.d(TAG, "Successfully canceled notification - ID: " + notificationId);
    } catch (Exception e) {
      Log.e(TAG, "Error canceling notification: " + e.getMessage());
    }
  }
}