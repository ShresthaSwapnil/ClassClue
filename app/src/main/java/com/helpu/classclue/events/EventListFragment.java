package com.helpu.classclue.events;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helpu.classclue.R;
import com.helpu.classclue.models.Event;
import com.helpu.classclue.notifications.EventNotificationManager;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends Fragment {
    private static final String TAG = "EventNotifManager EventListFragment";
    private static final String ARG_STATUS = "status";
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvNoEvents;
    private EventNotificationManager notificationManager;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            Log.d(TAG, "Permission result received: " + isGranted);
            if (isGranted) {
                Log.d(TAG, "Notification permission granted");
                if (getArguments() != null) {
                    loadEvents(getArguments().getInt(ARG_STATUS));
                }
            } else {
                Log.w(TAG, "Notification permission denied");
                Toast.makeText(requireContext(),
                    "Notification permission required for event reminders",
                    Toast.LENGTH_LONG).show();
            }
        });

    public static EventListFragment newInstance(int status) {
        Log.d(TAG, "Creating new instance with status: " + status);
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        notificationManager = EventNotificationManager.getInstance(requireContext());
        Log.d(TAG, "NotificationManager initialized");

        recyclerView = view.findViewById(R.id.recyclerViewEvents);
        progressBar = view.findViewById(R.id.progressBar);
        tvNoEvents = view.findViewById(R.id.tvNoEvents);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "RecyclerView and adapter initialized");

        int status = getArguments() != null ? getArguments().getInt(ARG_STATUS) : 0;
        Log.d(TAG, "Retrieved status from arguments: " + status);
        checkPermissionAndLoadEvents(status);

        return view;
    }

    private void checkPermissionAndLoadEvents(int status) {
        Log.d(TAG, "Checking notification permission for status: " + status);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Requesting notification permission");
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                return;
            }
            Log.d(TAG, "Notification permission already granted");
        }
        loadEvents(status);
    }

    private void loadEvents(int status) {
        Log.d(TAG, "Loading events for status: " + status);
        showLoading(true);
        EventManager.getInstance().getEvents(status, new EventManager.EventsCallback() {
            @Override
            public void onEventsLoaded(List<Event> events) {
                Log.d(TAG, "Events loaded. Count: " + events.size());
                // Schedule reminders for upcoming events
                    Log.d(TAG, "Processing reminders for upcoming events");
                    for (Event event : events) {
                        Log.d(TAG, "event name" + event.getTitle() + "  " + event.getDate());
                            Log.d(TAG, "Scheduling reminders for event: " + event.getTitle() +
                                " (24h: " + event.isReminder24h() +
                                ", 2h: " + event.isReminder2h() + ")");
                            notificationManager.cancelScheduledReminders(event);
                            notificationManager.scheduleEventReminders(event);
                    }

                if (isAdded()) {
                    if (events.isEmpty()) {
                        Log.d(TAG, "No events found");
                        showNoEvents(true);
                    } else {
                        showNoEvents(false);
                        adapter.updateList(events);
                    }
                    showLoading(false);
                } else {
                    Log.w(TAG, "Fragment not attached, skipping UI updates");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error loading events", e);
                if (isAdded()) {
                    showLoading(false);
                    showNoEvents(true);
                    Toast.makeText(requireContext(),
                        "Error loading events", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        if (getArguments() != null) {
            int status = getArguments().getInt(ARG_STATUS);
            Log.d(TAG, "Reloading events for status: " + status);
            checkPermissionAndLoadEvents(status);
        }
    }

    private void showLoading(boolean isLoading) {
        Log.d(TAG, "Showing loading state: " + isLoading);
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
    }

    private void showNoEvents(boolean isEmpty) {
        Log.d(TAG, "Showing no events state: " + isEmpty);
        if (tvNoEvents != null) {
            tvNoEvents.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }
    }
}