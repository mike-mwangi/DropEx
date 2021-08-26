package com.example.dropex.Common;

import android.graphics.Color;

import java.util.Random;

public class ColorWheel {

    private static  String mColors[] = {
            "#39add1", // light blue [0]1
            "#3079ab", // dark blue [1]2
            "#c25975", // mauve [2]3
            "#f9845b", // orange4
            "#838cc7", // lavender4
            "#7d669e", // purple6
            "#53bbb4", // aqua7
            "#637a91", // dark gray9

            "#b7c0c7"  // light gray11
    };

    private static String mColors2[] = {
            "#3079ab", //dark blue  [0]1
            "#39add1", // light blue [1]2
            "#e0ab18", // mustard [2]3
            "#f092b0", // pink4
            "#b7c0c7",  // light gray5
            "#c25975", // mauve6
            "#51b46d", // green7
            "#838cc7", // lavender9

            "#53bbb4", // aqua11
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
