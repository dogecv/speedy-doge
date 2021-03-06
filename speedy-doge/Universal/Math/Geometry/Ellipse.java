package Universal.Math.Geometry;

import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Pose;
import Universal.Math.Vector2;

/**
* TESTED
*/
public class Ellipse extends Shape{
    public double r1, r2;

    public Ellipse (Pose position, double r2, double r1) {
        this.location = position;
        this.r1 = r1;
        this.r2 = r2;
    }

    //TODO: return an exact solution instead of an approximation
    public Vector2 getClosestPoint (Vector2 point) {
        point.subtract(location.toVector());
        point.rotate(-location.angle);
        double newTheta = Math.atan2(point.y * r2, point.x * r1);
        point = new Vector2(r2 * Math.cos(newTheta), r1 * Math.sin(newTheta));
        point.rotate(location.angle);
        point.add(location.toVector());
        return point;
    }

    /*
    returns the slope of the ellipse at a given angle
     */
    public double slope (double theta) {
        return -1/Math.tan(theta) * Math.pow(r2/r1, 2);
    }

    /*
    returns the angle of a point on the ellipse
     */
    public double angle (Pose position) {

        //normalizes x and y relative to the ellipse's location
        double x = position.x - this.location.x;
        double y = position.y - this.location.y;

        //computes the angle
        double output = Math.acos(x / Math.hypot(x, y));
        if(y < 0)
            output *= -1;

        return output;
    }

    /*
    returns a vector relative to the center of the ellipse pointing towards the closest point on the ellipse to a given point
     */
    public Vector2 getClosestVector(Vector2 pose){
        Vector2 output = getClosestPoint(pose);
        output.subtract(location.toVector());
        return output;
    }
}
