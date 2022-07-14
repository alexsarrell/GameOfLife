package com.example.cells;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.Collection;

public class Header extends HBox{
    public Header(Collection<? extends Node> e){
        this.getChildren().addAll(e);
    }
    public void addChildren(Node e){
        this.getChildren().addAll(e);
    }
    public void addChildren(Collection<? extends Node> e){
        this.getChildren().addAll(e);
    }
    public void deleteChildren(Node e){
        this.getChildren().removeAll(e);
    }
}
