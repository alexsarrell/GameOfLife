package com.example.cells;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Draw {
    public static void drawGrid(double width, double height,
                                double scale, GraphicsContext content){
        content.setStroke(Color.BLACK);
        content.setLineWidth(0.1);
        int step = (int)(Math.max(width, height) / (100 * scale));
        for(int i = 0; i < 100 * scale; i++){
            content.strokeLine(1, i * step, width - 1, i * step);
            content.strokeLine(i * step, 1, i * step, height - 1);
        }
    }
}