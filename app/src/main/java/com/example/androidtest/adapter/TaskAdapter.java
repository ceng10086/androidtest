package com.example.androidtest.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtest.R;
import com.example.androidtest.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface Listener {
        void onDoneToggled(@NonNull Task task, boolean isDone);
    }

    @NonNull
    private final Listener listener;

    @NonNull
    private final ArrayList<Task> tasks = new ArrayList<>();

    public TaskAdapter(@NonNull Listener listener) {
        this.listener = listener;
    }

    @NonNull
    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    public void addTask(@NonNull Task task) {
        tasks.add(0, task);
        notifyItemInserted(0);
    }

    public Task removeAt(int position) {
        Task removed = tasks.remove(position);
        notifyItemRemoved(position);
        return removed;
    }

    public void insertAt(int position, @NonNull Task task) {
        int safePos = Math.max(0, Math.min(position, tasks.size()));
        tasks.add(safePos, task);
        notifyItemInserted(safePos);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkDone;
        private final TextView textTitle;
        private final TextView textNote;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkDone = itemView.findViewById(R.id.checkDone);
            textTitle = itemView.findViewById(R.id.textTitle);
            textNote = itemView.findViewById(R.id.textNote);
        }

        void bind(@NonNull Task task) {
            checkDone.setOnCheckedChangeListener(null);

            textTitle.setText(task.getTitle());

            if (TextUtils.isEmpty(task.getNote())) {
                textNote.setVisibility(View.GONE);
            } else {
                textNote.setVisibility(View.VISIBLE);
                textNote.setText(task.getNote());
            }

            checkDone.setChecked(task.isDone());

            int flags = textTitle.getPaintFlags();
            if (task.isDone()) {
                textTitle.setPaintFlags(flags | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textTitle.setPaintFlags(flags & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            checkDone.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onDoneToggled(task, isChecked));
        }
    }
}
