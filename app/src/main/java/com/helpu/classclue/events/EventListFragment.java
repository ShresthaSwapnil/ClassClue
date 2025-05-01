package com.helpu.classclue.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Event;
import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends Fragment {

    private static final String ARG_STATUS = "status";
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvNoEvents;

    public static EventListFragment newInstance(int status) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewEvents);
        progressBar = view.findViewById(R.id.progressBar);
        tvNoEvents = view.findViewById(R.id.tvNoEvents);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with empty list
        adapter = new EventAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Get status from arguments
        int status = getArguments() != null ? getArguments().getInt(ARG_STATUS) : 0;

        // Load events for this status
        loadEvents(status);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh events when fragment becomes visible
        if (getArguments() != null) {
            int status = getArguments().getInt(ARG_STATUS);
            loadEvents(status);
        }
    }

    private void loadEvents(int status) {
        showLoading(true);

        EventManager.getInstance().getEvents(status, new EventManager.EventsCallback() {
            @Override
            public void onEventsLoaded(List<Event> events) {
                if (isAdded()) {  // Check if fragment is still attached to avoid crashes
                    if (events.isEmpty()) {
                        showNoEvents(true);
                    } else {
                        showNoEvents(false);
                        adapter.updateList(events);
                    }
                    showLoading(false);
                }
            }

            @Override
            public void onError(Exception e) {
                if (isAdded()) {  // Check if fragment is still attached
                    showLoading(false);
                    showNoEvents(true);
                    // Consider showing an error message here
                }
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
    }

    private void showNoEvents(boolean isEmpty) {
        if (tvNoEvents != null) {
            tvNoEvents.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }
    }
}