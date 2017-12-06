package com.jblearning.puzzle;

import java.util.Random;

class Puzzle {
    private static final int NUMBER_PARTS = 5;
    private String[] parts;
    private Random random = new Random();

    Puzzle() {
        parts = new String[NUMBER_PARTS];
        parts[0] = "Я люблю";
        parts[1] = "создавать";
        parts[2] = "мобильные";
        parts[3] = "приложения";
        parts[4] = "под Android";
    }

    boolean solved(String[] solution) {
        if (solution != null && solution.length == parts.length) {
            for (int i = 0; i < parts.length; i++) {
                if (!solution[i].equals(parts[i]))
                    return false;
            }
            return true;
        } else
            return false;
    }

    String[] scramble() {
        String[] scrambled = new String[parts.length];
        for (int i = 0; i < scrambled.length; i++)
            scrambled[i] = parts[i];

        while (solved(scrambled)) {
            for (int i = 0; i < scrambled.length; i++) {
                int n = random.nextInt(scrambled.length - i) + i;
                String temp = scrambled[i];
                scrambled[i] = scrambled[n];
                scrambled[n] = temp;
            }
        }
        return scrambled;
    }

    int getNumberOfParts() {
        return parts.length;
    }
}