package by.controllers;

import by.helpers.MatrixOperations;
import by.models.Edge;
import by.models.Parallelepiped;
import by.models.Pyramid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

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

    public void createFigures() {
        if (parallelepiped == null || pyramid == null) {
            parallelepiped = Parallelepiped.createParallelepiped(100, 100);
            pyramid = Pyramid.createPyramid(100, 100);
        }

        drawFigures();
    }

    /**Отрисовка фигур*/
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

    /**Отрисовка граней*/
    private void drawEdge(Edge edge) {
        double width = 1500;
        int height = 1000;
        double[] startCoordinates, endCoordinates;
        for (int i = 0; i < edge.getPoints().length; i++) {
            if (i == edge.getPoints().length -1 && edge.isVisible()) {
                startCoordinates = convertingCoordinates(edge.getPoints()[edge.getPoints().length - 1].getX(),
                        edge.getPoints()[edge.getPoints().length - 1].getY(), width, height);
                endCoordinates = convertingCoordinates(edge.getPoints()[0].getX(), edge.getPoints()[0].getY(),
                        width, height);
                canvas.getGraphicsContext2D().strokeLine(startCoordinates[0], startCoordinates[1],
                        endCoordinates[0], endCoordinates[1]);
            }else if (edge.isVisible()) {
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
        return  new double[] { coordinate_x + widthOfAScreen / 2.0, heightOfAScreen / 2.0 - coordinate_y};
    }

    /**Метод для отрисовки фигуры*/
    public void drawFigure(ActionEvent actionEvent) {
        if (parallelepiped == null || pyramid == null) {

            if (!setDataOnFigures()) {
                return;
            }
        } else {
            if (h1Field.getText().isEmpty() || h2Field.getText().isEmpty()
                    || a1SideField.getText().isEmpty() || a2SideField.getText().isEmpty()) {
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

            double a1  =Double.parseDouble(a1SideField.getText());
            double a2  =Double.parseDouble(a2SideField.getText());

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

    /**Перемещение*/
    public void moveFigures(ActionEvent actionEvent) {
        if (parallelepiped == null || pyramid == null) {
            exceptionOnMovingLabel.setText("Фигура не создана");
            exceptionOnMovingLabel.setVisible(true);
            return;
        }

        try {
            double moveX = Double.parseDouble(moveXLabel.getText());
            double moveY = Double.parseDouble(moveYLabel.getText());
            double moveZ = Double.parseDouble(moveZLabel.getText());
            double[][] matrix = new double[][] {
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
        for (int edgeInd = 0; edgeInd < edges.size(); edgeInd++) {
            for (int pointInd = 0; pointInd < edges.get(edgeInd).getPoints().length; pointInd++) {
                double[][] pointInMatrix = {edges.get(edgeInd).getPoints()[pointInd].getCoordinates()};
                pointInMatrix = MatrixOperations.multiplyMatrices(pointInMatrix, matrix);
                edges.get(edgeInd).getPoints()[pointInd].setCoordinates(pointInMatrix[0]);
            }
        }
    }
}
