package com.example.androidtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidtest.data.db.TaskEntity;
import com.example.androidtest.ui.viewmodel.EditTaskViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "task_id";

    private EditTaskViewModel viewModel;

    private TextInputLayout titleLayout;
    private TextInputEditText titleInput;
    private TextInputEditText noteInput;
    private CheckBox doneCheck;

    @Nullable
    private TaskEntity editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_task);

        View root = findViewById(R.id.editRoot);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.editToolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        titleLayout = findViewById(R.id.editTitleLayout);
        titleInput = findViewById(R.id.editTitle);
        noteInput = findViewById(R.id.editNote);
        doneCheck = findViewById(R.id.editDone);

        viewModel = new ViewModelProvider(this).get(EditTaskViewModel.class);

        long taskId = getIntent().getLongExtra(EXTRA_TASK_ID, 0L);
        if (taskId > 0L) {
            setTitle(R.string.edit_task);
            doneCheck.setVisibility(View.VISIBLE);
            viewModel.observeById(taskId).observe(this, entity -> {
                if (entity == null) {
                    return;
                }
                editing = entity;
                titleInput.setText(entity.title);
                noteInput.setText(entity.note);
                doneCheck.setChecked(entity.isDone);
            });
        } else {
            setTitle(R.string.add_task);
            doneCheck.setVisibility(View.GONE);
        }

        findViewById(R.id.buttonSave).setOnClickListener(v -> onSave());
        findViewById(R.id.buttonCancel).setOnClickListener(v -> finish());
    }

    private void onSave() {
        String title = titleInput.getText() == null ? "" : titleInput.getText().toString().trim();
        String note = noteInput.getText() == null ? "" : noteInput.getText().toString().trim();

        if (title.isEmpty()) {
            titleLayout.setError(getString(R.string.title_required));
            return;
        }
        titleLayout.setError(null);

        if (editing == null) {
            viewModel.insert(title, note);
        } else {
            viewModel.update(editing, title, note, doneCheck.isChecked());
        }

        finish();
    }

    public static Intent createIntent(AppCompatActivity activity, long taskId) {
        Intent intent = new Intent(activity, EditTaskActivity.class);
        if (taskId > 0L) {
            intent.putExtra(EXTRA_TASK_ID, taskId);
        }
        return intent;
    }
}
