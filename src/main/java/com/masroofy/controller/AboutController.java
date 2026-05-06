package com.masroofy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.awt.Desktop;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AboutController {

    @FXML
    private Label buildDateLabel;

    @FXML
    public void initialize() {
        buildDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
    }

    @FXML
    private void handleOpenGitHub() {
        openWebpage("https://github.com/youssef97-7/javafx-Masroofy-cringe");
    }

    private void openWebpage(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
