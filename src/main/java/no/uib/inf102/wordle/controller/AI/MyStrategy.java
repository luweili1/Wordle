package no.uib.inf102.wordle.controller.AI;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class MyStrategy implements IStrategy {

    private Dictionary dictionary;
    private WordleWordList possibleAnswers;

    public MyStrategy(Dictionary dictionary) {
        this.dictionary = dictionary;
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            possibleAnswers.eliminateWords(feedback);
        }
        String bestWord = findMostInformativeWord();
        return bestWord;
    }

    private String findMostInformativeWord() {
        String bestWord = "";
        double bestScore = -1;
        for (String word : possibleAnswers.possibleAnswers()) {
            double score = calculateInformationGain(word);
            if (score > bestScore) {
                bestWord = word;
                bestScore = score;
            }
        }
        return bestWord;
    }

    private double calculateInformationGain(String word) {
        double score = 0;
        Map<String, Integer> bucket = new HashMap<>();
        for (String possible : possibleAnswers.possibleAnswers()) {
            String pattern = getPattern(word, possible);
            bucket.put(pattern, bucket.getOrDefault(pattern, 0) + 1);
        }
        for (int count : bucket.values()) {
            double p = (double) count / possibleAnswers.possibleAnswers().size();
            score -= p * Math.log(p);
        }
        return score;
    }

    private String getPattern(String guess, String actual) {
        StringBuilder pattern = new StringBuilder(".....");
        // Computes pattern logic based on feedback rules:
        // 'G' (Green) for correct letter and position
        // 'Y' (Yellow) for correct letter
        // '.' for incorrect letter
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == actual.charAt(i)) {
                pattern.setCharAt(i, 'G');
            } else if (actual.contains(String.valueOf(guess.charAt(i)))) {
                pattern.setCharAt(i, 'Y');
            }
        }
        return pattern.toString();
    }

    @Override
    public void reset() {
        possibleAnswers = new WordleWordList(dictionary);
    }
}