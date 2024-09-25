package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

/**
 * This strategy finds the word within the possible words which has the highest
 * expected number of green matches.
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

        List<Map<Character, Integer>> letterFrequency = calculateLetterFrequency(guesses.possibleAnswers());

        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }

        int bestGreenMatches = 0;
        String bestWord = "";

        for (String word : guesses.possibleAnswers()) {
            int greenMatches = 0;
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                greenMatches += letterFrequency.get(i).get(letter);
            }
            if (greenMatches > bestGreenMatches) {
                bestGreenMatches = greenMatches;
                bestWord = word;
            }
        }
        return bestWord;

    }

    private List<Map<Character, Integer>> calculateLetterFrequency(List<String> words) {
        Map<Character, Integer> firstIndex = new HashMap<>();
        Map<Character, Integer> secondIndex = new HashMap<>();
        Map<Character, Integer> thirdIndex = new HashMap<>();
        Map<Character, Integer> fourthIndex = new HashMap<>();
        Map<Character, Integer> fifthIndex = new HashMap<>();

        List<Map<Character, Integer>> indexList = new ArrayList<>();
        indexList.add(firstIndex);
        indexList.add(secondIndex);
        indexList.add(thirdIndex);
        indexList.add(fourthIndex);
        indexList.add(fifthIndex);

        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                if (indexList.get(i).containsKey(letter)) {
                    indexList.get(i).put(letter, indexList.get(i).get(letter) + 1);
                } else {
                    indexList.get(i).put(letter, 1);
                }
            }
        }
        return indexList;
    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);
    }
}