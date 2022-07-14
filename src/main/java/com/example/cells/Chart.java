package com.example.cells;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Chart{
    double scale = 1;
    Canvas canvas;
    int canvasHeight, canvasWidth;
    int max = -1;
    ArrayList<Integer> yPositions;
    GraphicsContext graphicsContext;
    public Chart(){
        yPositions = new ArrayList<Integer>();
        yPositions.add(0);
        canvas = new Canvas(180, 110);
        graphicsContext = canvas.getGraphicsContext2D();
        canvasHeight = (int)canvas.getHeight();
        canvasWidth = (int)canvas.getWidth();
        graphicsContext.setFill(Color.web("#6E7577"));
        graphicsContext.fillRect(0, 0, canvasWidth, canvasHeight);
        //drawBorder(graphicsContext, canvas);
    }
    public Canvas getChart(){
        return canvas;
    }
    public static void drawBorder(GraphicsContext graphicsContext, Canvas canvas){
        if(graphicsContext != null && canvas != null){
            graphicsContext.setLineWidth(1);
            graphicsContext.setStroke(Color.web("8B9A9D"));
            graphicsContext.moveTo(0, 0);
            graphicsContext.lineTo(0, canvas.getHeight());
            graphicsContext.lineTo(canvas.getWidth(), canvas.getHeight());
            graphicsContext.lineTo(canvas.getWidth(), 0);
            graphicsContext.lineTo(0, 0);
            graphicsContext.stroke();
        }
        else{
            throw new RuntimeException("Null values were passed to drawBorder");
        }
    }

    public void drawChart(int cPosY, int step){
        graphicsContext.setLineWidth(2);
        graphicsContext.setStroke(Color.web("6FCD58"));
        int cPosX = step;
        max = Math.max(max, cPosY);
        while(max * scale > canvasHeight) scale /= 2;
        yPositions.add(cPosY);
        graphicsContext.fillRect(0, 0, canvasWidth, canvasHeight);
        for (int i = 1; i < yPositions.size(); i++) {
            if (cPosX <= canvasWidth) {
                graphicsContext.strokeLine(cPosX - step, canvasHeight
                        - yPositions.get(i - 1) * scale, cPosX, canvasHeight
                        - (yPositions.get(i) * scale));
                cPosX += step;
            } else {
                max = -1;
                for (int j = 0; j < yPositions.size() / 2; j++) {
                    yPositions.remove(j);
                }
                for (int j = 0; j < yPositions.size(); j++) {
                    max = Math.max(yPositions.get(j), max);
                }
                while (max * scale < canvasHeight) {
                    scale *= 2;
                }
                break;
            }
        }
    }
}
