package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

/**
 * This strategy finds the word within the possible words which has the highest
 * expected number of green matches.
 * 
 */
public class MyStrategy implements IStrategy {

    private Dictionary dictionary;
    private WordleWordList guesses;

    public MyStrategy(Dictionary dictionary) {
        this.dictionary = dictionary;
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }

        return chooseBestWord();
    }

    private String chooseBestWord() {
        List<String> possibleAnswers = guesses.possibleAnswers();
        Map<Character, Integer> letterFrequencies = calculateLetterFrequencies(possibleAnswers);

        String bestWord = "";
        int bestScore = -1;

        for (String word : possibleAnswers) {
            int score = scoreWord(word, letterFrequencies);
            if (score > bestScore) {
                bestScore = score;
                bestWord = word;
            }
        }

        return bestWord;
    }

    private Map<Character, Integer> calculateLetterFrequencies(List<String> possibleAnswers) {
        Map<Character, Integer> frequencies = new HashMap<>();

        for (String word : possibleAnswers) {
            for (char letter : word.toCharArray()) {
                frequencies.put(letter, frequencies.getOrDefault(letter, 0) + 1);
            }
        }

        return frequencies;
    }

    private int scoreWord(String word, Map<Character, Integer> letterFrequencies) {
        int score = 0;
        List<Character> usedLetters = new ArrayList<>();

        for (char letter : word.toCharArray()) {
            if (!usedLetters.contains(letter)) {
                score += letterFrequencies.getOrDefault(letter, 0);
                usedLetters.add(letter);
            }
        }

        return score;
    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);
    }
}