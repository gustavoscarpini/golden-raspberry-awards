package com.example.goldenraspberryawards.util;

import java.text.SimpleDateFormat;

public class Util {

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
