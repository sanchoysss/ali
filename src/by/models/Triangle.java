package by.models;

import static java.lang.Math.sqrt;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

public class Triangle {

    public static Edge createTriangle(double side, double z) {
        double radius = side / sqrt(3);


        double x = radius * cos( toRadians(30) );
        double y = radius * sin( toRadians(30) );

        Point point1 = new Point(x, y, z);
        Point point2 = new Point(-x, y, 0);
        Point point3 = new Point(0, -2 * y, 0);

        return new Edge(point1, point2, point3);
    }
}
