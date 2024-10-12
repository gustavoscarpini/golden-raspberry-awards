package com.example.goldenraspberryawards.util;

public class Util {

    private Util() {
    }

    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }
}
