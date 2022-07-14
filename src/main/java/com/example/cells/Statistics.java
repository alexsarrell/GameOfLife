package com.example.cells;

import javafx.scene.control.Label;

public class Statistics implements iStatObserver{
    int living = -1;
    int died = -1;
    public boolean isObserver(){
        return (living != -1 && died != -1);
    }
    public void update(boolean state, int livingCount, int diedCount){
        if(state){
            living = livingCount;
            died = diedCount;
        }
    }
    public int getLiving(){
        return living;
    }
    public int getDied(){
        return died;
    }
}
