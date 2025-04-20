package com.helpu.classclue.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;
import java.util.List;

public class AdminSubjectAdapter extends RecyclerView.Adapter<AdminSubjectAdapter.ViewHolder> {

    private final List<Subject> subjects;

    public AdminSubjectAdapter(List<Subject> subjects) {
        this.subjects = subjects;
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
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvName, tvSemester;

        ViewHolder(View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvName = itemView.findViewById(R.id.tvName);
            tvSemester = itemView.findViewById(R.id.tvSemester);
        }
    }
}