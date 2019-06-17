package Universal.Math.Geometry;

import Universal.Math.Pose;

public interface Shape {
    Pose location = new Pose();

    public void getClosestDistance(Pose pose);
}
