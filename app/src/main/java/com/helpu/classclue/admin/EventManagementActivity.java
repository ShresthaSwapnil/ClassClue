package com.helpu.classclue.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class EventManagementActivity extends AppCompatActivity implements AddEventDialog.EventAddListener, AdminEventAdapter.OnEventDeleteListener {

    private AdminEventAdapter adapter;
    private List<Event> events = new ArrayList<>();
    // Map to store document IDs for each event
    private Map<String, String> eventDocumentIds = new HashMap<>();
    FirebaseFirestore db;
    private static final String TAG = "EventManagementActivity";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_management);

        recyclerView = findViewById(R.id.rvEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();

        // Initialize adapter with empty list
        adapter = new AdminEventAdapter(events);
        adapter.setOnEventDeleteListener(this); // Set this activity as the delete listener
        recyclerView.setAdapter(adapter);

        // Retrieve the events
        retrieveAllEvents();

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddEventDialog());
    }

    private void showAddEventDialog() {
        AddEventDialog dialog = new AddEventDialog();
        dialog.setEventAddListener(this);
        dialog.show(getSupportFragmentManager(), "AddEventDialog");
    }

    // Implementation of EventAddListener interface
    @Override
    public void onEventAdded() {
        retrieveAllEvents();
        Log.d(TAG, "Event added, reloading data...");
    }

    // Implementation of OnEventDeleteListener interface
    @Override
    public void onEventDelete(Event event, int position) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteEvent(event, position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteEvent(Event event, int position) {
        // Get the document ID for this event
        String documentId = getDocumentIdForEvent(event);

        if (documentId == null) {
            Toast.makeText(this, "Unable to delete: Event ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Delete from Firestore
        db.collection("events").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove from local list
                    events.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Event deleted: " + documentId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete event", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error deleting event", e);
                });
    }

    private String getDocumentIdForEvent(Event event) {
        // Create a key based on unique properties of the event
        String eventKey = event.getTitle() + "_" + event.getDate() + "_" + event.getTime();
        return eventDocumentIds.get(eventKey);
    }

    private void retrieveAllEvents() {
        // Get reference to the "events" collection
        CollectionReference eventsCollection = db.collection("events");

        eventsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                events.clear(); // Clear the list
                eventDocumentIds.clear(); // Clear the document IDs map

                for (QueryDocumentSnapshot document : querySnapshot) {
                    Log.d(TAG, document.getId() + " => " + document.getData());

                    // Access fields
                    String eventName = document.getString("title");
                    String eventDate = document.getString("date");
                    String eventSubject = document.getString("subjectId");
                    String eventTime = document.getString("time");
                    String eventLocation = document.getString("location");

                    // Create Event object
                    Event event = new Event(eventName, eventSubject, eventDate, eventTime, eventLocation);
                    events.add(event);

                    // Store the document ID for this event
                    String eventKey = eventName + "_" + eventDate + "_" + eventTime;
                    eventDocumentIds.put(eventKey, document.getId());
                }

                adapter.notifyDataSetChanged();
                Log.d(TAG, "Retrieved " + events.size() + " events");
            } else {
                Log.e(TAG, "Error getting documents: ", task.getException());
                Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}