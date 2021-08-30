package com.example.dropex.Common;

import android.graphics.Color;

import java.util.Random;

public class ColorWheel {

    private static  String mColors[] = {
            "#39add1", // light blue [0]1
            "#3079ab", // dark blue [1]2

    };

    private static String mColors2[] = {
            "#3079ab", //dark blue  [0]1
            "#39add1", // light blue [1]2
    };

    public static int[] getGcolors() {
        String color = "";
        String color2 = "";

        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mColors.length);

        color = mColors[randomNumber];
        color2 = mColors2[randomNumber];
        int colorAsInt = Color.parseColor(color);
        int colorAsInt2 = Color.parseColor(color2);

        return new int[]{colorAsInt, colorAsInt2};
    }
}
