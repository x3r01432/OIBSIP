package com.stevenrios.todo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_register);
        EditText user = findViewById(R.id.editUsername), pass = findViewById(R.id.editPassword), conf = findViewById(R.id.editConfirm);
        DbHelper db = new DbHelper(this);
        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            String u = user.getText().toString(), p = pass.getText().toString();
            String err = Validator.username(u);
            if (err == null) err = Validator.password(p, conf.getText().toString());
            if (err != null) { Toast.makeText(this, err, Toast.LENGTH_SHORT).show(); return; }
            if (db.register(u, p) == -1) { Toast.makeText(this, "That username is already taken", Toast.LENGTH_SHORT).show(); return; }
            Toast.makeText(this, "Account created — please log in", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
