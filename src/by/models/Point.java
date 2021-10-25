package by.models;

/**
 * Класс, объекты которого будут представлять сущность точки
 */
public class Point {

    private double[] coordinates;

    /**
     * Конструктор принимает координаты и хранит их значения в массиве
     * @param x координата х
     * @param y координата у
     * @param z координата z
     */
    public Point(double x, double y, double z) {
        coordinates = new double[4];
        coordinates[0] = x;
        coordinates[1] = y;
        coordinates[2] = z;
        coordinates[3] = 1;
    }

    /**
     * Конструктор принимает на вход точку и копирует ее значения
     * @param point точка, значение которой будет скопировано
     */
    public Point(Point point) {
        this.coordinates = new double[4];
        this.coordinates[0] = point.coordinates[0];
        this.coordinates[1] = point.coordinates[1];
        this.coordinates[2] = point.coordinates[2];
        this.coordinates[3] = point.coordinates[3];
    }

    /**
     * Конструктор, который создает точку с координатами (0;0;0)
     */
    public Point() {
        coordinates = new double[4];
        coordinates[0] = coordinates[1] = coordinates[2] = 0;
        coordinates[3] = 1;
    }


    /**
     * @return координата х.
     */
    public double getX() {
        return coordinates[0];
    }

    /**
     * @return коорината у.
     */
    public double getY() {
        return coordinates[1];
    }

    /**
     * @return координата z.
     */
    public double getZ() {
        return coordinates[2];
    }

    /**
     * Метод заменяет координату x
     * @param x новая координата x
     */
    public void setX(double x) {
        coordinates[0] = x;
    }

    /**
     * Метод заменяет координату y
     * @param y новая координата y
     */
    public void setY(double y) {
        coordinates[1] = y;
    }

    /**
     * @param z новая координата z.
     * Метод заменяет координату z
     */
    public void setZ(double z)
    {
        coordinates[2] = z;
    }

    /**
     * @return массив координат
     */
    public double[] getCoordinates() {
        return coordinates;
    }

    /**
     * Метод заменяет координаты текущей точки на те, которые передаются в метод
     * @param point принимает точку в качестве входного параметра.
     */
    public void setCoordinates(Point point) {
        coordinates[0] = point.coordinates[0];
        coordinates[1] = point.coordinates[1];
        coordinates[2] = point.coordinates[2];
    }

    public void setCoordinates(double[] coordinates) {
        System.arraycopy(coordinates, 0, this.coordinates, 0, coordinates.length);
    }

    /**
     * @return информация о классе в формате строки.
     */
    @Override
    public String toString() {
        return "Point( " + coordinates[0] + " , " + coordinates[1] + " , " + coordinates[2] + " )";
    }
}
