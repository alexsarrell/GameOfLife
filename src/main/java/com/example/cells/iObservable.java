package com.example.cells;

public interface iObservable {
    public void addObserver(iObserver observer);
    public void banishObserver(iObserver observer);
    public void notifyObservers(boolean state);
}
