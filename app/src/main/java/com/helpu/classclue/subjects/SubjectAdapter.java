package com.helpu.classclue.subjects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjectList;
    private OnSubjectClickListener clickListener;

    public SubjectAdapter(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public void setOnSubjectClickListener(OnSubjectClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.bind(subject);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public void updateData(List<Subject> newSubjects) {
        // Create a temporary list to avoid duplicates
        List<Subject> uniqueSubjects = new ArrayList<>();

        // Check for duplicates
        for (Subject subject : newSubjects) {
            boolean isDuplicate = false;
            for (Subject existingSubject : uniqueSubjects) {
                if (existingSubject.equals(subject)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                uniqueSubjects.add(subject);
            }
        }

        this.subjectList.clear();
        this.subjectList.addAll(uniqueSubjects);
        notifyDataSetChanged();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvSubjectCode, tvDescription, tvSemester, tvCapacity;

        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvSubjectCode = itemView.findViewById(R.id.tvSubjectCode);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvSemester = itemView.findViewById(R.id.tvSemester);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onSubjectClick(subjectList.get(position));
                }
            });
        }

        void bind(Subject subject) {
            tvSubjectName.setText(subject.getName());
            tvSubjectCode.setText(subject.getCode());
            tvDescription.setText(subject.getDescription());
            tvSemester.setText(subject.getSemester());
        }
    }

    public interface OnSubjectClickListener {
        void onSubjectClick(Subject subject);
    }
}