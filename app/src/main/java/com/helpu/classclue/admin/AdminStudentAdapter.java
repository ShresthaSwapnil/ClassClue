package com.helpu.classclue.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Student;
import java.util.List;

public class AdminStudentAdapter extends RecyclerView.Adapter<AdminStudentAdapter.ViewHolder> {

    private final List<Student> students;

    public AdminStudentAdapter(List<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.tvName.setText(student.getName());
        holder.tvStudentId.setText(student.getStudentId());
        holder.tvEmail.setText(student.getEmail());
        holder.tvSemester.setText("Semester: " + student.getSemester());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStudentId, tvEmail, tvSemester;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvSemester = itemView.findViewById(R.id.tvSemester);
        }
    }
}