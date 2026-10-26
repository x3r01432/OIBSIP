# Unit Converter — Oasis Infobyte Android Development (Android-Task1-UnitConverter)

Converts values across 5 categories (Length, Weight, Temperature, Volume, Speed) using a category Spinner plus From/To Spinners, with a swap button. Empty or non-numeric input shows a Toast. All conversion maths lives in `UnitConverter.java` (factor-to-base-unit; temperature goes through Kelvin).

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
- 7 JVM unit tests pass → `output/unit_test_results.txt`.
- No on-device screenshots: the Android emulator could not boot on the build machine, so the UI was not exercised on a device. Install the APK on a phone or emulator to see it.
