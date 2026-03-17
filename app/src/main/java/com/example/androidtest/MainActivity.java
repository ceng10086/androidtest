package com.example.androidtest;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtest.adapter.TaskAdapter;
import com.example.androidtest.data.db.TaskEntity;
import com.example.androidtest.ui.viewmodel.TaskListViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter adapter;
    private View emptyView;
    private View rootView;

    private TaskListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.main);
        emptyView = findViewById(R.id.emptyView);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(TaskListViewModel.class);

        adapter = new TaskAdapter(new TaskAdapter.Listener() {
            @Override
            public void onDoneToggled(TaskEntity task, boolean isDone) {
                viewModel.toggleDone(task, isDone);
            }

            @Override
            public void onItemClicked(TaskEntity task) {
                startActivity(EditTaskActivity.createIntent(MainActivity.this, task.id));
            }
        });
        recyclerView.setAdapter(adapter);

        viewModel.getTasks().observe(this, tasks -> {
            List<TaskEntity> safe = tasks == null ? Collections.emptyList() : tasks;
            adapter.setTasks(safe);
            updateEmptyState(safe);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }

                TaskEntity removed = adapter.getTaskAt(position);
                viewModel.delete(removed);

                Snackbar.make(rootView, R.string.deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, v -> viewModel.insert(removed))
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        findViewById(R.id.fabAdd).setOnClickListener(v -> startActivity(EditTaskActivity.createIntent(this, 0L)));
    }

    private void updateEmptyState(List<TaskEntity> tasks) {
        boolean isEmpty = tasks == null || tasks.isEmpty();
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}