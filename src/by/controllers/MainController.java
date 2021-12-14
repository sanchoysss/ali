package by.controllers;

import by.helpers.MatrixOperations;
import by.models.Edge;
import by.models.Parallelepiped;
import by.models.Point;
import by.models.Pyramid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

public class MainController implements Initializable {

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

    @FXML
    private Point light;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        light = new Point(0, 0, 5000);
    }

    /**
     * Отрисовка фигур
     */
    private void drawFigures() {
        updateEdges(new Point(0, 0, 5000));
        if (parallelepiped.getClosestPoint().getZ() > pyramid.getClosestPoint().getZ()) {
            drawParallelepiped();
            drawPyramid();
        } else {
            drawPyramid();
            drawParallelepiped();
        }
    }

    public void drawPyramid() {
        for (Edge edge : pyramid.getTriangles()) {
            drawEdge(edge);
        }
    }

    public void drawParallelepiped() {
        for (Edge edge : parallelepiped.getEdges()) {
            drawEdge(edge);
        }
    }

    /**
     * Отрисовка грани
     */
    private void drawEdge(Edge edge) {
        if (edge.isVisible()) {
            double[] xCoordinates = edge.getXCoordinates();
            double[] yCoordinates = edge.getYCoordinates();

            for (int i = 0; i < xCoordinates.length; i++) {
                double[] coordinates = convertingCoordinates(xCoordinates[i], yCoordinates[i], 1500, 1000);
                xCoordinates[i] = coordinates[0];
                yCoordinates[i] = coordinates[1];
            }

            canvas.getGraphicsContext2D().setFill(edge.getColor());
            //canvas.getGraphicsContext2D().strokePolygon(xCoordinates, yCoordinates, xCoordinates.length);
            canvas.getGraphicsContext2D().fillPolygon(xCoordinates, yCoordinates, xCoordinates.length);
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

    @FXML
    private TextField scaleXField, scaleYField, scaleZField;

    @FXML
    private Label exceptionOnScalingLabel;

    @FXML
    public void scaleFigures() {
        if (pyramid == null || parallelepiped == null) {
            exceptionOnScalingLabel.setText("Фигуры не созданы");
            exceptionOnScalingLabel.setVisible(true);
            return;
        }
        try {
            // Достаем значения из инпутов
            double scaleX = Double.parseDouble(scaleXField.getText());
            double scaleY = Double.parseDouble(scaleYField.getText());
            double scaleZ = Double.parseDouble(scaleZField.getText());
            //Матрица масштабирования
            double[][] matrix = {
                    {scaleX, 0,      0     , 0},
                    {0     , scaleY, 0     , 0},
                    {0     , 0     , scaleZ, 0},
                    {0     , 0     , 0     , 1}
            };
            //масштабируем и отрисовываем
            transformAndThenDraw(matrix);
            exceptionOnScalingLabel.setVisible(false);
        } catch (NumberFormatException e) {
            exceptionOnScalingLabel.setText("Данные введены неверно");
            exceptionOnScalingLabel.setVisible(true);
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

    /**
     * Поворот относительно оси X
     */
    public void rotateOnX() {
        if (pyramid == null || parallelepiped == null) {
            setException(exceptionOnRotatingX, "Фигуры не созданы");
            return;
        }
        double angleX;
        try {
            //берем значение угла из инпута
            angleX = toRadians(Double.parseDouble(rotateXField.getText()));
        } catch (NumberFormatException e) {
            setException(exceptionOnRotatingX, "Данные введены неверно");
            return;
        }
        exceptionOnRotatingX.setVisible(false);
        // матрица поворота
        double[][] matrix = new double[][]{
                {1, 0, 0, 0},
                {0, cos(angleX), sin(angleX), 0},
                {0, -sin(angleX), cos(angleX), 0},
                {0, 0, 0, 1}
        };
        // поворачиваем и отрисовываем
        transformAndThenDraw(matrix);
    }

    /**
     * поворот относительно Y
     */
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

    /**
     * Поворот относительно Z
     */
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

    private void updateEdges(Point viewer) {
        pyramid.updateEdges(viewer, light);
        parallelepiped.updateEdges(viewer, light);
    }

    @FXML
    private Label exceptionOnProjectionsLabel;

    /**
     * Профильная проекция
     */
    public void drawProfileProjection() {
        if (pyramid == null || parallelepiped == null) {
            exceptionOnProjectionsLabel.setText("Фигуры не созданы");
            exceptionOnProjectionsLabel.setVisible(true);
            return;
        }
        exceptionOnProjectionsLabel.setVisible(false);
        double[][] matrix = new double[][] {
                {0, 0, 1, 0},
                {0, 1, 0, 0},
                {1, 0, 0, 0},
                {0, 0, 0, 1}
        };
        transformAndThenDraw(matrix);
    }

    /**
     * Горизонтальная проекция
     */
    public void drawHorizontalProjection() {
        if (pyramid == null || parallelepiped == null) {
            exceptionOnProjectionsLabel.setText("Фигуры не созданы");
            exceptionOnProjectionsLabel.setVisible(true);
            return;
        }
        exceptionOnProjectionsLabel.setVisible(false);
        double[][] matrix = new double[][] {
                {1, 0, 0, 0},
                {0, 0, 1, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 1}
        };
        transformAndThenDraw(matrix);
    }

    @FXML
    private TextField alphaField, phiField, roField, dField;

    @FXML
    private Label exceptionPerspectiveLabel;

    /**
     * Перспективная проекция
     */
    public void drawPerspectiveProjection() {
        if (parallelepiped == null || pyramid == null) {
            exceptionPerspectiveLabel.setText("Фигуры не созданы");
            exceptionPerspectiveLabel.setVisible(true);
            return;
        }
        try {
            double alpha = toRadians(Double.parseDouble(alphaField.getText()));
            double phi = toRadians(Double.parseDouble(phiField.getText()));
            double ro = Double.parseDouble(roField.getText());
            double d = Double.parseDouble(dField.getText());

            double[][] matrixV = {
                    {-sin(alpha), -cos(phi) * (-cos(alpha)), -sin(phi) * cos(alpha), 0},
                    {cos(phi), -cos(phi) * sin(alpha), -sin(phi) * sin(alpha), 0},
                    {0, sin(phi), -cos(phi), 0},
                    {0, 0, ro, 1}
            };

            double[][] matrixM = {
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 1d/d},
                    {0, 0, 0, 0}
            };

            for (Edge edge : parallelepiped.getEdges()) {
                for (Point point : edge.getPoints()) {
                    double[][] pointInMatrix = {point.getCoordinates()};
                    double[][] result = MatrixOperations.multiplyMatrices(MatrixOperations.multiplyMatrices(pointInMatrix, matrixV), matrixM);
                    point.setCoordinates(result[0]);
                }
            }

            for (Edge edge : pyramid.getEdges()) {
                for (Point point : edge.getPoints()) {
                    double[][] pointInMatrix = {point.getCoordinates()};
                    double[][] result = MatrixOperations.multiplyMatrices(MatrixOperations.multiplyMatrices(pointInMatrix, matrixV), matrixM);
                    point.setCoordinates(result[0]);
                }
            }

            drawFigure();
        } catch (NumberFormatException e) {
            exceptionPerspectiveLabel.setText("Данные введены неверно");
            exceptionPerspectiveLabel.setVisible(true);
        }
    }

    @FXML
    TextField lightX, lightY, lightZ;

    @FXML
    Label exceptionOnLightLabel;

    public void setLight() {
        double lightX, lightY, lightZ;
        try {
            lightX = Double.parseDouble(this.lightX.getText());
            lightY = Double.parseDouble(this.lightY.getText());
            lightZ = Double.parseDouble(this.lightZ.getText());

            exceptionOnLightLabel.setVisible(false);

            this.lightX.setText("");
            this.lightY.setText("");
            this.lightZ.setText("");

            light = new Point(lightX, lightY, lightZ);
            drawFigures();
        } catch (NumberFormatException e) {
            exceptionOnLightLabel.setVisible(true);
            exceptionOnLightLabel.setText("Данные введены неверно");

        }
    }

    @FXML
    TextField phiAxonometricField, psiField;

    @FXML
    Label exceptionAxonometricLabel;

    public void drawAxonometricProjection() {
        if (pyramid == null || parallelepiped == null) {
            exceptionAxonometricLabel.setText("Фигуры не созданы");
            exceptionAxonometricLabel.setVisible(true);
            return;
        }
        exceptionAxonometricLabel.setVisible(false);
        try {
            double phi = toRadians(Double.parseDouble(phiAxonometricField.getText()));
            double psi = toRadians(Double.parseDouble(psiField.getText()));
            double[][] matrix = new double[][] {
                    {cos(psi), sin(phi) * sin(psi),  0, 0},
                    {0,        cos(phi),             0, 0},
                    {sin(psi), -sin(phi) * cos(psi), 0, 0},
                    {0,        0,                    0, 1}
            };
            transformAndThenDraw(matrix);
        } catch (NumberFormatException e) {
            exceptionAxonometricLabel.setText("Данные введены неверно");
            exceptionAxonometricLabel.setVisible(true);
        }
    }

    @FXML
    TextField aField, lField;

    @FXML
    Label exceptionObliqueLabel;

    public void drawObliqueProjection() {
        if (pyramid == null || parallelepiped == null) {
            exceptionObliqueLabel.setText("Фигуры не созданы");
            exceptionObliqueLabel.setVisible(true);
            return;
        }
        exceptionObliqueLabel.setVisible(false);
        try {
            double a = toRadians(Double.parseDouble(aField.getText()));
            double l = Double.parseDouble(lField.getText());
            double[][] matrix = new double[][] {
                    {1,          0,          0, 0},
                    {0,          1,          0, 0},
                    {l * cos(a), l * sin(a), 0, 0},
                    {0,           0,         0, 1}
            };
            transformAndThenDraw(matrix);
        } catch (NumberFormatException e) {
            exceptionObliqueLabel.setText("Данные введены неверно");
            exceptionObliqueLabel.setVisible(true);
        }
    }

}
