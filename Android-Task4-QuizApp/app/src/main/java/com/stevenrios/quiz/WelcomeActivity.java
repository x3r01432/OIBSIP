package com.stevenrios.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_welcome);
        ((TextView) findViewById(R.id.textInfo)).setText(QuizEngine.bank().size() + " questions on programming & computing.\nTap an answer — you'll see instantly if it's right.");
        findViewById(R.id.btnStart).setOnClickListener(v -> startActivity(new Intent(this, QuizActivity.class)));
    }
}
