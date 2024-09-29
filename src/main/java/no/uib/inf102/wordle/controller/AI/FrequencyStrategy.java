package no.uib.inf102.wordle.controller.AI;

import java.util.HashMap;
import java.util.Map;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

/**
 * This strategy finds the word within the possible words which has the highest
 * expected number of green matches. It does this by calculating the frequency
 * of each letter in each position of the possible words, and then scoring each
 * word based on the sum of the frequencies of the letters in the word.
 */
public class FrequencyStrategy implements IStrategy {

    private Dictionary dictionary;
    private WordleWordList guesses;

    public FrequencyStrategy(Dictionary dictionary) {
        this.dictionary = dictionary;
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {

        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }

        Map<Integer, Map<Character, Integer>> letterFrequencies = calculateLetterFrequencies();

        String bestGuess = "";
        int bestScore = -1;

        for (String word : guesses.possibleAnswers()) {
            int score = scoreWord(word, letterFrequencies);
            if (score > bestScore) {
                bestScore = score;
                bestGuess = word;
            }
        }

        return bestGuess;
    }

    private Map<Integer, Map<Character, Integer>> calculateLetterFrequencies() {
        Map<Integer, Map<Character, Integer>> frequencies = new HashMap<>();

        int wordLength = guesses.wordLength();
        for (int i = 0; i < wordLength; i++) {
            frequencies.put(i, new HashMap<>());
        }

        for (String word : guesses.possibleAnswers()) {
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                frequencies.get(i).put(letter, frequencies.get(i).getOrDefault(letter, 0) + 1);
            }
        }

        return frequencies;
    }

    private int scoreWord(String word, Map<Integer, Map<Character, Integer>> letterFrequencies) {
        int score = 0;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            score += letterFrequencies.get(i).getOrDefault(letter, 0);
        }

        return score;
    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);
    }
}