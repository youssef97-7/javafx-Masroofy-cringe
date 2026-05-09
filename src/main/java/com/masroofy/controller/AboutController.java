package com.masroofy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.awt.Desktop;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the "About" view.
 * Handles the display of build information and external links to the project repository.
 */
public class AboutController {

    @FXML
    private Label buildDateLabel;

    /**
     * Automatically called by FXML loader after the view has been rooted.
     * Formats and sets the current date on the build label.
     */
    @FXML
    public void initialize() {
        buildDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
    }

    /**
     * Event handler for the GitHub link action.
     * Redirects the user to the project's source code repository.
     */
    @FXML
    private void handleOpenGitHub() {
        openWebpage("https://github.com/youssef97-7/javafx-Masroofy-cringe");
    }

    /**
     * Attempts to open a URL in the system's default web browser.
     *
     * @param url The string representation of the URI to be opened.
     */
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
