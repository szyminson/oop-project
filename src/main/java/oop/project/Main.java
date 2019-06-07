package oop.project;

import oop.project.gui.SimulatorGui;

import java.awt.*;

/**
 * Main class of the application.
 */
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SimulatorGui gui = new SimulatorGui();
            gui.setVisible(true);
        });
    }
}
