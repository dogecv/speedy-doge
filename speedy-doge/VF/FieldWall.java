package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;

public class FieldWall implements Obstical {

    public Vector2 interact(Pose point, Vector2 vector) {
        if(point.x > 72 - UniversalConstants.EFFECTIVE_ROBOT_RADIUS && vector.x > 0)
            vector.x = 0;
        if(point.x < -72 + UniversalConstants.EFFECTIVE_ROBOT_RADIUS && vector.x < 0)
            vector.x = 0;
        if(point.y > 72 - UniversalConstants.EFFECTIVE_ROBOT_RADIUS && vector.y > 0)
            vector.y = 0;
        if(point.y < -72 + UniversalConstants.EFFECTIVE_ROBOT_RADIUS && vector.y < 0)
            vector.y = 0;
        return vector;
    }

}