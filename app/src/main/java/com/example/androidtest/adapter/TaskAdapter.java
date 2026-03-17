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
import com.example.androidtest.data.db.TaskEntity;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface Listener {
        void onDoneToggled(@NonNull TaskEntity task, boolean isDone);

        void onItemClicked(@NonNull TaskEntity task);
    }

    @NonNull
    private final Listener listener;

    @NonNull
    private final ArrayList<TaskEntity> tasks = new ArrayList<>();

    public TaskAdapter(@NonNull Listener listener) {
        this.listener = listener;
    }

    @NonNull
    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public TaskEntity getTaskAt(int position) {
        return tasks.get(position);
    }

    public void setTasks(@NonNull List<TaskEntity> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
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

        void bind(@NonNull TaskEntity task) {
            checkDone.setOnCheckedChangeListener(null);

            textTitle.setText(task.title);

            if (TextUtils.isEmpty(task.note)) {
                textNote.setVisibility(View.GONE);
            } else {
                textNote.setVisibility(View.VISIBLE);
                textNote.setText(task.note);
            }

            checkDone.setChecked(task.isDone);

            int flags = textTitle.getPaintFlags();
            if (task.isDone) {
                textTitle.setPaintFlags(flags | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textTitle.setPaintFlags(flags & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            checkDone.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onDoneToggled(task, isChecked));
            itemView.setOnClickListener(v -> listener.onItemClicked(task));
        }
    }
}
