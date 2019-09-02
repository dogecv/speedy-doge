package VF.Test_Programs;

import Universal.Math.Geometry.Ellipse;
import Universal.Math.Geometry.Rectangle;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import VF.Feilds.RoverRuckusField;
import VF.Feilds.TestField;
import VF.Objects.Robot;
import VF.PointField;

import java.awt.*;

public class test1 {

    public static void main (String[] args){

        RoverRuckusField roverRuckusField = new RoverRuckusField();
        roverRuckusField.setWaypoint(new Pose(30, -30));
        roverRuckusField.destination.assignArbitraryDirection();
        roverRuckusField.generatePath(new Pose(60, 60), 5, 9);

    }
}
