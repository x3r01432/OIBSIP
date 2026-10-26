package com.stevenrios.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class Question {
    public final String text; public final List<String> options; public final int answer;
    public Question(String text, List<String> options, int answer) { this.text = text; this.options = options; this.answer = answer; }

    /** Same question with its options shuffled; the answer index follows the correct option. */
    public Question shuffled(Random r) {
        List<String> o = new ArrayList<>(options);
        String correct = o.get(answer);
        Collections.shuffle(o, r);
        return new Question(text, o, o.indexOf(correct));
    }
}
