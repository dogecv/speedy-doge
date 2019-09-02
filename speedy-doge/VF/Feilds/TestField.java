package VF.Feilds;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.Objects.FieldWall;
import VF.VectorField;
import VF.VectorShapes.VectorRectangle;

import java.util.ArrayList;

public class TestField extends VectorField {

    public TestField(){
        super(new ArrayList<>(), new ArrayList<>());
        FieldWall wall= new FieldWall();

        //obstacles.add(new VectorRectangle(new Pose(), 50, 50, 24,1/2));
    }
}
