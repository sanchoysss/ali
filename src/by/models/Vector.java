package by.models;

import static java.lang.Math.sqrt;

public class Vector {

    private final double[] values;

    public Vector(double... values) {
        this.values = new double[3];
        System.arraycopy(values, 0, this.values, 0, values.length);
    }

    public double multiply(Vector vector) {
        return this.values[0] * vector.values[0] + this.values[1] * vector.values[1] +
                this.values[2] * vector.values[2];
    }

    public double multiplyLengths(Vector vector) {
        return sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]) *
                sqrt(vector.values[0] * vector.values[0] + vector.values[1] * vector.values[1] +
                        vector.values[2] * vector.values[2]);
    }

    public double[] getValues() {
        return values;
    }
}
