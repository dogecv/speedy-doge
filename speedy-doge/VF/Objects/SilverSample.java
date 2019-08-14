package VF.Objects;

import Universal.Math.Pose;
import VF.VectorShapes.VectorCircle;

public class SilverSample extends VectorCircle {
    public SilverSample(Pose location, double strength, double falloff){
        super(location, 2.75, strength, falloff);
    }
}
