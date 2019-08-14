package VF.Objects;

import Universal.Math.Pose;
import VF.VectorFieldComponent;
import VF.VectorShapes.VectorEllipse;

public class Crater extends VectorEllipse {
    public Crater(Pose location, double strength, double falloff) {
        super(location, 46.4, 51.4, strength, falloff);
    }
}
