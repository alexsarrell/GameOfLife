package com.example.cells;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CAController{
    Stage stage;
    double sceneX;
    double sceneY;
    @FXML
    public void headerDrag(MouseEvent me){
        if(stage == null) stage = (Stage)((Node)me.getSource()).getScene().getWindow();
        stage.setX(me.getScreenX() - sceneX);
        stage.setY(me.getScreenY() - sceneY);
    }
    @FXML
    public void headerPressed(MouseEvent me){
          sceneX = me.getSceneX();
          sceneY = me.getSceneY();
    }
    @FXML
    public void onCloseButton(){
        Platform.exit();
    }
    @FXML
    public void onIconified(ActionEvent actionEvent){
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

}
