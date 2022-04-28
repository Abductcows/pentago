package io.github.abductcows.pentago;

import io.github.abductcows.pentago.gui.PentagoGUI;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {

        var game = new PentagoGUI();
        SwingUtilities.invokeLater(game::run);
    }
}
