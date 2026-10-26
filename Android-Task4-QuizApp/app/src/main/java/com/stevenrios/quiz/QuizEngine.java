package com.stevenrios.quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Quiz state: shuffled questions, current index, score. Pure Java so it is unit-testable. */
public class QuizEngine {
    private final List<Question> questions;
    private int index = 0, score = 0;
    private boolean answered = false;

    public QuizEngine(List<Question> bank, Random r) {
        questions = new ArrayList<>();
        for (Question q : bank) questions.add(q.shuffled(r));
        Collections.shuffle(questions, r);
    }

    public Question current() { return questions.get(index); }
    public int index() { return index; }
    public int total() { return questions.size(); }
    public int score() { return score; }
    public boolean isAnswered() { return answered; }
    public boolean isLast() { return index == questions.size() - 1; }

    /** @return true if correct. Second answer to the same question is ignored (returns previous result). */
    public boolean answer(int choice) {
        boolean correct = choice == current().answer;
        if (!answered) { answered = true; if (correct) score++; }
        return correct;
    }

    public boolean next() {
        if (!answered || isLast()) return false;
        index++; answered = false; return true;
    }

    public static String verdict(int score, int total) {
        double p = 100.0 * score / total;
        if (p >= 90) return "Outstanding!"; if (p >= 70) return "Great job!"; if (p >= 50) return "Not bad!"; return "Keep practising!";
    }

    public static List<Question> bank() {
        return Arrays.asList(
            q("Which language is the official first language for Android development before Kotlin?", 0, "Java", "Swift", "C#", "Ruby"),
            q("What does SQL stand for?", 1, "Simple Query Logic", "Structured Query Language", "Sequential Query Language", "System Query Layer"),
            q("Which data structure works on a LIFO basis?", 2, "Queue", "Tree", "Stack", "Graph"),
            q("What is the time complexity of binary search?", 0, "O(log n)", "O(n)", "O(n log n)", "O(1)"),
            q("Which HTTP status code means 'Not Found'?", 3, "200", "301", "500", "404"),
            q("Which company created the Android OS (before Google bought it)?", 1, "Apple", "Android Inc.", "Nokia", "Samsung"),
            q("What does CPU stand for?", 0, "Central Processing Unit", "Computer Personal Unit", "Central Program Utility", "Core Processing Unit"),
            q("Which protocol secures web traffic?", 2, "FTP", "SMTP", "HTTPS", "Telnet"),
            q("In Git, which command uploads local commits to a remote?", 3, "git pull", "git commit", "git clone", "git push"),
            q("What is 1 byte equal to?", 1, "4 bits", "8 bits", "16 bits", "1024 bits"),
            q("Which Android component displays a single screen with a UI?", 0, "Activity", "Service", "BroadcastReceiver", "ContentProvider"),
            q("Which of these is NOT a programming language?", 2, "Python", "Go", "HTML", "Rust")
        );
    }
    private static Question q(String t, int a, String... o) { return new Question(t, Arrays.asList(o), a); }
}
