package VF.Objects;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import VF.Boundry;
/**
 * Generates an obstical which prevents vector fields from pushing past the field wall
 * Field: All
 **/
public class FieldWall implements Boundry {
    private boolean isActive;

    public Vector2 interact(Pose point, Vector2 vector) {
        if (isActive) {
            if (point.x > 72 - UniversalConstants.EFFECTIVE_ROBOT_RADIUS && vector.x > 0)
                vector.x = 0;
            if (point.x < -72 + UniversalConstants.EFFECTIVE_ROBOT_RADIUS && vector.x < 0)
                vector.x = 0;
            if (point.y > 72 - UniversalConstants.EFFECTIVE_ROBOT_RADIUS && vector.y > 0)
                vector.y = 0;
            if (point.y < -72 + UniversalConstants.EFFECTIVE_ROBOT_RADIUS && vector.y < 0)
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