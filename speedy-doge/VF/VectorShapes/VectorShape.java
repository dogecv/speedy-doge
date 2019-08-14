package VF.VectorShapes;

import Universal.Math.Geometry.Rectangle;
import Universal.Math.Geometry.Shape;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.PointField;

public class VectorShape extends PointField {
    public Shape shape;

    public VectorShape(){
        super(new Pose(), 1, 1);
        shape = new Rectangle(1,1);
    }
    public VectorShape(Pose location, double strength, double falloff){
        super(location, strength, falloff);
    }

    @Override
    public Vector2 interact(Pose pose){
        Vector2 vectorLocation = shape.getClosestPoint(pose.toVector());
        location = new Pose(vectorLocation.x, vectorLocation.y, location.angle);
        return super.interact(pose);
    }
}
