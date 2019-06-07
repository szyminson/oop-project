package oop.project;

import oop.project.gui.SimulatorGui;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SimulatorGui gui = new SimulatorGui();
            gui.setVisible(true);
        });
    }
}
