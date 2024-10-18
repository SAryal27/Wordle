/*
 * File: Wordle.java
 * -----------------
 * This module is the starter file for the Wordle assignment.
 * BE SURE TO UPDATE THIS COMMENT WHEN YOU COMPLETE THE CODE.
 */

import edu.willamette.cs1.wordle.WordleDictionary;
import edu.willamette.cs1.wordle.WordleGWindow;

import java.awt.*;
import java.util.ArrayList;

public class Wordle {
    /* Private instance variables */

    private WordleGWindow gw;
    private String[] wordArray;
    private String[] answerArray;
    private String answer1;
    private String answer2;
    private String answerS;
    private String answer;

    private int rowNum;
    private ArrayList<String> answerUnique;
    private ArrayList<Integer> answerNum;


    public void run() {

        int count = 0;
        answerUnique = new ArrayList<>();
        rowNum = 0;
        answerArray = new String[5];
        wordArray = new String[5];
        gw = new WordleGWindow();
        int rand = (int) (Math.random() * (WordleDictionary.FIVE_LETTER_WORDS.length));
        answerS = WordleDictionary.FIVE_LETTER_WORDS[rand];
        while (!answerS.substring(4, 5).equals("s")) {
            rand = (int) (Math.random() * (WordleDictionary.FIVE_LETTER_WORDS.length));
            answerS = WordleDictionary.FIVE_LETTER_WORDS[rand];
        }
        rand = (int) (Math.random() * (WordleDictionary.FIVE_LETTER_WORDS.length));
        answer1 = WordleDictionary.FIVE_LETTER_WORDS[rand];
        rand = (int) (Math.random() * (WordleDictionary.FIVE_LETTER_WORDS.length));
        answer2 = WordleDictionary.FIVE_LETTER_WORDS[rand];
        while (answer1.substring(4, 5).equals("s")) {
            rand = (int) (Math.random() * (WordleDictionary.FIVE_LETTER_WORDS.length));
            answer1 = WordleDictionary.FIVE_LETTER_WORDS[rand];
        }

        while (answer2.substring(4, 5).equals("s")) {
            rand = (int) (Math.random() * (WordleDictionary.FIVE_LETTER_WORDS.length));
            answer2 = WordleDictionary.FIVE_LETTER_WORDS[rand];
        }
        int randChoice = (int) (Math.random() * 2 + 1);
        if (randChoice == 1) {
            answer = answerS;
        }
        if (randChoice == 2) {
            answer = answer1;
        }
        if (randChoice == 3) {
            answer = answer2;
        }


        answerUnique.add(answer.substring(0, 1));
        for (int i = 1; i < 5; i++) {
            for (int j = i - 1; j >= 0; j--) { // NEED TO FIX, WORKS WITH GLASS AND PESPS, NOT PEEPS
                if ((answer.substring(i, i + 1).equals(answer.substring(j, j + 1)))) {
                    count++;
                }
            }
            if ((count == 0)) {
                answerUnique.add(answer.substring(i, i + 1));
            }
        }
        if (!(answer.substring(0, 4).contains(answer.substring(4, 5)))) {
            answerUnique.add(answer.substring(4, 5));
        }
        answerNum = new ArrayList<>();
        for (int a = 0; a < answerUnique.size(); a++) {
            int letterCount = 0;
            for (int b = 0; b < 5; b++) {
                if (answerUnique.get(a).equals(answer.substring(b, b + 1))) {
                    letterCount += 1;
                }
            }
            answerNum.add(letterCount);
        }

        gw.addEnterListener((s) -> enterAction(s));
        System.out.println(answerUnique);
        System.out.println(answerUnique.size());
        System.out.println(answerNum);
        System.out.println(answerNum.size());

    }

    /*
     * Called when the user hits the RETURN key or clicks the ENTER button,
     * passing in the string of characters on the current row.
     */

    public void enterAction(String s) { //submit word
        s = s.toLowerCase();
        int count = 0;
        for (int i = 0; i < WordleDictionary.FIVE_LETTER_WORDS.length; i++) {
            if (s.equals(WordleDictionary.FIVE_LETTER_WORDS[i])) {
                count += 1;
            }
        }


        if (count == 0) {
            gw.showMessage("Not in word list");
        } else {
            for (int x = 0; x < 5; x++) {
                gw.setSquareColor(rowNum, x, WordleGWindow.MISSING_COLOR);
            }
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (s.substring(i, i + 1).equals(answer.substring(j, j + 1)) && i != j) {
                        if (!gw.getSquareColor(rowNum, i).equals(WordleGWindow.CORRECT_COLOR)) {
                            gw.setSquareColor(rowNum, i, WordleGWindow.PRESENT_COLOR);
                        }
                        if (!gw.getKeyColor(s.substring(i, i + 1).toUpperCase()).equals(WordleGWindow.CORRECT_COLOR)) {
                            gw.setKeyColor(s.substring(i, i + 1).toUpperCase(), WordleGWindow.PRESENT_COLOR);
                        }
                    }
                    if (s.substring(i, i + 1).equals(answer.substring(j, j + 1)) && i == j) {
                        gw.setSquareColor(rowNum, i, WordleGWindow.CORRECT_COLOR);
                        gw.setKeyColor(s.substring(i, i + 1).toUpperCase(), WordleGWindow.CORRECT_COLOR);
                    }
                }
            }
            for (int p = 0; p < 5; p++) {
                if (gw.getSquareColor(rowNum, p).equals(WordleGWindow.MISSING_COLOR)) {
                    gw.setKeyColor(s.substring(p, p + 1).toUpperCase(), WordleGWindow.MISSING_COLOR);
                }
            }
            for (int a = 0; a < answerUnique.size(); a++) {
                int colorCount = 0;
                for (int b = 0; b < 5; b++) {
                    if (answerUnique.get(a).equals(s.substring(b, b + 1))) {
                        colorCount += 1;
                    }
                }
                if (colorCount > answerNum.get(a)) {
                    int diff = colorCount - answerNum.get(a);
                    for (int i = 4; i >= 0; i--) {
                        if (diff > 0) {
                            if (s.substring(i, i + 1).equals(answerUnique.get(a)) && !(gw.getSquareColor(rowNum, i).equals(WordleGWindow.CORRECT_COLOR))) {
                                gw.setSquareColor(rowNum, i, WordleGWindow.MISSING_COLOR);
                                diff--;
                            }
                        }
                    }
                }
            }
            if (s.equals(answer)) {
                gw.showMessage("Wow, you guessed the word");
                gw.setSquareColor(rowNum, 0, Color.RED);
                gw.setSquareColor(rowNum, 1, Color.ORANGE);
                gw.setSquareColor(rowNum, 2, Color.YELLOW);
                gw.setSquareColor(rowNum, 3, Color.GREEN);
                gw.setSquareColor(rowNum, 4, Color.BLUE);


            } else {
                if (gw.getCurrentRow() == 5) {
                    gw.showMessage("The answer was " + answer);
                } else {
                    gw.setCurrentRow(rowNum + 1);
                    rowNum += 1;
                }

            }
        }

    }


    //make it so keys do not change back once they are green
    /* Startup code */

    public static void main(String[] args) {// main code
        new Wordle().run();
    }

}

// if the word is in wordList(from dictionary)
// then check each index of the word and compare it to each index of the dictionary word
// if the word is not in wordList (the dictonary) return "not in wordList"
// once you compare each index, return if the letter in each square starting from 0-4 should be gray(not a letter), green(correct position),
// or yellow (correct letter, wrong position)
// color the keyboard to eliminate used words that the user already typed as you go from row to row
// check the next word that is returned into row 1 and repeat the same process
// once you get to the end,if the user hasn't gotten the word, return "the correct word is: ____"
// if at any point the user has guessed the correct word at each index/position of a row, change the whole word to green, and
// return their guess distribution

// smaller details:
// whenever the user clicks "ENTER" or presses ENTER on keyboard, pass their 5 letter word as a String
// use get.squareColor(row, col, color) to color a square
// SPECIAL CASE: if a the hidden word and the guess contians multiple copies of the same letter
// for example, if the word was GLASS and the user guessed SASSY, the first S would be yellow, the 3rd S in
// SASSY would be green because it is in the right spot, BUT the 2nd S in SASSY would be gray because there isnt
// an S in that spot for the actual word (Glass) and there isnt 3 S's in the word (Glass)


// MileStone #1: pick a random word and display it in the first row of boxes
// - WordleGWindow class does not export any mehtod for displaying an entire word
// - method setSquareLetter(row, col, letter) puts one letter in a box identified by its row and column numbers
// - use WordleGWindow.N_Rows and WordleGWindow.N_Cols whenever your code needs to know how many rows and cols exist

// MileStone #2: Check whether the letters entered by the user form a word


// MileStone #3: Color the boxes


// MileStone #4: Color the keys

