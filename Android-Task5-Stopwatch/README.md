# Stopwatch — Oasis Infobyte Android Development (Android-Task5-Stopwatch)

HH:MM:SS.cc display refreshed by a main-looper `Handler`. Time is computed from `SystemClock.elapsedRealtime()` so it never drifts. Start/Stop/Resume button changes label and colour; Lap only enabled while running, Reset only while paused. Laps list shows lap duration and split. `onPause` removes Handler callbacks, `onResume` re-posts them, and state survives rotation via `onSaveInstanceState`.

## Tech stack
Java 17 · Android SDK 34 (minSdk 24) · XML layouts · Gradle 8.7 / AGP 8.5.2 · JUnit 4. No third-party runtime libraries.

## Project layout
```
app/src/main/java/...   Activities + pure-Java logic class
app/src/main/res/       layouts, drawables, strings, themes
app/src/test/java/...   JVM unit tests for the logic
apk/                    debug APK built from this source
output/                 build log + unit-test results
```

## Build & run
Open the folder in Android Studio (it will create `local.properties`) and press Run, or:
```
./gradlew testDebugUnitTest assembleDebug
adb install apk/*.apk
```

## Verification
- `assembleDebug` succeeds → `apk/` (see `output/build_log.txt`).
- 5 JVM unit tests pass → `output/unit_test_results.txt`.
- No on-device screenshots: the Android emulator could not boot on the build machine, so the UI was not exercised on a device. Install the APK on a phone or emulator to see it.
