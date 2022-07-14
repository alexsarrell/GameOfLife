package com.example.cells;

import javafx.scene.control.Button;

public class Test {
    public void testButton(Button button){
        for(int i = 0; i < 1000; i++){

            button.arm();
            button.fire();
        }
    }
}
