package com.example.cells;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Collection;
public class CellularAutomation extends Application {
    boolean activeButton = true;
    Petri petri;
    Color cellsColor;
    int chartStep = 5;
    Timer cellDraw;
    Timer statisticTimer;

    @Override
    public void start(Stage stage){
        FXMLLoader fxmlLoader = new FXMLLoader(CellularAutomation.class.getResource("main.fxml"));

        stage.initStyle(StageStyle.UNDECORATED);
        BorderPane borderPane = new BorderPane();

        cellsColor = Color.web("262828");
        ObservableList<String> scales = FXCollections.observableArrayList(
                "1", "0.2", "0.3", "0.5", "0.1");
        ComboBox scaleBox = new ComboBox(scales);
        Label sliderValue = new Label();
        scaleBox.getSelectionModel().selectFirst();
        Test test = new Test();
        HBox hroot = new HBox();
        HBox root2 = new HBox();
        VBox vRoot1_vRoot = new VBox();
        Label livingCells = new Label();
        Label deathCells = new Label();
        root2.setSpacing(100);
        hroot.setSpacing(5);
        hroot.setPadding(new Insets(20, 0, 0, 0));
        hroot.setAlignment(Pos.TOP_LEFT);
        VBox vRoot = new VBox();
        vRoot.getStyleClass().add("vbox");
        vRoot.setAlignment(Pos.TOP_LEFT);
        vRoot.setPadding(new Insets(0, 0, 0, 50));
        vRoot.setSpacing(20);
        Canvas canvas = new Canvas(400, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Chart aliveChart = new Chart();
        int side = (int)Math.max(canvas.getWidth(), canvas.getHeight());
        petri = new Petri((int)(100 * Double.parseDouble(
            (String)(scaleBox.getValue()))),
                    side / (int)(100 * Double.parseDouble(
                                (String)(scaleBox.getValue()))));

            UserCanvasControl userCanvasControl = new UserCanvasControl(gc,
                    Double.parseDouble((String)scaleBox.getValue()), canvas);
            Button start = new Button();
            //Button edit = new Button();
            Button pause = new Button();
            Button clear = new Button();
            setButtonProperties(start, "start");
            //setButtonProperties(edit, "edit");
            setButtonProperties(pause, "pause");
            setButtonProperties(clear, "clear");
            Draw.drawGrid(canvas.getWidth(), canvas.getHeight(),
                    Double.parseDouble((String)scaleBox.getValue()), gc);
            Button testB = new Button();
            hroot.getChildren().addAll(start,  pause,  clear, scaleBox, testB, sliderValue);
            vRoot1_vRoot.setAlignment(Pos.TOP_LEFT);
            vRoot1_vRoot.setSpacing(20);
            vRoot1_vRoot.getChildren().addAll(aliveChart.getChart(), livingCells, deathCells);
            root2.getChildren().addAll(canvas, vRoot1_vRoot);
            vRoot.getChildren().addAll(hroot, root2);

            Statistics statistics = new Statistics();
            livingCells.setText("Alive: 0");
            deathCells.setText("Died: 0");
            borderPane.setCenter(vRoot);

            testB.setOnAction(actionEvent -> {
                test.testButton(start);
                test.testButton(clear);
                test.testButton(pause);
                //test.testButton(edit);
            });
            scaleBox.setOnAction(actionEvent -> {
                if(cellDraw != null){
                    cellDraw.cancel();
                    cellDraw.purge();
                }
                petri.getStatisticObserver().banishObserver(statistics);
                statistics.update(true, 0, 0);
                petri.getStatisticObserver().addObserver(statistics);
                String sScale = (String) scaleBox.getValue();
                petri = new Petri((int)(100 * Double.parseDouble(sScale)), (side /
                        (int)(100 * Double.parseDouble(sScale))));
                userCanvasControl.clearCanvas(Double.parseDouble(sScale));
            });
            //edit.setOnAction(actionEvent -> {
            //    activeButton = true;
            //});
        pause.setOnAction(actionEvent -> {
            if(cellDraw != null){
                cellDraw.cancel();
                cellDraw.purge();
            }
            if(statisticTimer != null){
                statisticTimer.cancel();
            }
            activeButton = true;
        });
            start.setOnAction(actionEvent -> {
                if(petri.assistantCheck()){
                    if(activeButton){
                        petri.getStatisticObserver().addObserver(statistics);
                        activeButton = false;
                        cellDraw = new Timer();
                        cellDraw.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    userCanvasControl.clearCanvas();
                                    ArrayList<PrimitiveCoordinates> coordinates = petri.startSim();
                                    if(!coordinates.isEmpty()){
                                        for(int j = 0; j < coordinates.get(coordinates.size() - 1).getCoordinates().length; j++){
                                            userCanvasControl.drawCell(coordinates.get(coordinates.size() - 1).getCoordinates()[j][0],
                                                    coordinates.get(coordinates.size() - 1).getCoordinates()[j][1],
                                                    cellsColor);
                                        }
                                    }
                                });
                            }
                        }, 0, 100);
                        statisticTimer = new Timer();
                        statisticTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(statistics.isObserver()){
                                            livingCells.setText("Alive: " + Integer.toString(statistics.getLiving()));
                                            deathCells.setText("Died: " + Integer.toString(statistics.getDied()));
                                            aliveChart.drawChart(statistics.getLiving(), chartStep);
                                        }
                                    }
                                });
                            }
                        }, 0, 500);

                    }
                }
            });
            clear.setOnAction(actionEvent -> {
                petri.getStatisticObserver().banishObserver(statistics);
                statistics.update(true, 0, 0);
                petri.getStatisticObserver().addObserver(statistics);
                if(cellDraw != null){
                    cellDraw.cancel();
                }
                String sScale = (String) scaleBox.getValue();
                userCanvasControl.clearCanvas(Double.parseDouble(sScale));
                petri = new Petri((int)(100 * Double.parseDouble(sScale)), side / (int)(100 * Double.parseDouble(sScale)));
            });
            canvas.setOnMouseDragged(mouseEvent -> {
                if(activeButton){
                    String sScale = (String) scaleBox.getValue();
                    userCanvasControl.canvasClick((int)mouseEvent.getX(),
                            (int)mouseEvent.getY(), cellsColor, Double.parseDouble(sScale));
                    Position[] positions = UserCanvasControl.getSquare((int)mouseEvent.getX(),
                            (int)mouseEvent.getY(), Double.parseDouble(sScale), canvas);
                    petri.addCell(positions[0]);
                }
            });
            canvas.setOnMouseClicked(mouseEvent -> {
                if(activeButton){
                    String sScale = (String) scaleBox.getValue();
                    userCanvasControl.canvasClick((int)mouseEvent.getX(),
                            (int)mouseEvent.getY(), cellsColor, Double.parseDouble(sScale));
                    Position[] positions = UserCanvasControl.getSquare((int)mouseEvent.getX(),
                            (int)mouseEvent.getY(), Double.parseDouble(sScale), canvas);
                    petri.addCell(positions[0]);
                }
           });
            stage.setOnCloseRequest(windowEvent -> {
                if(cellDraw != null){
                    cellDraw.cancel();
                    cellDraw.purge();
                }
                statisticTimer.cancel();
            });
            try{
                BorderPane mainPane = fxmlLoader.load();
                Scene scene = new Scene(mainPane, 780, 540);
                try{
                    scene.getStylesheets().add("CAStyle.css");
                }catch (Exception e){
                    e.printStackTrace();
                }
                mainPane.setCenter(vRoot);
                //stage.setTitle("Cellular Automation");
                stage.setScene(scene);
                stage.show();
            }catch (IOException e){
                e.printStackTrace();
            }

    }
    private void setButtonImage(Image img, Button button, double width, double height){
        ImageView iv = new ImageView(img);
        iv.setFitHeight(height);
        iv.setFitWidth(width);
        button.setGraphic(iv);
    }
    private void setButtonProperties(Button button, String buttonName){
        try{
            Image img = new Image(System.getProperty("user.dir") + "/img/" + buttonName + ".png");
            setButtonImage(img, button, 10, 10);
            button.setMinSize(26, 26);
        }
       catch (Exception e){
            System.out.println("Something goes wrong in setButtonProp method");
       }
    }
    public static void main(String[] args) {
        launch();
    }
}