# Java Development — Task 4: Online Examination System

Swing GUI exam system with login, profile update, timed MCQs, navigation, and results.

## Features
- Login screen (default `student`/`pass`)
- Profile update: change display name and password before starting
- Exam screen: one MCQ at a time with 4 radio-button options
- Next / Previous navigation (answers remembered)
- Always-visible countdown timer (`javax.swing.Timer`); auto-submits at zero
- Manual Submit with confirmation dialog
- Result screen: score (X/Y), time taken, correct/incorrect breakdown
- Window-close during exam → "Are you sure you want to quit?"
- Logout on result screen returns to login

## Tech Stack
Java, Swing (`ButtonGroup`, `JRadioButton`, `javax.swing.Timer`).

## Run
```bash
javac OnlineExaminationSystem.java
java OnlineExaminationSystem
```

## Note
Compiled/verified on the build machine; the GUI requires a desktop display, not available
on the headless build box.

Author: Steven Manuel Rios
