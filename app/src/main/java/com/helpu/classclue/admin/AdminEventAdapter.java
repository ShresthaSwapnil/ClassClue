package com.helpu.classclue.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Event;
import java.util.List;

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventAdapter.ViewHolder> {

    private final List<Event> events;

    public AdminEventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.tvTitle.setText(event.getTitle());
        holder.tvSubject.setText(event.getSubject());
        holder.tvDateTime.setText(String.format("%s • %s", event.getDate(), event.getTime()));
        holder.tvLocation.setText(event.getLocation());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubject, tvDateTime, tvLocation;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
}