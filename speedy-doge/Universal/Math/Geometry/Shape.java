package Universal.Math.Geometry;

import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Pose;
import Universal.Math.Vector2;

public abstract class Shape {
    public Pose position = new Pose();

    public abstract Vector2 getClosestPoint(Vector2 point);

    public abstract Vector2 getClosestVector(Vector2 point);
}
