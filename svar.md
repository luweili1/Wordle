# Runtime Analysis
For each method of the tasks give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented new methods not listed you must add these as well, e.g. any helper methods. You need to show how you analyzed any methods used by the methods listed below.**

The runtime should be expressed using these three parameters:
   * `n` - number of words in the list allWords
   * `m` - number of words in the list possibleWords
   * `k` - number of letters in the wordleWords


## Task 1 - matchWord
* `WordleAnswer::matchWord`: O(k)
    * In total, the method has three steps: 
    1) For loop iterate k times: The loop runs once for each character in the asnwer. For each character, the hashmap is updated. The runtime for this step is O(k).

    2) The for loop iterates k times: The loop runs once for each character in the guess. For each character, it is compared to the corresponding character in the answer, and the feedback array and answerLetterCounts map are updated. The runtime for this step is O(k).

    3) The loop runs once for each character in the guess. For each character that is not marked as CORRECT, it checks if the character is in answerLetterCounts and updates the feedback array and answerLetterCounts map. The runtime for this step is O(k).

    The total runtime for matchWord method is therefore O(k). 

## Task 2 - EliminateStrategy
* `WordleWordList::eliminateWords`: O(m*k)
    * This method eliminates words from possibleAnswers list based on feedback from the previous guess. 

    1) Creating new list newPossibleAnswer takes O(1) time. 

    2) Loop through possibleAnswer iterates over m words in the list. For each word, isPossibleWord is called and it compares the word (length k) with feedback, which takes O(k) time. Since this is done for each m words, it takes O(m*k) time in total. 

    3) Adding a word to newPossibleAnswer takes O(1) times. 

    4) Updating possibleAnwers takes O(1) time. 

    Total runtime is therefore O(m*k)

## Task 3 - FrequencyStrategy
* `FrequencyStrategy::makeGuess`: O(m*k)
    * This method checks if it is a feedbak from previous guess and elimate word based on this. 

    1) Eliminating words: as analyzed above, this steps takes O(m*k) time. 

    2) A frequeency table for remaining words in a dictonary is constructed, which has m words. For each word of length k, it tallies the occurrences of each letter, which takes O(k) time. Therefor, constructing the frequency table has a runtime of O(m*k)

    3) Finding the best word: finBestWord(frequencyTable) selects the word with the hightst score based on the frequency table. Takes O(m) times. 

    Since the dominating factor is the contruction of the frequency table, the total runtime is O(m*k). 


# Task 4 - Make your own (better) AI
MyStrategy prioritizes words with a high probability of including correct letters from the remaining possibilities. It avoids selecting the same word twice to maximize the value of each guess. Words are scored based on the frequency of their letters within the potential solutions.

Initial Word List:
MyStrategy begins with a full list of possible words from the dictionary. This list is maintained and updated using a WordleWordList object, which helps manage the remaining valid guesses as feedback is received after each guess.

Guessing and Feedback:
For each round of guessing, feedback from the previous guess is used to eliminate words that no longer match the possible answer based on the feedback.

Choosing the Best Word:
Once the remaining words are updated, the strategy calculates the frequency of each letter across all possible answers. The algorithm then iterates through each word in the remaining word list, assigning a score to each word based on the sum of its letters' frequencies. The word with the highest total frequency is chosen as the next guess.

The strategy avoids counting duplicate letters more than once when calculating a word's score to avoid over-representing any single letter. This ensures that words with diverse letters are favored, as they provide more information about the potential answer.

Narrowing Down: 
The process of receiving feedback helps further narrow the pool of possible answers. After each guess, the list of possible words is reduced, and the frequency-based selection process is repeated until the correct word is found.

