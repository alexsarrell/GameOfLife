package com.example.cells;

public interface iStatObservable {
    public void addObserver(iStatObserver observer);
    public void banishObserver(iStatObserver observer);
    public void notifyObservers(boolean state, int smth);
}
