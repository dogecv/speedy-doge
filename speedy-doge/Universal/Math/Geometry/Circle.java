package Universal.Math.Geometry;

import Universal.Math.Pose;

public class Circle extends Ellipse {

    public Circle (Pose position, double radius) {
        super(position, radius, radius);
    }
}
