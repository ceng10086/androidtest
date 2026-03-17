package com.example.androidtest.model;

public class Task {
    private final long id;
    private String title;
    private String note;
    private boolean done;

    public Task(long id, String title, String note, boolean done) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.done = done;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
