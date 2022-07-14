package com.example.cells;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UserCanvasControl {
    GraphicsContext graphics;
    Canvas canvas;
    double scale;
    public UserCanvasControl(GraphicsContext graphics, double scale, Canvas canvas){
        graphics.setFill(Color.web("#6E7577"));
        graphics.fillRect(0, 0, canvas.getWidth(),
                canvas.getHeight());
        this.graphics = graphics;
        this.scale = scale;
        this.canvas = canvas;
        //Chart.drawBorder(graphics, canvas);
    }
    public void clearCanvas(){
        graphics.setFill(Color.web("#6E7577"));
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Draw.drawGrid(canvas.getWidth(), canvas.getHeight(), scale, graphics);
        //Chart.drawBorder(graphics, canvas);
    }
    public void clearCanvas(double scale){
        graphics.setFill(Color.web("#6E7577"));
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Draw.drawGrid(canvas.getWidth(), canvas.getHeight(), scale, graphics);
        //Chart.drawBorder(graphics, canvas);
    }
    public void canvasClick(double cursorCoordinateX, double cursorCoordinateY,
                            Color color, double scale){
        if(cursorCoordinateX >= 0.0 && cursorCoordinateY >= 0.0 &&
                cursorCoordinateX < canvas.getWidth() &&
                cursorCoordinateY < canvas.getHeight()) {
            this.scale = scale;
            Position[] positions = getSquare(cursorCoordinateX,
                    cursorCoordinateY, scale, canvas);
            graphics.setFill(color);
            graphics.fillRect(positions[0].getPosX(), positions[0].getPosY(),
                    positions[1].getPosX() - positions[0].getPosX(),
                    positions[1].getPosY() - positions[0].getPosY());
        }
    }
    public void drawCell(double cellCoordinateX, double cellCoordinateY, Color color){
        if(cellCoordinateX >= 0 && cellCoordinateY >= 0 &&
                cellCoordinateX < canvas.getWidth()/(scale)
                && cellCoordinateY < canvas.getHeight()/(scale)) {
            graphics.setFill(color);
            double step = Math.max(canvas.getWidth(), canvas.getHeight())/ (100 * scale);
            cellCoordinateX = cellCoordinateX * (int)step;
            cellCoordinateY = cellCoordinateY * (int)step;
            graphics.fillRect((int)cellCoordinateX, (int)cellCoordinateY, (int)step, (int)step);
        }

    }
    public static Position[] getSquare(double cursorCoordinateX, double cursorCoordinateY,
                              double scale, Canvas canvas){
            int topLine, bottomLine, leftLine, rightLine, step;
            step = (int)(Math.max(canvas.getWidth(), canvas.getHeight())/(100 * scale));
            topLine = lines((int) cursorCoordinateY, step, true);
            bottomLine = lines((int) cursorCoordinateY, step, false);
            leftLine = lines((int) cursorCoordinateX, step, true);
            rightLine = lines((int) cursorCoordinateX, step, false);
            Position points1 = new Position(leftLine, topLine);
            Position points2 = new Position(rightLine, bottomLine);
            if(points1.getPosX() >= 0 && points1.getPosY() >=0 &&
                    points1.getPosX() < canvas.getWidth()
                    && points1.getPosY() <  canvas.getHeight() &&
                    points2.getPosX() >= 0 && points2.getPosY() >=0 &&
                    points2.getPosX() < canvas.getWidth()
                    && points2.getPosY() <  canvas.getHeight()) {
                return new Position[]{points1, points2};
            }
            else {
                throw new RuntimeException("Bad coordinates");
            }
    }
    static int lines(int line, int step, boolean vector){

        if(vector){
            while(line % step != 0) {
                line -= 1;
            }
        }
        else {
            while(line % step != 0) {
                line += 1;
            }
        }
        if(line < 0){
            line = 0;
        }
        if(line >= 400){
            line = 399;
        }
        return line;
    }
}
