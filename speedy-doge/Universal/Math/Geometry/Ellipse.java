package Universal.Math.Geometry;

import Universal.Math.Pose;
import Universal.Math.Vector2;

public class Ellipse {
    public double r1, r2;
    public Pose position;

    public Ellipse (Pose position, double r1, double r2) {
        this.position = position;
        this.r1 = r1;
        this.r2 = r2;
    }

    public Vector2 getClosestPoint (Vector2 point) {
        double m = (point.y - position.y) / (point.x - position.x);
        double x = Math.sqrt(1/(Math.pow(1/r1, 2) + Math.pow(m/r2, 2)));
        x *= point.x > position.x ? 1 : -1;
        double y = m * x;
        return new Vector2 (position.x + x, position.y + y);
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
}