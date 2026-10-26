package com.stevenrios.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        if (Session.userId(this) != -1) { openTasks(); return; } // already logged in
        setContentView(R.layout.activity_login);
        EditText user = findViewById(R.id.editUsername), pass = findViewById(R.id.editPassword);
        DbHelper db = new DbHelper(this);
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String u = user.getText().toString(), p = pass.getText().toString();
            if (u.trim().isEmpty() || p.isEmpty()) { Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show(); return; }
            long id = db.login(u, p);
            if (id == -1) { pass.setText(""); Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show(); return; }
            Session.login(this, id, u.trim());
            openTasks();
        });
        findViewById(R.id.btnGoRegister).setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
    private void openTasks() { startActivity(new Intent(this, TasksActivity.class)); finish(); }
}
