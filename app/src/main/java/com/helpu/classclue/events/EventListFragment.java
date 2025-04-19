package com.helpu.classclue.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get status from arguments
        int status = getArguments() != null ? getArguments().getInt(ARG_STATUS) : 0;

        // Sample data - replace with actual filtered data
        List<Event> events = new ArrayList<>();
        if(status == 0) { // To Do
            events.add(new Event("Web Programming", "Today, Monday 17", "11:30 AM - 12:30 PM", 2));
        } else if(status == 1) { // In Progress
            events.add(new Event("Mobile Development", "Thursday 18", "11:30 AM - 12:30 PM", 1));
        }

        EventAdapter adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);

        return view;
    }
}