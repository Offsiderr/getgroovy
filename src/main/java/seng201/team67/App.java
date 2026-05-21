package seng201.team67;

import javafx.scene.text.Font;
/**
 * Launches the JavaFX application for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */

public class App {

    /**
     * Launches the JavaFX application entry point for the game.
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        var stream = App.class.getResourceAsStream("/fonts/Game_Paused.ttf");
        Font f = Font.loadFont(stream, 14);
        System.out.println("Font name: " + f.getName());
        System.out.println("Font family: " + f.getFamily());
        javafx.application.Application.launch(seng201.team67.gui.JavaFxApp.class, args);
    }
}