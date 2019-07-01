package org.firstinspires.ftc.teamcode.Universal.Math;

public class Vector3 {
    public double x;
    public double y;
    public double z;

    public Vector3 () {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(double i, double j, double k, double magnitude){
        x = magnitude / Math.sqrt(i * i + k * k + j * j) * i;
        y = magnitude / Math.sqrt(i * i + k * k + j * j) * j;
        z = magnitude / Math.sqrt(i * i + k * k + j * j) * k;
    }

    public Vector3(Vector3 componentForm, double magnitude){
        x = magnitude / componentForm.magnitude() * componentForm.x;
        y = magnitude / componentForm.magnitude() * componentForm.y;
        z = magnitude / componentForm.magnitude() * componentForm.z;
    }

    public void setFromComponentForm(Vector3 componentForm, double magnitude){
        componentForm.scalarMultiply(magnitude / componentForm.magnitude());
        componentForm.copyTo(this);
    }
    //Adds the components of a given Vector3 to this Vector3
    public void add(Vector3 vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }
    //Subtracts the components of a given Vector3 from this Vector3
    public void subtract(Vector3 vector) {
        vector.scalarMultiply(-1);
        add(vector);
    }
    //Adds the components of a given Vector3 to this Vector3
    public void scalarMultiply(double a) {
        x *= a;
        y *= a;
        z *= a;
    }

    //Length of vector
    public double magnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    //Dot Product
    public double dot(Vector3 vector) {
        return vector.x * this.x + vector.y * this.y + vector.z * this.z;
    }

    //Cross Product
    public Vector3 cross(Vector3 vector) {
        Vector3 result = new Vector3();

        result.x = (this.y * vector.z) - (this.z * vector.y);
        result.y = (this.z * vector.x) - (this.x * vector.z);
        result.z = (this.x * vector.y) - (this.y * vector.x);

        return result;
    }

    //Returns angle between two vectors in radians.
    public double angleBetween(Vector3 vector) {
        return Math.acos(this.dot(vector) / (this.magnitude() * vector.magnitude()));
    }

    public void rotateX(double angle){
        Vector2 temp = new Vector2(y, z);
        temp.rotate(angle);
        y = temp.x;
        z = temp.y;
    }

    public void rotateY(double angle){
        Vector2 temp = new Vector2(x, z);
        temp.rotate(angle);
        x = temp.x;
        z = temp.y;
    }

    public void rotateZ(double angle){
        Vector2 temp = new Vector2(x, y);
        temp.rotate(angle);
        x = temp.x;
        y = temp.y;
    }
    public void copyTo(Vector3 intendedVector){
        intendedVector.x = x;
        intendedVector.y = y;
        intendedVector.z = z;
    }
    public String toString(){
        return "(" + x + ", " + y + ", " + z + ")";
    }
}