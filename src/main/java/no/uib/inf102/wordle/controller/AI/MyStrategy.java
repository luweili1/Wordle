package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

        List<Map<Character, Integer>> letterFrequency = initializeLetterFrequency();

        int bestGreenMatches = 0;
        String bestWord = "";

        for (String word : guesses.possibleAnswers()) {
            int greenMatches = 0;
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                greenMatches += letterFrequency.get(i).getOrDefault(letter, 0);
            }
            if (greenMatches > bestGreenMatches) {
                bestGreenMatches = greenMatches;
                bestWord = word;
            }
        }

        return bestWord;
    }

    /**
     * Initializes the letter frequency for each position in the word.
     * This method creates a list of maps, where each map corresponds to a character
     * position
     * in the word and contains the frequency of each letter appearing in that
     * position
     * across all possible words.
     * 
     * @return A list of maps where each map contains the frequency of each letter
     *         in the corresponding position.
     */
    private List<Map<Character, Integer>> initializeLetterFrequency() {
        List<Map<Character, Integer>> letterFrequency = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            letterFrequency.add(new HashMap<>());
        }

        for (String word : guesses.possibleAnswers()) {
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                letterFrequency.get(i).put(letter, letterFrequency.get(i).getOrDefault(letter, 0) + 1);
            }
        }

        return letterFrequency;
    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);
    }
}