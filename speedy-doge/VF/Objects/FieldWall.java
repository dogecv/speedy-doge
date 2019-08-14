package VF.Objects;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import Universal.UniversalFunctions;
import VF.Boundry;
/**
 * Generates an obstical which prevents vector fields from pushing past the field wall
 * Field: All
 **/
public class FieldWall implements Boundry {
    private boolean isActive;

    //TODO: stop using EFFECTIVE_ROBOT_RADIUS and calculate the shortest distance between the robot and the wall
    public Vector2 interact(Pose point, Vector2 vector) {
        if (isActive) {
            double effectiveRadius = 0;

            if(Math.abs(Math.tan(point.toVector().angle())) > 1)
                effectiveRadius = UniversalConstants.robot.polarVector(Math.PI / 2 - point.angle).magnitude();
            else
                effectiveRadius = UniversalConstants.robot.polarVector(-point.angle).magnitude();

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