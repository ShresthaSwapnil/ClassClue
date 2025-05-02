package com.helpu.classclue.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;
import java.util.List;

public class AdminSubjectAdapter extends RecyclerView.Adapter<AdminSubjectAdapter.ViewHolder> {

    private List<Subject> subjects;
    private OnSubjectActionListener listener;

    // Interface for handling subject actions
    public interface OnSubjectActionListener {
        void onEditSubject(Subject subject, int position);
        void onDeleteSubject(Subject subject, int position);
    }

    public AdminSubjectAdapter(List<Subject> subjects, OnSubjectActionListener listener) {
        this.subjects = subjects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.tvCode.setText(subject.getCode());
        holder.tvName.setText(subject.getName());
        holder.tvSemester.setText("Semester: " + subject.getSemester());
        holder.tvDescription.setText(subject.getDescription());

        // Set click listeners for edit and delete buttons
        holder.ivEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditSubject(subject, holder.getAdapterPosition());
            }
        });

        holder.ivDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteSubject(subject, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvName, tvSemester, tvDescription;
        ImageView ivEdit, ivDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvName = itemView.findViewById(R.id.tvName);
            tvSemester = itemView.findViewById(R.id.tvSemester);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    public void updateSubjects(List<Subject> newSubjects) {
        subjects.clear();
        subjects.addAll(newSubjects);
        notifyDataSetChanged();
    }

    public void removeSubject(int position) {
        if (position >= 0 && position < subjects.size()) {
            subjects.remove(position);
            notifyItemRemoved(position);
        }
    }
}