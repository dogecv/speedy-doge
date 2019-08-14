package VF.VectorShapes;

import Universal.Math.Geometry.Rectangle;
import Universal.Math.Pose;
/**
 * VectorShape in the form of a rectangle
 */
public class VectorRectangle extends VectorShape {

    public VectorRectangle(){
        super(new Pose(), 1, 1);
        shape = new Rectangle(1,1);
    }
    public VectorRectangle(Pose location, double width, double height, double strength, double falloff){
        super(location, strength, falloff);
        shape = new Rectangle(location, width, height);
    }



}
