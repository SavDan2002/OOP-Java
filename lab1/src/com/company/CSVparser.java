package com.company;

import java.io.*;
import java.util.*;

public class CSVparser {
    Reader reader = null;
    Map<String, Integer> words = new TreeMap<>();
    int counter = 0;

    void putWord(String word) {
        if (words.containsKey(word)) {
            words.replace(word, words.get(word) + 1);
        } else {
            words.put(word, 1);
        }
        counter++;
    }

    public void parse(String fileName) {
        java.lang.StringBuilder word = new java.lang.StringBuilder();

        try {
            reader = new InputStreamReader(new FileInputStream(fileName));
            int letter;
            while ((letter = reader.read()) != -1) {
                if (Character.isLetterOrDigit(letter)) {
                    word.append((char) letter);
                } else if (word.length() != 0) {
                    putWord(word.toString());
                    word.delete(0, word.length());
                }
            }
            if (word.length() != 0) {
                putWord(word.toString());
                word.delete(0, word.length());
            }

            List<Map.Entry<String, Integer>> wordsList = new LinkedList<>(words.entrySet());
            wordsList.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

            Writer writer = new OutputStreamWriter(new FileOutputStream("F:\\User\\Java OOP\\lab1\\src\\com\\company\\output.csv"));

            double percent;
            for (var i : wordsList) {
                percent = (double) i.getValue() / counter * 100;
                writer.write(i.getKey() + ", " + i.getValue() + ", " + percent + "%;\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
