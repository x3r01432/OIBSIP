package com.stevenrios.stopwatch;

import static org.junit.Assert.*;
import org.junit.Test;

public class StopwatchModelTest {
    @Test public void format() {
        assertEquals("00:00:00.00", StopwatchModel.format(0));
        assertEquals("00:01:05.43", StopwatchModel.format(65_432));
        assertEquals("01:00:00.00", StopwatchModel.format(3_600_000));
    }
    @Test public void startStopResume() {
        StopwatchModel s = new StopwatchModel();
        s.start(1000); assertEquals(500, s.elapsed(1500));
        s.stop(2000); assertEquals(1000, s.elapsed(9999)); // frozen while stopped
        s.start(10_000); assertEquals(1250, s.elapsed(10_250));
        s.start(10_100); assertEquals(1250, s.elapsed(10_250)); // double start ignored
    }
    @Test public void laps() {
        StopwatchModel s = new StopwatchModel();
        s.lap(0); assertTrue("no lap while stopped", s.laps().isEmpty());
        s.start(0); s.lap(1000); s.lap(3500);
        assertEquals(2, s.laps().size());
        assertEquals(3500L, (long) s.laps().get(0));
        assertEquals(2500, s.lapDuration(0));
        assertEquals(1000, s.lapDuration(1));
    }
    @Test public void reset() {
        StopwatchModel s = new StopwatchModel();
        s.start(0); s.lap(10); s.stop(100); s.reset();
        assertEquals(0, s.elapsed(500)); assertFalse(s.isRunning()); assertTrue(s.laps().isEmpty());
    }
    @Test public void snapshotSurvivesRecreation() {
        StopwatchModel a = new StopwatchModel();
        a.start(0); a.lap(400);
        long[] snap = a.snapshot(1000);
        StopwatchModel b = new StopwatchModel(); b.restore(snap);
        assertTrue(b.isRunning());
        assertEquals(1500, b.elapsed(1500));
        assertEquals(1, b.laps().size());
    }
}
