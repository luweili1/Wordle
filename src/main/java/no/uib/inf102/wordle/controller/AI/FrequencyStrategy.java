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
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }
        List<HashMap<Character, Integer>> frequencyTable = makeFrequencyTable(dictionary);
        String currentBestWord = findBestWord(frequencyTable);
        return currentBestWord;
    }

    private String findBestWord(List<HashMap<Character, Integer>> frequencyTable) {
        String bestWord = "";
        int bestWordScore = 0;
        for (String word : guesses.possibleAnswers()) {
            int score = 0;
            for (int i = 0; i < word.length(); i++) {
                score += frequencyTable.get(i).getOrDefault(word.charAt(i), 0);
            }
            if (score > bestWordScore) {
                bestWord = word;
                bestWordScore = score;
            }
        }
        return bestWord;
    }

    private List<HashMap<Character, Integer>> makeFrequencyTable(Dictionary dictionary) {
        List<String> guessWordList = guesses.possibleAnswers();
        List<HashMap<Character, Integer>> frequencyTable = new ArrayList<>();
        for (int i = 0; i < guessWordList.get(0).length(); i++) {
            frequencyTable.add(new HashMap<>());
        }
        for (int i = 0; i < guessWordList.get(0).length(); i++) {
            for (String word : guessWordList) {
                frequencyTable.get(i).put(word.charAt(i), frequencyTable.get(i).getOrDefault(word.charAt(i), 0) + 1);
            }
        }
        return frequencyTable;
    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);
    }
}