package rs.etf.ga070530.monopoly.Model;

import java.util.Random;


public class Dice {

    private static final int MIN = 1;
    private static final int MAX = 6;

    private int value;
    private Random rand;

    public int roll() {
        rand = new Random();

        value = rand.nextInt(MAX - MIN + 1) + MIN;
        return value;
    }
}
