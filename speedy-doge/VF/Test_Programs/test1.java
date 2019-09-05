package VF.Test_Programs;

import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Geometry.Ellipse;
import Universal.Math.Geometry.Rectangle;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import VF.Feilds.RoverRuckusField;
import VF.Feilds.TestField;
import VF.Objects.Robot;
import VF.PointField;
import VF.VectorShapes.VectorRectangle;

import java.awt.*;

public class test1 {

    public static void main (String[] args){
/*
        RoverRuckusField roverRuckusField = new RoverRuckusField();
        roverRuckusField.setWaypoint(new Pose(60, 60));
        roverRuckusField.destination.assignArbitraryDirection();
        roverRuckusField.generatePath(new Pose(-30, -30), 1, 9);
*/


        TestField testField = new TestField();
        testField.setWaypoint(new Pose(50,0));
        testField.destination.assignArbitraryDirection();
        System.out.println(testField.destination.location);
        testField.generatePath(new Pose(-50,0), 5, 9);
    }
}
