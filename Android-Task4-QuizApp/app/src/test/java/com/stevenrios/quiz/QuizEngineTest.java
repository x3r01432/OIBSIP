package com.stevenrios.quiz;

import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.Test;

public class QuizEngineTest {
    @Test public void bankHasAtLeastTenValidQuestions() {
        assertTrue(QuizEngine.bank().size() >= 10);
        for (Question q : QuizEngine.bank()) { assertEquals(4, q.options.size()); assertTrue(q.answer >= 0 && q.answer < 4); }
    }
    @Test public void shufflingKeepsCorrectAnswer() {
        for (Question q : QuizEngine.bank()) {
            String right = q.options.get(q.answer);
            for (int seed = 0; seed < 20; seed++) { Question s = q.shuffled(new Random(seed)); assertEquals(right, s.options.get(s.answer)); }
        }
    }
    @Test public void orderIsShuffled() {
        Set<String> firsts = new HashSet<>();
        for (int seed = 0; seed < 30; seed++) firsts.add(new QuizEngine(QuizEngine.bank(), new Random(seed)).current().text);
        assertTrue("different seeds should give different first questions", firsts.size() > 3);
    }
    @Test public void scoringAndFlow() {
        QuizEngine e = new QuizEngine(QuizEngine.bank(), new Random(1));
        assertFalse("cannot skip without answering", e.next());
        assertTrue(e.answer(e.current().answer));
        e.answer((e.current().answer + 1) % 4); // double-tap ignored
        assertEquals(1, e.score());
        int n = 1;
        while (e.next()) { e.answer((e.current().answer + 1) % 4); n++; }
        assertEquals(e.total(), n);
        assertTrue(e.isLast());
        assertEquals(1, e.score());
    }
    @Test public void verdicts() {
        assertEquals("Outstanding!", QuizEngine.verdict(12, 12));
        assertEquals("Keep practising!", QuizEngine.verdict(2, 12));
    }
}
