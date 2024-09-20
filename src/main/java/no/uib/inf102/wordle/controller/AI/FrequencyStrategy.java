package no.uib.inf102.wordle.controller.AI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

/**
 * This strategy finds the word within the possible words which has the highest
 * expected
 * number of green matches.
 */
public class FrequencyStrategy implements IStrategy {

    private Dictionary dictionary;
    private WordleWordList guesses;
    private Map<Character, Integer>[] letterFrequency;

    public FrequencyStrategy(Dictionary dictionary) {
        this.dictionary = dictionary;
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        List<String> possibleAnswers = guesses.possibleAnswers(); // henter ut mulige svar
        calculateLetterFrequency(possibleAnswers); // beregner frekvensen av bokstaver i mulige svar

        String bestGuess = null; // beste gjetning
        int maxGreenCount = -1;

        for (String candidate : possibleAnswers) {
            int score = scoreWord(candidate);

            // Oppdater hvis dette er den beste kandidaten
            if (score > maxGreenCount) {
                maxGreenCount = score;
                bestGuess = candidate;
            }
        }
        return bestGuess;
    }

    private int scoreWord(String candidate) {
        int score = 0;
        for (int i = 0; i < candidate.length(); i++) {
            char letter = candidate.charAt(i);
            score += letterFrequency[i].getOrDefault(letter, 0);
        }
        return score;
    }

    private void calculateLetterFrequency(List<String> possibleAnswers) {
        int wordLength = possibleAnswers.get(0).length();
        letterFrequency = new HashMap[wordLength];
        for (int i = 0; i < wordLength; i++) {
            letterFrequency[i] = new HashMap<>();
        }

        for (String word : possibleAnswers) {
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                letterFrequency[i].put(letter, letterFrequency[i].getOrDefault(letter, 0) + 1);
            }
        }
    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);
    }
}