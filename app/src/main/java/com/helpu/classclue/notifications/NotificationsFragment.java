package com.helpu.classclue.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Notification;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificationAdapter(getSampleNotifications());
        recyclerView.setAdapter(adapter);

        return root;
    }

    private List<Notification> getSampleNotifications() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Assignment Due",
                "Web Programming Assignment 1 due in 2 hours", new Date()));
        notifications.add(new Notification("Exam Reminder",
                "Mobile Development midterm exam tomorrow at 9:00 AM", new Date()));
        notifications.add(new Notification("Class Update",
                "Advanced OOP class canceled today", new Date()));
        return notifications;
    }
}