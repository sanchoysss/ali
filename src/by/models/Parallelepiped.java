package by.models;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parallelepiped {

    private Edge[] triangles;
    private Edge[] rectangles;

    public Parallelepiped(Edge[] triangles, Edge[] rectangles) {
        this.triangles = triangles.clone();
        this.rectangles = rectangles.clone();
    }

    public static Parallelepiped createParallelepiped(double height, double side) {
        Edge[] triangles = new Edge[2];
        Edge[] rectangles = new Edge[3];

        triangles[0] = Triangle.createTriangle(side, 0);
        triangles[1] = Triangle.createTriangle(side, height);

        rectangles[0] = new Edge(
                triangles[1].getPoints()[0],
                triangles[1].getPoints()[1],
                triangles[0].getPoints()[1],
                triangles[0].getPoints()[0]
        );

        rectangles[1] = new Edge(
                triangles[1].getPoints()[1],
                triangles[1].getPoints()[2],
                triangles[0].getPoints()[2],
                triangles[0].getPoints()[1]
        );

        rectangles[2] = new Edge(
                triangles[1].getPoints()[2],
                triangles[1].getPoints()[0],
                triangles[0].getPoints()[0],
                triangles[0].getPoints()[2]
        );

        return new Parallelepiped(triangles, rectangles);
    }

    public Edge[] getTriangles() {
        return triangles;
    }

    public void setTriangles(Edge[] triangles) {
        this.triangles = triangles;
    }

    public Edge[] getRectangles() {
        return rectangles;
    }

    public void setRectangles(Edge[] rectangles) {
        this.rectangles = rectangles;
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>(triangles.length + rectangles.length);
        Collections.addAll(edges, triangles);
        Collections.addAll(edges, rectangles);
        return edges;
    }

    public void updateEdges(Point viewer, Point light) {
        for (int i = 0; i < triangles.length; i++) {
            triangles[i].updateVisibility(viewer);
            triangles[i].updateColor(light);
            if (i == 1) {
                triangles[i].setVisible(!triangles[i].isVisible());
                double green = ((50 + 205 * (triangles[i].calculateAngle(viewer) / 180d)) / 255);
                if (green < 0.2) green = 0.2;
                triangles[i].setColor(new Color(0, green, 0, 1));
            }
        }

        for (Edge rectangle : rectangles) {
            rectangle.updateVisibility(viewer);
            rectangle.updateColor(light);
        }
    }

    public Point getClosestPoint() {
        Point closestPoint = triangles[0].getPoints()[0];
        for (Edge edge : getEdges()) {
            for (Point point : edge.getPoints()) {
                if (point.getZ() > closestPoint.getZ()) {
                    closestPoint = point;
                }
            }
        }
        return closestPoint;
    }
}
