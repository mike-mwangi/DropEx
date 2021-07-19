package com.example.dropex;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static SharedPref sharedPref = new SharedPref();
   private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private String IS_FIRST_LAUNCH = "is_first_launch";

    private SharedPref(){

    }
    public void setIsFirstLaunchToFalse() {
        editor.putBoolean(IS_FIRST_LAUNCH, false);
        editor.commit();
    }

    public Boolean isFirstLaunch(){
        return sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true);
    }





        public static SharedPref getInstance(Context context) {

            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
                editor = sharedPreferences.edit();
            }

            return sharedPref;
        }
    }

