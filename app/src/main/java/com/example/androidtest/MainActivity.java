package com.example.androidtest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtest.adapter.TaskAdapter;
import com.example.androidtest.model.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter adapter;
    private View emptyView;
    private View rootView;

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

        adapter = new TaskAdapter((task, isDone) -> {
            task.setDone(isDone);
            int position = adapter.getTasks().indexOf(task);
            if (position >= 0) {
                adapter.notifyItemChanged(position);
            }
        });
        recyclerView.setAdapter(adapter);

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

                Task removed = adapter.removeAt(position);
                updateEmptyState();

                Snackbar.make(rootView, R.string.deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, v -> {
                            adapter.insertAt(position, removed);
                            updateEmptyState();
                        })
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        findViewById(R.id.fabAdd).setOnClickListener(v -> showAddDialog());

        updateEmptyState();
    }

    private void updateEmptyState() {
        boolean isEmpty = adapter == null || adapter.isEmpty();
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        TextInputLayout titleLayout = dialogView.findViewById(R.id.inputTitleLayout);
        TextInputEditText titleInput = dialogView.findViewById(R.id.inputTitle);
        TextInputEditText noteInput = dialogView.findViewById(R.id.inputNote);

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.add_task)
                .setView(dialogView)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String title = titleInput.getText() == null ? "" : titleInput.getText().toString().trim();
            String note = noteInput.getText() == null ? "" : noteInput.getText().toString().trim();
            if (title.isEmpty()) {
                titleLayout.setError(getString(R.string.title_required));
                return;
            }
            titleLayout.setError(null);

            Task task = new Task(System.currentTimeMillis(), title, note, false);
            adapter.addTask(task);
            updateEmptyState();
            dialog.dismiss();
        }));

        dialog.show();
    }
}