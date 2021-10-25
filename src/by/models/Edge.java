package by.models;

/**
 * Данный класс представляет собой грань
 */
public class Edge {

    private Point[] points;
    private boolean visible;

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
}
