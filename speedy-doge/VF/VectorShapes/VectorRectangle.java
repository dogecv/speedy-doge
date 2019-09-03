package VF.VectorShapes;

import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Geometry.Rectangle;
import Universal.Math.Geometry.Shape;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.Objects.Robot;
import VF.PointField;

import java.awt.*;

/**
 * VectorShape in the form of a rectangle
 */
public class VectorRectangle extends VectorShape {

    double width=1, height = 1;
    public VectorRectangle(){
        super(new Pose(), 1, 1);
        shape = new Rectangle(1,1);
    }
    public VectorRectangle(Pose location, double strength, double falloff, Shape shape){
        super(location, strength, falloff);
        this.shape = shape;

    }
    public VectorRectangle(Pose location, double width, double height, double strength, double falloff){
        super(location, strength, falloff);
        shape = new Rectangle(location, width, height);
        this.width = width;
        this.height = height;
    }



}
