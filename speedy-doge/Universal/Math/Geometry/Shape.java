package Universal.Math.Geometry;

import Universal.Math.Pose;

public abstract class Shape {
    Pose location = new Pose();

    public abstract double getClosestDistance(Pose pose);
}
