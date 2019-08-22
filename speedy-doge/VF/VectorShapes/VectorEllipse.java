package VF.VectorShapes;

import Universal.Math.Geometry.Ellipse;
import Universal.Math.Geometry.Rectangle;
import Universal.Math.Pose;
import VF.Objects.Robot;

/**
 * VectorShape in the form of an Ellipse
 */
public class VectorEllipse extends VectorShape{

    public VectorEllipse(){
        super(new Pose(), 1, 1);
        shape = new Ellipse(new Pose(), 1, 1);
    }
    public VectorEllipse(Pose location, double r1, double r2, double strength, double falloff){
        super(location, strength, falloff);
        shape = new Ellipse(location, r1, r2);
    }
}
