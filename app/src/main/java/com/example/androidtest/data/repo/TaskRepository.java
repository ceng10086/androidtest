package com.example.androidtest.data.repo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.androidtest.data.db.AppDatabase;
import com.example.androidtest.data.db.TaskDao;
import com.example.androidtest.data.db.TaskEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRepository {

    private final TaskDao taskDao;
    private final Executor writeExecutor = Executors.newSingleThreadExecutor();

    public TaskRepository(@NonNull Context context) {
        this.taskDao = AppDatabase.getInstance(context).taskDao();
    }

    public LiveData<List<TaskEntity>> observeAll() {
        return taskDao.observeAll();
    }

    public LiveData<TaskEntity> observeById(long id) {
        return taskDao.observeById(id);
    }

    public void insert(@NonNull TaskEntity entity) {
        writeExecutor.execute(() -> taskDao.insert(entity));
    }

    public void update(@NonNull TaskEntity entity) {
        writeExecutor.execute(() -> taskDao.update(entity));
    }

    public void delete(@NonNull TaskEntity entity) {
        writeExecutor.execute(() -> taskDao.delete(entity));
    }

    public void deleteById(long id) {
        writeExecutor.execute(() -> taskDao.deleteById(id));
    }
}
