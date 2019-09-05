package VF.Feilds;

import Universal.Math.Geometry.Rectangle;
import Universal.Math.Geometry.Shape;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.Objects.FieldWall;
import VF.PointField;
import VF.VectorField;
import VF.VectorShapes.VectorCircle;
import VF.VectorShapes.VectorRectangle;

import java.util.ArrayList;

public class TestField extends VectorField {

    public TestField(){
        super(new ArrayList<>(), new ArrayList<>());
        FieldWall wall= new FieldWall();
        boundaries.add(wall);
        Rectangle rectangle = new Rectangle(new Pose(), 50, 50);
        obstacles.add(new VectorRectangle(new Pose(), rectangle.width, rectangle.height, 24,  0.01));
    }
}
