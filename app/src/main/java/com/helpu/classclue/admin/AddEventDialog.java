package com.helpu.classclue.admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Event;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class AddEventDialog extends DialogFragment {

    private EditText etId, etTitle, etSubjectId, etDate, etTime, etLocation;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
    FirebaseFirestore db;

    // Interface to communicate with the activity
    public interface EventAddListener {
        void onEventAdded();
    }

    private EventAddListener eventAddListener;

    public void setEventAddListener(EventAddListener listener) {
        this.eventAddListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_event, null);

        etId = view.findViewById(R.id.etId);
        etTitle = view.findViewById(R.id.etTitle);
        etSubjectId = view.findViewById(R.id.etSubject);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        etLocation = view.findViewById(R.id.etLocation);
        db = FirebaseFirestore.getInstance();

        // Generate a unique ID for the event
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        etId.setText(uniqueId);

        // Make date and time fields not directly editable
        etDate.setFocusable(false);
        etDate.setClickable(true);

        etTime.setFocusable(false);
        etTime.setClickable(true);

        // Set click listeners for date and time pickers
        etDate.setOnClickListener(v -> showDatePickerDialog());
        etTime.setOnClickListener(v -> showTimePickerDialog());

        builder.setView(view)
                .setTitle("Add New Event")
                .setPositiveButton("Save", (dialog, id) -> {
                    if(validateInputs()) {
                        try {
                            String eventId = etId.getText().toString().trim();
                            Event event = new Event(
                                    etTitle.getText().toString().trim(),
                                    etSubjectId.getText().toString().trim(),
                                    etDate.getText().toString().trim(),
                                    etTime.getText().toString().trim(),
                                    etLocation.getText().toString().trim()
                            );

                            saveEventToFirestore(event, eventId);
                        } catch (Exception e) {
                            showToast("Error creating event: " + e.getMessage());
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dismiss());

        return builder.create();
    }

    private void saveEventToFirestore(Event event, String eventId) {
        // Show a loading indicator if needed

        // Add a new document with the specified ID
        db.collection("events")
                .document(eventId) // Use the provided eventId as the document ID
                .set(event) // Use .set() to explicitly set the document data
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast("Event added successfully");

                        // Notify the activity that an event was added
                        if (eventAddListener != null) {
                            eventAddListener.onEventAdded();
                        }

                        // Close the dialog
                        dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Error adding event: " + e.getMessage());
                    }
                });
    }

    private void showDatePickerDialog() {
        try {
            final Calendar calendar = Calendar.getInstance();
            // If a date is already set, use it as the default in the picker
            if (!etDate.getText().toString().isEmpty()) {
                try {
                    Calendar savedDate = Calendar.getInstance();
                    savedDate.setTime(dateFormatter.parse(etDate.getText().toString()));
                    calendar.set(Calendar.YEAR, savedDate.get(Calendar.YEAR));
                    calendar.set(Calendar.MONTH, savedDate.get(Calendar.MONTH));
                    calendar.set(Calendar.DAY_OF_MONTH, savedDate.get(Calendar.DAY_OF_MONTH));
                } catch (Exception e) {
                    // If parsing fails, use current date
                }
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(selectedYear, selectedMonth, selectedDay);
                        etDate.setText(dateFormatter.format(newDate.getTime()));
                        etDate.setError(null); // Clear any previous error
                    },
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        } catch (Exception e) {
            showToast("Error showing date picker: " + e.getMessage());
        }
    }

    private void showTimePickerDialog() {
        try {
            final Calendar calendar = Calendar.getInstance();
            // If a time is already set, use it as the default in the picker
            if (!etTime.getText().toString().isEmpty()) {
                try {
                    Calendar savedTime = Calendar.getInstance();
                    savedTime.setTime(timeFormatter.parse(etTime.getText().toString()));
                    calendar.set(Calendar.HOUR_OF_DAY, savedTime.get(Calendar.HOUR_OF_DAY));
                    calendar.set(Calendar.MINUTE, savedTime.get(Calendar.MINUTE));
                } catch (Exception e) {
                    // If parsing fails, use current time
                }
            }

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            boolean is24HourFormat = DateFormat.is24HourFormat(requireContext());

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (TimePicker view, int selectedHour, int selectedMinute) -> {
                        Calendar newTime = Calendar.getInstance();
                        newTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        newTime.set(Calendar.MINUTE, selectedMinute);
                        etTime.setText(timeFormatter.format(newTime.getTime()));
                        etTime.setError(null); // Clear any previous error
                    },
                    hour,
                    minute,
                    is24HourFormat
            );
            timePickerDialog.show();
        } catch (Exception e) {
            showToast("Error showing time picker: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Clear previous errors
        etId.setError(null);
        etTitle.setError(null);
        etSubjectId.setError(null);
        etDate.setError(null);
        etTime.setError(null);
        etLocation.setError(null);

        if(etId.getText().toString().trim().isEmpty()) {
            etId.setError("ID required");
            isValid = false;
        }

        if(etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError("Title required");
            isValid = false;
        }

        if(etSubjectId.getText().toString().trim().isEmpty()) {
            etSubjectId.setError("Subject required");
            isValid = false;
        }

        String dateText = etDate.getText().toString().trim();
        if(dateText.isEmpty()) {
            etDate.setError("Date required");
            etDate.requestFocus();
            showToast("Please select a date");
            isValid = false;
        }

        String timeText = etTime.getText().toString().trim();
        if(timeText.isEmpty()) {
            etTime.setError("Time required");
            etTime.requestFocus();
            showToast("Please select a time");
            isValid = false;
        }

        // Optional: Validate location if required
        if(etLocation.getText().toString().trim().isEmpty()) {
            etLocation.setError("Location required");
            isValid = false;
        }

        return isValid;
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}