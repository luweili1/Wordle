package no.uib.inf102.wordle.controller.AI;

import java.util.List;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class MyStrategy implements IStrategy {

    private WordleWordList guesses;
    private Dictionary dictionary;

    public MyStrategy(Dictionary dictionary) {
        this.dictionary = dictionary;
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        List<String> possibleAnswers = guesses.possibleAnswers();

        // If this is the first guess, pick a word with unique, common letters
        if (feedback == null) {
            return "aurei"; // A balanced word with common vowels and no repeated letters
        }

        // Update the possible answers based on the feedback from the previous guess
        guesses.updateWithFeedback(feedback);

        // Step 1: If many letters are unknown, guess a word that maximizes letter
        // coverage
        String bestGuess = pickMaxCoverageWord(possibleAnswers);

        // Step 2: If possibleAnswers list is small, focus on making more precise
        // guesses
        if (possibleAnswers.size() < 10) {
            bestGuess = pickMostLikelyWord(possibleAnswers);
        }

        return bestGuess;
    }

    private String pickMostLikelyWord(List<String> possibleAnswers) {
        return possibleAnswers.get(0);

    }

    private String pickMaxCoverageWord(List<String> possibleAnswers) {
        String bestWord = null;
        int maxCoverage = 0;

        // Iterate through the dictionary to find words that cover the most unique
        // letters
        for (String candidate : dictionary.getAllWords()) {
            int uniqueLetters = countUniqueLetters(candidate);

            // Prioritize words that have the most new unique letters
            if (uniqueLetters > maxCoverage) {
                maxCoverage = uniqueLetters;
                bestWord = candidate;
            }
        }
        return bestWord;
    }

    private int countUniqueLetters(String candidate) {
        String word;
        return (int) word.chars().distinct().count();

    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);

    }

}
