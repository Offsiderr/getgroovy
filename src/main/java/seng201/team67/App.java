package seng201.team67;

import javafx.scene.text.Font;
/**
 * Default entry point class
 * @author seng201 teaching team
 */

public class App {

    public static void main(String[] args) {
        var stream = App.class.getResourceAsStream("/fonts/Game_Paused.ttf");
        Font f = Font.loadFont(stream, 14);
        System.out.println("Font name: " + f.getName());
        System.out.println("Font family: " + f.getFamily());
        javafx.application.Application.launch(seng201.team67.gui.JavaFxApp.class, args);
    }
}