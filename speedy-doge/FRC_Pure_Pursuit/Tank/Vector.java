package FRC_Pure_Pursuit.Tank;

public class Vector {

    private final int n;         // length of the vector
    public double[] data;       // array of vector's components

    // create the zero vector of length n
    public Vector(int n) {
        this.n = n;
        this.data = new double[n];
    }

    // create a vector from an array
    public Vector(double[] data) {
        n = data.length;

        // defensive copy so that client can't alter our copy of data[]
        this.data = new double[n];
        for (int i = 0; i < n; i++)
            this.data[i] = data[i];
    }

    // create a vector from either an array or a vararg list
    // this constructor uses Java's vararg syntax to support
    // a constructor that takes a variable number of arguments, such as
    // Vector x = new Vector(1.0, 2.0, 3.0, 4.0);
    // Vector y = new Vector(5.0, 2.0, 4.0, 1.0);
/*
    public Vector(double... data) {
        n = data.length;

        // defensive copy so that client can't alter our copy of data[]
        this.data = new double[n];
        for (int i = 0; i < n; i++)
            this.data[i] = data[i];
    }
*/
    // return the length of the vector
    public int length() {
        return n;
    }

    // return the inner product of this Vector a and b
    public double dot(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        double sum = 0.0;
        for (int i = 0; i < n; i++)
            sum = sum + (this.data[i] * that.data[i]);
        return sum;
    }

    // return the Euclidean norm of this Vector
    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    // return the Euclidean distance between this and that
    public double distanceTo(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        return this.minus(that).magnitude();
    }

    // return this + that
    public Vector plus(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++)
            c.data[i] = this.data[i] + that.data[i];
        return c;
    }

    // return this - that
    public Vector minus(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++)
            c.data[i] = this.data[i] - that.data[i];
        return c;
    }

    // return the corresponding coordinate
    public double cartesian(int i) {
        return data[i];
    }

    // create and return a new object whose value is (this * factor)
    @Deprecated
    public Vector times(double factor) {
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++)
            c.data[i] = factor * data[i];
        return c;
    }

    // create and return a new object whose value is (this * factor)
    public Vector scale(double factor) {
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++)
            c.data[i] = factor * data[i];
        return c;
    }


    // return the corresponding unit vector
    public Vector direction() {
        if (this.magnitude() == 0.0)
            throw new ArithmeticException("zero-vector has no direction");
        return this.scale(1.0 / this.magnitude());
    }

    // return a string representation of the vector
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append('(');
        for (int i = 0; i < n; i++) {
            s.append(data[i]);
            if (i < n-1) s.append(", ");
        }
        s.append(')');
        return s.toString();
    }


    // test client

    }

