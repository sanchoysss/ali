package by.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pyramid {

    private Edge[] triangles;

    public Pyramid(Edge[] triangles) {
        this.triangles = triangles.clone();
    }

    public static Pyramid createPyramid(double height, double side) {
        Edge[] triangles = new Edge[4];

        triangles[0] = Triangle.createTriangle(side, 0);


        triangles[1] = new Edge(
                triangles[0].getPoints()[0],
                triangles[0].getPoints()[1],
                new Point(0, 0, -height)
        );

        triangles[2] = new Edge(
                triangles[0].getPoints()[1],
                triangles[0].getPoints()[2],
                new Point(0, 0, -height)
        );

        triangles[3] = new Edge(
                triangles[0].getPoints()[2],
                triangles[0].getPoints()[0],
                new Point(0, 0, -height)
        );

        return new Pyramid(triangles);
    }

    public Edge[] getTriangles() {
        return triangles;
    }

    public void setTriangles(Edge[] triangles) {
        this.triangles = triangles;
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        Collections.addAll(edges, triangles);
        return edges;
    }

    public void updateEdges(Point viewer) {
        for (Edge edge : triangles) {
            edge.updateVisibility(viewer);
        }
    }
}
