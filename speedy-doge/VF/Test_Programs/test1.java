package VF.Test_Programs;

import Universal.Math.Geometry.Ellipse;
import Universal.Math.Geometry.Rectangle;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.Feilds.RoverRuckusField;
import VF.Feilds.TestField;
import VF.PointField;

import java.awt.*;

public class test1 {

    public static void main (String[] args){
        RoverRuckusField roverRuckusField = new RoverRuckusField();
        roverRuckusField.setWaypoint(new Pose(60, 60));
        roverRuckusField.destination.assignArbitraryDirection();
        roverRuckusField.generatePath(new Pose(30, -30), 5, 9);


    }
}
