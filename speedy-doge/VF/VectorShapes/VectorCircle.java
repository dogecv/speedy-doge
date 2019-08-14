package VF.VectorShapes;

import Universal.Math.Geometry.Circle;
import Universal.Math.Pose;

public class VectorCircle extends VectorShape{

    public VectorCircle(){
        super(new Pose(), 1, 1);
        shape = new Circle(new Pose(), 1);
    }
    public VectorCircle(Pose location, double radius, double strength, double falloff){
        super(location, strength, falloff);
        shape = new Circle(location, radius);
    }


}
