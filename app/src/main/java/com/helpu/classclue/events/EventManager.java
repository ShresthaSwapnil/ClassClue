package com.helpu.classclue.events;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.helpu.classclue.models.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventManager {
    public static final int STATUS_TODO = 0;
    public static final int STATUS_IN_PROGRESS = 1;
    public static final int STATUS_COMPLETED = 2;

    private static EventManager instance;
    private final FirebaseFirestore db;

    private EventManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    // Method to add a new event to Firestore
    public void addEvent(Event event, OnEventAddedCallback callback) {
        CollectionReference eventsRef = db.collection("events");

        eventsRef.add(event)
                .addOnSuccessListener(documentReference -> {
                    callback.onEventAdded(documentReference.getId());
                })
                .addOnFailureListener(callback::onError);
    }

    public void getEvents(int status, EventsCallback callback) {
        CollectionReference eventsRef = db.collection("events");
        eventsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Event> filteredEvents = new ArrayList<>();
                Date currentDate = new Date();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    try {
                        // Parse event data
                        String subjectId = document.getString("subjectId");
                        String date = document.getString("date");
                        String time = document.getString("time");
                        String title = document.getString("title");
                        String location = document.getString("location");

                        // Create event
                        Event event = new Event(subjectId, date, time, title, location);

                        // Determine the status of the event
                        int eventStatus = determineEventStatus(date, time, currentDate);

                        // Add event to list if it matches the requested status
                        if (eventStatus == status) {
                            filteredEvents.add(event);
                        }
                    } catch (Exception e) {
                        // Handle parsing errors
                    }
                }
                callback.onEventsLoaded(filteredEvents);
            } else {
                callback.onError(task.getException());
            }
        });
    }

    private int determineEventStatus(String dateStr, String timeStr, Date currentDate) {
        try {
            // Parse date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            Date eventDate = dateFormat.parse(dateStr);
            Date eventTime = timeFormat.parse(timeStr);

            if (eventDate == null || eventTime == null) {
                return STATUS_TODO; // Default if parsing fails
            }

            // Combine date and time
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(eventDate);

            Calendar timeCalendar = Calendar.getInstance();
            timeCalendar.setTime(eventTime);

            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));

            Date combinedDateTime = calendar.getTime();

            // Check if event is in the past
            if (combinedDateTime.before(currentDate)) {
                return STATUS_COMPLETED;
            }

            // Check if event is upcoming within 1 hour
            Calendar oneHourFromNow = Calendar.getInstance();
            oneHourFromNow.setTime(currentDate);
            oneHourFromNow.add(Calendar.HOUR_OF_DAY, 1);

            if (combinedDateTime.before(oneHourFromNow.getTime())) {
                return STATUS_IN_PROGRESS;
            }

            // Otherwise, it's a to-do event
            return STATUS_TODO;

        } catch (ParseException e) {
            return STATUS_TODO; // Default if parsing fails
        }
    }

    public interface EventsCallback {
        void onEventsLoaded(List<Event> events);
        void onError(Exception e);
    }

    public interface OnEventAddedCallback {
        void onEventAdded(String eventId);
        void onError(Exception e);
    }
}