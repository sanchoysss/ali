package by.controllers;

import by.helpers.MatrixOperations;
import by.models.Edge;
import by.models.Parallelepiped;
import by.models.Pyramid;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

public class MainController {

    @FXML
    private Canvas canvas;

    @FXML
    private TextField a1SideField, a2SideField, h1Field, h2Field;

    @FXML
    private TextField moveXLabel, moveYLabel, moveZLabel;

    @FXML
    private Label exceptionLabel, exceptionOnMovingLabel;

    Parallelepiped parallelepiped;
    Pyramid pyramid;

    /**
     * Отрисовка фигур
     */
    private void drawFigures() {
        for (Edge edge : parallelepiped.getRectangles()) {
            drawEdge(edge);
        }

        for (Edge edge : parallelepiped.getTriangles()) {
            drawEdge(edge);
        }

        for (Edge edge : pyramid.getTriangles()) {
            drawEdge(edge);
        }
    }

    /**
     * Отрисовка граней
     */
    private void drawEdge(Edge edge) {
        double width = 1500;
        int height = 1000;
        double[] startCoordinates, endCoordinates;
        for (int i = 0; i < edge.getPoints().length; i++) {
            if (i == edge.getPoints().length - 1 && edge.isVisible()) {
                startCoordinates = convertingCoordinates(edge.getPoints()[edge.getPoints().length - 1].getX(),
                        edge.getPoints()[edge.getPoints().length - 1].getY(), width, height);
                endCoordinates = convertingCoordinates(edge.getPoints()[0].getX(), edge.getPoints()[0].getY(),
                        width, height);
                canvas.getGraphicsContext2D().strokeLine(startCoordinates[0], startCoordinates[1],
                        endCoordinates[0], endCoordinates[1]);
            } else if (edge.isVisible()) {
                startCoordinates = convertingCoordinates(edge.getPoints()[i].getX(), edge.getPoints()[i].getY(),
                        width, height);
                endCoordinates = convertingCoordinates(edge.getPoints()[i + 1].getX(), edge.getPoints()[i + 1].getY(),
                        width, height);
                canvas.getGraphicsContext2D().strokeLine(startCoordinates[0], startCoordinates[1],
                        endCoordinates[0], endCoordinates[1]);
            }

        }
    }

    private static double[] convertingCoordinates(double coordinate_x, double coordinate_y,
                                                  double widthOfAScreen, double heightOfAScreen) {
        return new double[]{coordinate_x + widthOfAScreen / 2.0, heightOfAScreen / 2.0 - coordinate_y};
    }

    /**
     * Метод для отрисовки фигуры
     */
    public void drawFigure() {
        if (parallelepiped == null || pyramid == null) {

            if (!setDataOnFigures()) {
                return;
            }
        } else {
            if (!h1Field.getText().isEmpty() || !h2Field.getText().isEmpty()
                    || !a1SideField.getText().isEmpty() || !a2SideField.getText().isEmpty()) {
                if (!setDataOnFigures()) {
                    return;
                }
            } else {
                canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        }
        exceptionLabel.setVisible(false);
        drawFigures();
    }

    private boolean setDataOnFigures() {
        try {
            double height1 = Double.parseDouble(h1Field.getText());
            double height2 = Double.parseDouble(h2Field.getText());

            double a1 = Double.parseDouble(a1SideField.getText());
            double a2 = Double.parseDouble(a2SideField.getText());

            h1Field.setText("");
            h2Field.setText("");

            a1SideField.setText("");
            a2SideField.setText("");

            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            pyramid = Pyramid.createPyramid(height1, a1);
            parallelepiped = Parallelepiped.createParallelepiped(height2, a2);

            return true;
        } catch (NumberFormatException e) {
            exceptionLabel.setVisible(true);
            exceptionLabel.setText("Введите данные корректно");
            return false;
        }
    }

    /**
     * Перемещение
     */
    public void moveFigures() {
        if (parallelepiped == null || pyramid == null) {
            exceptionOnMovingLabel.setText("Фигура не создана");
            exceptionOnMovingLabel.setVisible(true);
            return;
        }

        try {
            double moveX = Double.parseDouble(moveXLabel.getText());
            double moveY = Double.parseDouble(moveYLabel.getText());
            double moveZ = Double.parseDouble(moveZLabel.getText());
            double[][] matrix = new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {moveX, moveY, moveZ, 1}
            };
            transformAndThenDraw(matrix);
            exceptionOnMovingLabel.setVisible(false);
        } catch (NumberFormatException e) {
            exceptionOnMovingLabel.setText("Данные введены неверно!");
            exceptionOnMovingLabel.setVisible(true);
        }
    }

    private void transformAndThenDraw(double[][] matrix) {
        transformEdges(pyramid.getEdges(), matrix);
        transformEdges(parallelepiped.getEdges(), matrix);
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawFigures();
    }

    private void transformEdges(List<Edge> edges, double[][] matrix) {
        for (Edge edge : edges) {
            for (int pointInd = 0; pointInd < edge.getPoints().length; pointInd++) {
                double[][] pointInMatrix = {edge.getPoints()[pointInd].getCoordinates()};
                pointInMatrix = MatrixOperations.multiplyMatrices(pointInMatrix, matrix);
                edge.getPoints()[pointInd].setCoordinates(pointInMatrix[0]);
            }
        }
    }

    @FXML
    private TextField rotateXField, rotateYField, rotateZField;

    @FXML
    private Label exceptionOnRotatingX, exceptionOnRotatingY, exceptionOnRotatingZ;

    public void rotateOnX() {
        if (pyramid == null || parallelepiped == null) {
            setException(exceptionOnRotatingX, "Фигуры не созданы");
            return;
        }
        double angleX;
        try {
            angleX = toRadians(Double.parseDouble(rotateXField.getText()));
        } catch (NumberFormatException e) {
            setException(exceptionOnRotatingX, "Данные введены неверно");
            return;
        }
        exceptionOnRotatingX.setVisible(false);
        double[][] matrix = new double[][]{
                {1, 0, 0, 0},
                {0, cos(angleX), sin(angleX), 0},
                {0, -sin(angleX), cos(angleX), 0},
                {0, 0, 0, 1}
        };
        transformAndThenDraw(matrix);
    }

    public void rotateOnY() {
        if (pyramid == null || parallelepiped == null) {
            setException(exceptionOnRotatingY, "Фигуры не созданы");
            return;
        }
        double angleY;
        try {
            angleY = toRadians(Double.parseDouble(rotateYField.getText()));
        } catch (NumberFormatException e) {
            setException(exceptionOnRotatingY, "Данные введены неверно");
            return;
        }
        exceptionOnRotatingY.setVisible(false);
        double[][] matrix = {
                {cos(angleY), 0, -sin(angleY)},
                {0, 1, 0, 0},
                {sin(angleY), 0, cos(angleY), 0},
                {0, 0, 0, 1}
        };
        transformAndThenDraw(matrix);
    }

    public void rotateOnZ() {
        if (pyramid == null || parallelepiped == null) {
            setException(exceptionOnRotatingZ, "Фигуры не созданы");
            return;
        }
        double angleZ;
        try {
            angleZ = toRadians(Double.parseDouble(rotateZField.getText()));
        } catch (NumberFormatException exception) {
            setException(exceptionOnRotatingZ, "Данные введены неверно");
            return;
        }
        exceptionOnRotatingZ.setVisible(false);
        double[][] matrix = {
                {cos(angleZ), sin(angleZ), 0, 0},
                {-sin(angleZ), cos(angleZ), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        transformAndThenDraw(matrix);
    }

    private void setException(Label exceptionLabel, String message) {
        exceptionLabel.setVisible(true);
        exceptionLabel.setText(message);
    }
}
