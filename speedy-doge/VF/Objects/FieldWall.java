package VF.Objects;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import VF.Boundary;
/**
 * Generates an obstacle which prevents vector fields from pushing past the field wall
 * Field: All
 **/
public class FieldWall implements Boundary {
    private boolean isActive;

    public Vector2 interact(Pose point, Vector2 vector) {
        Robot robot = UniversalConstants.getRobot(point);
        if (isActive) {
            double effectiveRadius = 0;

            if(Math.abs(Math.tan(point.toVector().angle())) > 1)
                effectiveRadius = robot.polarVector(Math.PI / 2 - point.angle).magnitude();
            else
                effectiveRadius = robot.polarVector(-point.angle).magnitude();

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