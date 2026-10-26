package com.stevenrios.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_result);
        int s = getIntent().getIntExtra("score", 0), t = getIntent().getIntExtra("total", 1);
        int pct = Math.round(100f * s / t);
        ((TextView) findViewById(R.id.textEmoji)).setText(pct >= 70 ? "🏆" : pct >= 50 ? "👍" : "📚");
        ((TextView) findViewById(R.id.textTitle)).setText(QuizEngine.verdict(s, t));
        ((TextView) findViewById(R.id.textScore)).setText(s + " / " + t);
        ((TextView) findViewById(R.id.textPercent)).setText(pct + "% correct");
        findViewById(R.id.btnRestart).setOnClickListener(v -> { startActivity(new Intent(this, QuizActivity.class)); finish(); });
    }
}
