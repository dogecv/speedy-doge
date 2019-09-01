package Universal.Math.Geometry;

import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Pose;
import Universal.Math.Vector2;

/**
* TESTED
*/
public class Ellipse extends Shape{
    public double r1, r2;
    public Pose position;

    public Ellipse (Pose position, double r2, double r1) {
        this.position = position;
        this.r1 = r1;
        this.r2 = r2;
    }

    //TODO: return an exact solution instead of an approximation
    public Vector2 getClosestPoint (Vector2 point) {
        point.subtract(position.toVector());
        point.rotate(-position.angle);
        double newTheta = Math.atan2(point.y * r2, point.x * r1);
        point = new Vector2(r2 * Math.cos(newTheta), r1 * Math.sin(newTheta));
        point.rotate(position.angle);
        point.add(position.toVector());
        return point;
    }

    public double slope (double theta) {
        return -1/Math.tan(theta) * Math.pow(r2/r1, 2);
    }

    public double angle (Pose position) {
        double x = position.x - this.position.x;
        double y = position.y - this.position.y;
        double output = Math.acos(x / Math.hypot(x, y));
        if(y < 0)
            output *= -1;
        return output;
    }

    public Vector2 getClosestVector(Vector2 pose){
        Vector2 output = getClosestPoint(pose);
        output.subtract(position.toVector());
        return output;
    }
}
