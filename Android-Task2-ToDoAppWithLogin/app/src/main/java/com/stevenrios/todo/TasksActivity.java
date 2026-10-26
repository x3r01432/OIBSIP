package com.stevenrios.todo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends Activity {
    private DbHelper db;
    private long uid;
    private List<Task> tasks = new ArrayList<>();
    private TaskAdapter adapter;
    private TextView empty;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        uid = Session.userId(this);
        if (uid == -1) { startActivity(new Intent(this, LoginActivity.class)); finish(); return; }
        setContentView(R.layout.activity_tasks);
        db = new DbHelper(this);
        ((TextView) findViewById(R.id.textGreeting)).setText("Hi, " + Session.name(this) + " 👋");
        empty = findViewById(R.id.textEmpty);
        ListView list = findViewById(R.id.listTasks);
        adapter = new TaskAdapter();
        list.setAdapter(adapter);
        EditText input = findViewById(R.id.editTask);
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            String t = input.getText().toString();
            String err = Validator.task(t);
            if (err != null) { Toast.makeText(this, err, Toast.LENGTH_SHORT).show(); return; }
            db.addTask(uid, t); input.setText(""); refresh();
        });
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Session.logout(this);
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // no back-navigation into tasks
            startActivity(i); finish();
        });
        refresh();
    }

    private void refresh() {
        tasks = db.tasks(uid);
        adapter.notifyDataSetChanged();
        empty.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private class TaskAdapter extends BaseAdapter {
        @Override public int getCount() { return tasks.size(); }
        @Override public Task getItem(int i) { return tasks.get(i); }
        @Override public long getItemId(int i) { return tasks.get(i).id; }
        @Override public View getView(int i, View v, ViewGroup parent) {
            if (v == null) v = getLayoutInflater().inflate(R.layout.item_task, parent, false);
            Task t = getItem(i);
            CheckBox cb = v.findViewById(R.id.checkDone);
            TextView title = v.findViewById(R.id.textTitle);
            title.setText(t.title);
            cb.setOnCheckedChangeListener(null);
            cb.setChecked(t.done);
            styleDone(title, t.done);
            cb.setOnCheckedChangeListener((btn, checked) -> { db.setDone(uid, t.id, checked); refresh(); });
            v.findViewById(R.id.btnDelete).setOnClickListener(x -> { db.deleteTask(uid, t.id); refresh(); Toast.makeText(TasksActivity.this, "Task deleted", Toast.LENGTH_SHORT).show(); });
            return v;
        }
    }

    static void styleDone(TextView tv, boolean done) {
        if (done) { tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); tv.setTextColor(0xFF9E9E9E); }
        else { tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG); tv.setTextColor(0xFF212121); }
    }
}
