package com.stevenrios.todo;

public class Task {
    public final long id; public final String title; public boolean done;
    public Task(long id, String title, boolean done) { this.id = id; this.title = title; this.done = done; }
}
