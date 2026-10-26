package com.stevenrios.stopwatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Time-keeping logic, independent of Android. Elapsed time is computed from a monotonic clock
 * (SystemClock.elapsedRealtime on device) instead of counting Handler ticks, so it never drifts
 * and stays correct while the app is paused in the background.
 */
public class StopwatchModel {
    private long accumulated = 0, startedAt = 0;
    private boolean running = false;
    private final List<Long> laps = new ArrayList<>(); // lap split times (total elapsed at lap)

    public void start(long now) { if (!running) { startedAt = now; running = true; } }
    public void stop(long now) { if (running) { accumulated += now - startedAt; running = false; } }
    public void reset() { accumulated = 0; running = false; laps.clear(); }
    public boolean isRunning() { return running; }
    public long elapsed(long now) { return accumulated + (running ? now - startedAt : 0); }
    public boolean hasTime(long now) { return elapsed(now) > 0; }

    public void lap(long now) { if (running) laps.add(0, elapsed(now)); } // newest first
    public List<Long> laps() { return laps; }

    /** Duration of lap i (newest-first index), i.e. split minus previous split. */
    public long lapDuration(int i) { return laps.get(i) - (i + 1 < laps.size() ? laps.get(i + 1) : 0); }

    /** HH:MM:SS.cc */
    public static String format(long ms) {
        long cs = (ms / 10) % 100, s = (ms / 1000) % 60, m = (ms / 60000) % 60, h = ms / 3600000;
        return String.format(java.util.Locale.US, "%02d:%02d:%02d.%02d", h, m, s, cs);
    }

    // --- state save/restore (onSaveInstanceState / rotation) ---
    public long[] snapshot(long now) {
        long[] a = new long[3 + laps.size()];
        a[0] = elapsed(now); a[1] = running ? 1 : 0; a[2] = now;
        for (int i = 0; i < laps.size(); i++) a[3 + i] = laps.get(i);
        return a;
    }
    public void restore(long[] a) {
        accumulated = a[0]; running = a[1] == 1; startedAt = a[2];
        laps.clear(); for (int i = 3; i < a.length; i++) laps.add(a[i]);
    }
}
