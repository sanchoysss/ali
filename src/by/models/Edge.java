package by.models;

import java.awt.*;
import javafx.scene.paint.Color;

/**
 * Данный класс представляет собой грань
 */
public class Edge {

    private Point[] points;
    private boolean visible;
    private Color color;

    /**
     * Конструктор, который принимает на вход массив точек, чтобы создать грань
     * @param points массив точек
     */
    public Edge(Point... points) {
        this.points = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new Point(points[i]);
        }
        visible = true;
        color = new Color(0, 0, 0, 1);
    }

    /**
     * @return массив точек данной грани
     */
    public Point[] getPoints() {
        return points;
    }

    /**
     * Данный метод устанавливает для грани новые точки
     * @param points новый массив точек
     */
    public void setPoints(Point[] points) {
        this.points = points;
    }

    /**
     * Данный метод позволяет изменить переменную, которая отвечает за то,
     * видна ли грань
     * @param visible новое значение видимости
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return возвращает true или false, что позволяет определить, видна ли грань
     */
    public boolean isVisible() {
        return visible;
    }

    public double[] getXCoordinates() {
        double[] xCoordinates = new double[this.points.length];
        for (int i = 0; i < this.points.length; i++) {
            xCoordinates[i] = this.points[i].getX();
        }
        return xCoordinates;
    }

    public double[] getYCoordinates() {
        double[] yCoordinates = new double[this.points.length];
        for (int i = 0; i < this.points.length; i++) {
            yCoordinates[i] = this.points[i].getY();
        }
        return yCoordinates;
    }

    public void updateVisibility(Point viewer) {
        double angle = calculateAngle(viewer);
        this.visible = angle < 90;
    }

    public void updateColor(Point light) {
        double angle = calculateAngle(light);
        double green = 1 - (50 + 205 * (angle / 180d)) / 255;
        if (green < 0.2) green = 0.2;
        this.color = new Color(0, green, 0, 1);
    }

    public double calculateAngle(Point viewer) {
        double xA, yA, zA;
        xA = this.getPoints()[0].getY() * this.getPoints()[1].getZ() +
                this.getPoints()[1].getY() * this.getPoints()[2].getZ() +
                this.getPoints()[2].getY() * this.getPoints()[0].getZ() -
                this.getPoints()[1].getY() * this.getPoints()[0].getZ() -
                this.getPoints()[2].getY() * this.getPoints()[1].getZ() -
                this.getPoints()[0].getY() * this.getPoints()[2].getZ();
        yA = this.getPoints()[0].getZ() * this.getPoints()[1].getX() +
                this.getPoints()[1].getZ() * this.getPoints()[2].getX() +
                this.getPoints()[2].getZ() * this.getPoints()[0].getX() -
                this.getPoints()[1].getZ() * this.getPoints()[0].getX() -
                this.getPoints()[2].getZ() * this.getPoints()[1].getX() -
                this.getPoints()[0].getZ() * this.getPoints()[2].getX();

        zA = this.getPoints()[0].getX() * this.getPoints()[1].getY() +
                this.getPoints()[1].getX() * this.getPoints()[2].getY() +
                this.getPoints()[2].getX() * this.getPoints()[0].getY() -
                this.getPoints()[1].getX() * this.getPoints()[0].getY() -
                this.getPoints()[2].getX() * this.getPoints()[1].getY() -
                this.getPoints()[0].getX() * this.getPoints()[2].getY();
        Vector vectorA = new Vector(xA, yA, zA);
        xA = yA = zA = 0;
        for (int i = 0; i < this.getPoints().length; i++) {
            xA += this.getPoints()[i].getX();
            yA += this.getPoints()[i].getY();
            zA += this.getPoints()[i].getZ();
        }
        xA /= this.getPoints().length;
        yA /= this.getPoints().length;
        zA /= this.getPoints().length;
        double xB, yB, zB;
        xB = viewer.getX() - xA;
        yB = viewer.getY() - yA;
        zB = viewer.getZ() - zA;
        Vector vectorB = new Vector(xB, yB, zB);
        return Math.toDegrees(Math.acos( vectorA.multiply(vectorB) / vectorA.multiplyLengths(vectorB)));
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
