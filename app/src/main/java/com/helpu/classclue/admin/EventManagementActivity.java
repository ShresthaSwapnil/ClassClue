package com.helpu.classclue.admin;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Event;
import java.util.ArrayList;
import java.util.List;

public class EventManagementActivity extends AppCompatActivity {

    private AdminEventAdapter adapter;
    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_management);

        RecyclerView recyclerView = findViewById(R.id.rvEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        events.add(new Event("Midterm Exam", "Mobile Development", "2025-05-15", "09:00 AM", "Lecture Hall A"));
        events.add(new Event("Assignment Due", "Web Programming", "2025-05-10", "11:59 PM", "Online Submission"));

        adapter = new AdminEventAdapter(events);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddEventDialog());
    }

    private void showAddEventDialog() {
        AddEventDialog dialog = new AddEventDialog();
        dialog.show(getSupportFragmentManager(), "AddEventDialog");
    }

    // Call this from dialog when new event is added
    public void addNewEvent(Event event) {
        events.add(event);
        adapter.notifyItemInserted(events.size() - 1);
    }
}