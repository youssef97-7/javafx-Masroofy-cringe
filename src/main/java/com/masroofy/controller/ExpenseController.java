package com.masroofy.controller;

import com.masroofy.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ExpenseController {
    @FXML
    private ComboBox<String> categoryBox;
    private TextArea dailyLimit;
    private TextArea remainingAllowance;


    @FXML
    public void initial(){
        categoryBox.getItems().addAll("Food", "Transportation", "Utilities", "Shopping", "Healthcare");
        categoryBox.setPromptText("Category");
    }
}
