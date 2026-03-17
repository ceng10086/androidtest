package com.example.androidtest.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @NonNull
    @ColumnInfo(name = "title")
    public String title;

    @NonNull
    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "is_done")
    public boolean isDone;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    public TaskEntity() {
        this.title = "";
        this.note = "";
        this.isDone = false;
        this.createdAt = 0L;
        this.updatedAt = 0L;
    }

    @Ignore
    public TaskEntity(@NonNull String title,
                      @NonNull String note,
                      boolean isDone,
                      long createdAt,
                      long updatedAt) {
        this.title = title;
        this.note = note;
        this.isDone = isDone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
