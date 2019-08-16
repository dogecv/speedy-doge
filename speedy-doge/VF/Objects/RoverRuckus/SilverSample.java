package VF.Objects.RoverRuckus;

import Universal.Math.Pose;
import VF.Objects.Robot;
import VF.VectorShapes.VectorCircle;

/**
 * Generates the vector field eminating from silver samples
 * Field: Rover Ruckus
 **/
public class SilverSample extends VectorCircle {
    public SilverSample(Pose location){
        super(location, 2.75, 4, 1.5);
    }
    
    public SilverSample(Pose location, double strength, double falloff){
        super(location, 2.75, strength, falloff);
    }
}
