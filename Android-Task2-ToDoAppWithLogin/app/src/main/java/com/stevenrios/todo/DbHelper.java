package com.stevenrios.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/** SQLite storage: users(id, username UNIQUE, password_hash) and tasks(id, user_id FK, title, done, created_at). */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB = "todo.db";
    private static final int VERSION = 1;

    public DbHelper(Context c) { super(c, DB, null, VERSION); }

    @Override public void onConfigure(SQLiteDatabase db) { db.setForeignKeyConstraintsEnabled(true); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE COLLATE NOCASE, password_hash TEXT NOT NULL)");
        db.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE, " +
                "title TEXT NOT NULL, done INTEGER NOT NULL DEFAULT 0, created_at INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX idx_tasks_user ON tasks(user_id)");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("DROP TABLE IF EXISTS tasks"); db.execSQL("DROP TABLE IF EXISTS users"); onCreate(db);
    }

    /** @return new user id, or -1 if the username is taken. */
    public long register(String username, String password) {
        ContentValues v = new ContentValues();
        v.put("username", username.trim());
        v.put("password_hash", PasswordHasher.hash(password));
        return getWritableDatabase().insert("users", null, v);
    }

    /** @return user id on success, -1 on bad credentials. Parameterised query: no SQL injection. */
    public long login(String username, String password) {
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id, password_hash FROM users WHERE username = ?", new String[]{username.trim()})) {
            if (c.moveToFirst() && PasswordHasher.verify(password, c.getString(1))) return c.getLong(0);
        }
        return -1;
    }

    public List<Task> tasks(long userId) {
        List<Task> out = new ArrayList<>();
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id, title, done FROM tasks WHERE user_id = ? ORDER BY done, created_at DESC", new String[]{String.valueOf(userId)})) {
            while (c.moveToNext()) out.add(new Task(c.getLong(0), c.getString(1), c.getInt(2) == 1));
        }
        return out;
    }

    public void addTask(long userId, String title) {
        ContentValues v = new ContentValues();
        v.put("user_id", userId); v.put("title", title.trim()); v.put("created_at", System.currentTimeMillis());
        getWritableDatabase().insert("tasks", null, v);
    }

    // user_id in every WHERE so one user can never modify another user's rows
    public void setDone(long userId, long taskId, boolean done) {
        ContentValues v = new ContentValues(); v.put("done", done ? 1 : 0);
        getWritableDatabase().update("tasks", v, "id = ? AND user_id = ?", new String[]{String.valueOf(taskId), String.valueOf(userId)});
    }

    public void deleteTask(long userId, long taskId) {
        getWritableDatabase().delete("tasks", "id = ? AND user_id = ?", new String[]{String.valueOf(taskId), String.valueOf(userId)});
    }
}
