package Universal.Math.Geometry;

import Universal.Math.Pose;
import Universal.Math.Vector2;

public abstract class Shape {
    Pose position = new Pose();

    public abstract Vector2 getClosestPoint(Vector2 point);
}
