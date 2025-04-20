package com.helpu.classclue.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Event;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.tvSubject.setText(event.getSubject());
        holder.tvDueDate.setText("Due: " + event.getDate());
        holder.tvTime.setText(event.getTime());
        holder.tvTaskCount.setText(event.getTitle() + " tasks");
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateList(List<Event> newList) {
        eventList = newList;
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvDueDate, tvTime, tvTaskCount;

        EventViewHolder(View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTaskCount = itemView.findViewById(R.id.tvTaskCount);
        }
    }
}