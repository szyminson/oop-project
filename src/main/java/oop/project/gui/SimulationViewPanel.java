package oop.project.gui;

import oop.project.GravityField;
import oop.project.Rocket;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static java.lang.Math.*;

class SimulationViewPanel extends JPanel {
    private Rocket rocket;
    private List<GravityField> gravitySources;

    SimulationViewPanel(Rocket rocket, List<GravityField> gravitySources) {
        this.rocket = rocket;
        this.gravitySources = gravitySources;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double minX = -1, maxX = 1, minY = -1, maxY = 1;
        for (GravityField field : gravitySources) {
            minX = min(field.getSourcePosition().getX() - field.getRadius(), minX);
            maxX = max(field.getSourcePosition().getX() + field.getRadius(), maxX);

            minY = min(field.getSourcePosition().getY() - field.getRadius(), minY);
            maxY = max(field.getSourcePosition().getY() + field.getRadius(), maxY);
        }

        int offX = getWidth() / 2;
        int offY = getHeight() / 2;
        double maxCoord = max(max(abs(minX), abs(maxX)), max(abs(minY), abs(maxY)));

        double scale = min(getWidth(), getHeight()) / maxCoord / 2;

        g.setColor(Color.BLACK);

        for (GravityField field : gravitySources) {
            int x = (int) (field.getSourcePosition().getX() * scale);
            int y = (int) (field.getSourcePosition().getY() * scale);
            int r = (int) (field.getRadius() * scale);

            g.drawOval(x - 1 + offX, -y - 1 + offY, 3, 3);
            g.drawOval(x - r + offX, -y - r + offY, r * 2 + 1, r * 2 + 1);
        }

        int rocketX = (int) (rocket.getPosition().getX() * scale);
        int rocketY = (int) (rocket.getPosition().getY() * scale);

        g.setColor(Color.BLUE);
        g.drawRect(rocketX - 2 + offX, -rocketY - 4 + offY, 5, 9);
    }
}
