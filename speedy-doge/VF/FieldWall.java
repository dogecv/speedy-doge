package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;

public class FieldWall implements Obstical {
    public final double threshold = 27;

    public Vector2 interact(Pose point, Vector2 vector) {
        if(point.x > 72 - threshold && vector.x > 0)
            vector.x = 0;
        if(point.x < -72 + threshold && vector.x < 0)
            vector.x = 0;
        if(point.y > 72 - threshold && vector.y > 0)
            vector.y = 0;
        if(point.y < -72 + threshold && vector.y < 0)
            vector.y = 0;
        return vector;
    }

}