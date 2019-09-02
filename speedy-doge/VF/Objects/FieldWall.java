package VF.Objects;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import VF.Boundary;
/**
 * Generates an obstacle which prevents vector fields from pushing past the field wall
 **/
public class FieldWall implements Boundary {
    private boolean isActive;

    public Vector2 interact(Pose point, Vector2 vector) {

        //creates a robot at the input Pose
        Robot robot = UniversalConstants.getRobot(point);

        //if the boundary is currently active...
        if (isActive) {

            //declares effectiveRadius, the magnitude of the vector pointing to the closest point on the robot to the wall
            double effectiveRadius;

            //if point is closer to the top or bottom wall than either side wall...
            if(Math.abs(Math.tan(point.toVector().angle())) > 1)
                effectiveRadius = robot.polarVector(Math.PI / 2 - point.angle).magnitude();

            //if point is closer to the left or right wall than the top or bottom walls...
            else
                effectiveRadius = robot.polarVector(-point.angle).magnitude();

            //if the vector is out of bounds, correct it
            if (point.x > 72 - effectiveRadius && vector.x > 0)
                vector.x = 0;
            if (point.x < -72 + effectiveRadius && vector.x < 0)
                vector.x = 0;
            if (point.y > 72 - effectiveRadius && vector.y > 0)
                vector.y = 0;
            if (point.y < -72 + effectiveRadius && vector.y < 0)
                vector.y = 0;
        }
        return vector;
    }

    public void activate(){
        isActive = true;
    }
    public void deactivate(){
        isActive = false;
    }
    public boolean isActive(){
        return isActive;
    }
}