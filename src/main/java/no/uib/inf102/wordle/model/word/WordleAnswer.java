package no.uib.inf102.wordle.model.word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import no.uib.inf102.wordle.model.Dictionary;

/**
 * This class represents an answer to a Wordle puzzle.
 * 
 * The answer must be one of the words in the LEGAL_WORDLE_LIST.
 */
public class WordleAnswer {

    private final String WORD;

    private Dictionary dictionary;

    private static Random random = new Random();

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     */
    public WordleAnswer(Dictionary dictionary) {
        this(random, dictionary);
    }

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     * using a specified random object.
     * This gives us the opportunity to set a seed so that tests are repeatable.
     */
    public WordleAnswer(Random random, Dictionary dictionary) {
        this(getRandomWordleAnswer(random, dictionary), dictionary);
    }

    /**
     * Creates a WordleAnswer object with a given word.
     * 
     * @param answer
     */
    public WordleAnswer(String answer, Dictionary dictionary) {
        this.WORD = answer.toLowerCase();
        this.dictionary = dictionary;
    }

    /**
     * Gets a random wordle answer
     * 
     * @param random
     * @return
     */
    private static String getRandomWordleAnswer(Random random, Dictionary dictionary) {
        List<String> possibleAnswerWords = dictionary.getAnswerWordsList();
        int randomIndex = random.nextInt(possibleAnswerWords.size());
        String newWord = possibleAnswerWords.get(randomIndex);
        return newWord;
    }

    /**
     * Guess the Wordle answer. Checks each character of the word guess and gives
     * feedback on which that is in correct position, wrong position and which is
     * not in the answer word.
     * This is done by updating the AnswerType of each WordleCharacter of the
     * WordleWord.
     * 
     * @param wordGuess
     * @return wordleWord with updated answertype for each character.
     */
    public WordleWord makeGuess(String wordGuess) {
        if (!dictionary.isLegalGuess(wordGuess))
            throw new IllegalArgumentException("The word '" + wordGuess + "' is not a legal guess");

        WordleWord guessFeedback = matchWord(wordGuess, WORD);
        return guessFeedback;
    }

    /**
     * Generates a WordleWord showing the match between the given guess and the
     * answer.
     * This method compares each letter of the guess with the corresponding letter
     * in the answer and generates feedback indicating whether each letter is
     * correct,
     * in the wrong position, or not in the answer at all.
     * 
     * @param guess  the guessed word
     * @param answer answer the correct word
     * @return a WordleWord containing the guess and an array of AnswerType for each
     *         letter
     * @throws IllegalArgumentException if the guess and answer have different
     */
    public static WordleWord matchWord(String guess, String answer) {
        int wordLength = answer.length();
        if (guess.length() != wordLength)
            throw new IllegalArgumentException("Guess and answer must have same number of letters but guess = " + guess
                    + " and answer = " + answer);

        AnswerType[] feedback = new AnswerType[wordLength];

        Map<Character, Integer> answerLetterCounts = new HashMap<>();
        for (int i = 0; i < wordLength; i++) {
            char letter = answer.charAt(i);
            answerLetterCounts.put(letter, answerLetterCounts.getOrDefault(letter, 0) + 1);
        }

        for (int i = 0; i < wordLength; i++) {
            char letter = guess.charAt(i);
            if (letter == answer.charAt(i)) {
                feedback[i] = AnswerType.CORRECT;
                answerLetterCounts.put(letter, answerLetterCounts.get(letter) - 1);
            }
        }

        for (int i = 0; i < wordLength; i++) {
            if (feedback[i] == null) {
                char letter = guess.charAt(i);
                if (answerLetterCounts.containsKey(letter) && answerLetterCounts.get(letter) > 0) {
                    feedback[i] = AnswerType.MISPLACED;
                    answerLetterCounts.put(letter, answerLetterCounts.get(letter) - 1);
                } else {
                    feedback[i] = AnswerType.WRONG;
                }
            }
        }

        return new WordleWord(guess, feedback);
    }

}
