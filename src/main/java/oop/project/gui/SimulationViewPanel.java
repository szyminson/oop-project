package oop.project.gui;

import oop.project.GravityField;
import oop.project.Rocket;
import oop.project.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

        double centerX = rocket.getPosition().getX();
        double centerY = rocket.getPosition().getY();

        int offX = getWidth() / 2;
        int offY = getHeight() / 2;

        ArrayList<Double> distances = new ArrayList<>();
        for (GravityField field : gravitySources) {
            double distance = (field.getSourcePosition().sub(rocket.getPosition()).length() - field.getRadius());
            distances.add(distance);
        }
        distances.sort(Double::compare);
        Double distance = distances.isEmpty() ? 1.0 : (distances.get(0) + 50) * 2;
        double scale = Math.max(Math.min(getWidth() - 50, getHeight() - 50), 1) / distance;
        double gridSize = Math.pow(2, (int) (Math.log(distance / 6) / Math.log(2)));

        g.setColor(new Color(1f, 0f, 0f, 0.05f));
        drawGrid(g, gridSize / 2, scale, centerX, centerY);
        g.setColor(new Color(1f, 0f, 0f, 0.1f));
        drawGrid(g, gridSize, scale, centerX, centerY);
        g.setColor(new Color(1f, 0f, 0f, 0.2f));
        drawGrid(g, gridSize * 2, scale, centerX, centerY);


        g.setColor(Color.BLACK);

        for (GravityField field : gravitySources) {
            int x = (int) ((field.getSourcePosition().getX() - centerX) * scale);
            int y = (int) ((field.getSourcePosition().getY() - centerY) * scale);
            int r = (int) (field.getRadius() * scale);

            g.drawOval(x - 1 + offX, -y - 1 + offY, 3, 3);
            g.drawOval(x - r + offX, -y - r + offY, r * 2 + 1, r * 2 + 1);
        }

        int rocketX = (int) ((rocket.getPosition().getX() - centerX) * scale) + offX;
        int rocketY = (int) ((rocket.getPosition().getY() - centerY) * scale) + offY;

        g.setColor(Color.BLUE);

        double alpha = -rocket.getDirection();
        Vector forward = new Vector(cos(alpha), sin(alpha)).mul(11);

        alpha += Math.PI / 2;
        Vector left = new Vector(cos(alpha), sin(alpha)).mul(4);

        alpha += Math.PI;
        Vector right = new Vector(cos(alpha), sin(alpha)).mul(4);

        g.drawLine((int) forward.getX() + rocketX, (int) forward.getY() + rocketY,
                (int) left.getX() + rocketX, (int) left.getY() + rocketY);

        g.drawLine((int) left.getX() + rocketX, (int) left.getY() + rocketY,
                (int) right.getX() + rocketX, (int) right.getY() + rocketY);

        g.drawLine((int) right.getX() + rocketX, (int) right.getY() + rocketY,
                (int) forward.getX() + rocketX, (int) forward.getY() + rocketY);
    }

    private void drawGrid(Graphics g, double gridSize, double scale, double centerX, double centerY) {
        int count = 30;
        for (int i = -count; i < count; i++) {
            double x = (Math.floor(i + centerX / gridSize) * gridSize - centerX) * scale;
            g.drawLine((int) Math.ceil(x) + getWidth() / 2, 0, (int) Math.ceil(x) + getWidth() / 2, getHeight());
        }

        for (int i = -count; i < count; i++) {
            double y = (Math.floor(i - centerY / gridSize) * gridSize + centerY) * scale;
            g.drawLine(0, (int) Math.ceil(y) + getHeight() / 2, getWidth(), (int) Math.ceil(y) + getHeight() / 2);
        }
    }
}
