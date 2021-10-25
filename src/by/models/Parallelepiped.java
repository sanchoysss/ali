package by.models;

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
        triangles[1] = Triangle.createTriangle(side, 1);

        rectangles[0] = new Edge(
                triangles[1].getPoints()[0],
                triangles[1].getPoints()[1],
                triangles[0].getPoints()[0],
                triangles[0].getPoints()[1]
        );

        rectangles[1] = new Edge(
                triangles[1].getPoints()[1],
                triangles[1].getPoints()[2],
                triangles[0].getPoints()[1],
                triangles[0].getPoints()[2]
        );

        rectangles[2] = new Edge(
                triangles[1].getPoints()[2],
                triangles[1].getPoints()[0],
                triangles[0].getPoints()[2],
                triangles[0].getPoints()[0]
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
}
