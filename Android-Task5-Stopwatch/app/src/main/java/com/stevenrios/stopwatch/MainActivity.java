package com.stevenrios.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final long TICK_MS = 30;
    private final StopwatchModel sw = new StopwatchModel();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView time;
    private Button startStop, lap, reset;
    private ArrayAdapter<String> lapAdapter;
    private final List<String> lapRows = new ArrayList<>();

    // Handler-driven UI refresh; the time itself comes from the model, so a late tick never loses time.
    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            time.setText(StopwatchModel.format(sw.elapsed(SystemClock.elapsedRealtime())));
            if (sw.isRunning()) handler.postDelayed(this, TICK_MS);
        }
    };

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        time = findViewById(R.id.textTime);
        startStop = findViewById(R.id.btnStartStop); lap = findViewById(R.id.btnLap); reset = findViewById(R.id.btnReset);
        ListView list = findViewById(R.id.listLaps);
        lapAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lapRows);
        list.setAdapter(lapAdapter);
        if (b != null && b.getLongArray("sw") != null) sw.restore(b.getLongArray("sw"));

        startStop.setOnClickListener(v -> {
            long now = SystemClock.elapsedRealtime();
            if (sw.isRunning()) sw.stop(now); else sw.start(now);
            updateUi();
        });
        lap.setOnClickListener(v -> { sw.lap(SystemClock.elapsedRealtime()); updateLaps(); });
        reset.setOnClickListener(v -> { sw.reset(); updateUi(); });
        updateUi();
    }

    private void updateUi() {
        boolean running = sw.isRunning();
        long now = SystemClock.elapsedRealtime();
        startStop.setText(running ? "Stop" : (sw.hasTime(now) ? "Resume" : "Start"));
        startStop.setBackgroundTintList(android.content.res.ColorStateList.valueOf(running ? 0xFFE53935 : 0xFF43A047));
        lap.setEnabled(running);                       // laps only while running
        reset.setEnabled(!running && sw.hasTime(now)); // reset only when paused with time on the clock
        handler.removeCallbacks(ticker);
        handler.post(ticker);
        updateLaps();
    }

    private void updateLaps() {
        lapRows.clear();
        List<Long> laps = sw.laps();
        for (int i = 0; i < laps.size(); i++)
            lapRows.add(String.format("Lap %d    +%s    %s", laps.size() - i, StopwatchModel.format(sw.lapDuration(i)), StopwatchModel.format(laps.get(i))));
        lapAdapter.notifyDataSetChanged();
    }

    // Lifecycle: stop UI callbacks while invisible (saves battery, avoids leaks); time keeps counting in the model.
    @Override protected void onPause() { super.onPause(); handler.removeCallbacks(ticker); }
    @Override protected void onResume() { super.onResume(); handler.removeCallbacks(ticker); handler.post(ticker); }
    @Override protected void onSaveInstanceState(Bundle out) { super.onSaveInstanceState(out); out.putLongArray("sw", sw.snapshot(SystemClock.elapsedRealtime())); }
    @Override protected void onDestroy() { handler.removeCallbacksAndMessages(null); super.onDestroy(); }
}
