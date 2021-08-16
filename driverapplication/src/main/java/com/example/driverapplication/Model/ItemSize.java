package com.example.driverapplication.Model;

public abstract class ItemSize {
    public static int smallSizeItemWeight=1;
    public static int mediumSizeItemWeight=2;
    public static int bigSizeItemWeight=5;

    public static int getSizeOfItem(String size){
        int sizeInt=-1;
        if(size == "small"){
            sizeInt= smallSizeItemWeight;
        }
        else if (size == "medium"){
            sizeInt= mediumSizeItemWeight;
        }
        else if (size == "big")
        {
            sizeInt= bigSizeItemWeight;
        }
        return sizeInt;
    }
}
