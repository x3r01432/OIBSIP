package com.stevenrios.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Random;

public class QuizActivity extends Activity {
    private QuizEngine quiz;
    private final Button[] opts = new Button[4];
    private TextView question, progress, score, feedback;
    private ProgressBar bar;
    private Button next;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_quiz);
        quiz = new QuizEngine(QuizEngine.bank(), new Random());
        question = findViewById(R.id.textQuestion); progress = findViewById(R.id.textProgress);
        score = findViewById(R.id.textScore); feedback = findViewById(R.id.textFeedback);
        bar = findViewById(R.id.progressBar); next = findViewById(R.id.btnNext);
        int[] ids = {R.id.opt0, R.id.opt1, R.id.opt2, R.id.opt3};
        for (int i = 0; i < 4; i++) { final int c = i; opts[i] = findViewById(ids[i]); opts[i].setOnClickListener(v -> onAnswer(c)); }
        next.setOnClickListener(v -> {
            if (quiz.isLast()) {
                Intent r = new Intent(this, ResultActivity.class);
                r.putExtra("score", quiz.score()); r.putExtra("total", quiz.total());
                startActivity(r); finish();
            } else { quiz.next(); show(); }
        });
        bar.setMax(quiz.total());
        show();
    }

    private void show() {
        Question q = quiz.current();
        question.setText(q.text);
        progress.setText("Question " + (quiz.index() + 1) + " / " + quiz.total());
        score.setText("Score: " + quiz.score());
        bar.setProgress(quiz.index() + 1);
        for (int i = 0; i < 4; i++) { opts[i].setText(q.options.get(i)); opts[i].setEnabled(true); opts[i].setBackgroundResource(R.drawable.opt_option); }
        feedback.setText("");
        next.setEnabled(false);
        next.setText(quiz.isLast() ? "See results" : "Next");
    }

    private void onAnswer(int choice) {
        if (quiz.isAnswered()) return;
        boolean ok = quiz.answer(choice);
        int correct = quiz.current().answer;
        opts[correct].setBackgroundResource(R.drawable.opt_correct);           // always reveal the right answer in green
        if (!ok) opts[choice].setBackgroundResource(R.drawable.opt_wrong);      // and the wrong pick in red
        for (Button o : opts) o.setEnabled(false);
        feedback.setText(ok ? "✔ Correct!" : "✘ Wrong — the answer is " + quiz.current().options.get(correct));
        feedback.setTextColor(ok ? 0xFF2E7D32 : 0xFFC62828);
        score.setText("Score: " + quiz.score());
        next.setEnabled(true);
    }
}
