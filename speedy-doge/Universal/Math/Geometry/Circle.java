package Universal.Math.Geometry;

import Universal.Math.Pose;

/**
 * Represents an Ellipse with an r1 that equals r2
 */
public class Circle extends Ellipse {

    public Circle (Pose position, double radius) {
        super(position, radius, radius);
    }
}
