package com.example.cells;

import java.util.ArrayList;

public class PrimitiveCoordinates {
    private int[][] coordinates;
    public int[][] getCoordinates(){
        return coordinates;
    }
    public PrimitiveCoordinates(ArrayList<Position> cells){
        if(cells != null){
            coordinates = new int[cells.size()][2];
            for(int i = 0; i < coordinates.length; i++){
                if(cells.get(i) != null){
                    coordinates[i][0] = cells.get(i).getPosX();
                    coordinates[i][1] = cells.get(i).getPosY();
                }
            }
        }
        else{
            System.out.println("Null coordinates exception");
            coordinates = new int[1][2];
            coordinates[0][0] = 1;
            coordinates[0][1] = 1;
            throw new RuntimeException("Bad cells");
        }

    }
}
