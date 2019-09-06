package VF.VectorShapes;

import Universal.Math.Geometry.Rectangle;
import Universal.Math.Geometry.Shape;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.PointField;
/**
 * PointField whose location is determined by finding the shortest distance between the robot and a given Shape
 */
public class VectorShape extends PointField {
    public Shape shape;

    public VectorShape(){
        super(new Pose(), 1, 1);
        shape = new Rectangle(1,1);
    }
    public VectorShape(Pose location, double strength, double falloff){
        super(location, strength, falloff);
    }

    /*
    places the PointField at the location on shape that is closest to pose, then creates the corresponding output vector
     */
    @Override
    public Vector2 interact(Pose pose){
        Vector2 vectorLocation = shape.getClosestPoint(pose.toVector());
        setLocation(new Pose(vectorLocation.x, vectorLocation.y));
        return super.interact(pose);
    }

    public void setLocation(Pose loc){
  	   location = loc;
       //location.x += shape.location.x;
       //location.y += shape.location.y;
    }
}
