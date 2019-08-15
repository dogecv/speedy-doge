package VF.Objects.RoverRuckus;

import Universal.Math.Pose;
import VF.VectorFieldComponent;
import VF.VectorShapes.VectorEllipse;


/**
 * Generates the vector field eminating from the crater lip
 * Field: Rover Ruckus
 **/
public class Crater extends VectorEllipse {
    public Crater(Pose location, double strength, double falloff) {
        super(location, 46.4, 51.4, strength, falloff);
    }
}
