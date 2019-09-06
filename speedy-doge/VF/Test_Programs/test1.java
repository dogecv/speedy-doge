package VF.Test_Programs;

import Universal.Math.Pose;
import VF.Feilds.RoverRuckusField;

import java.awt.*;

public class test1 {

    public static void main (String[] args) {

        RoverRuckusField roverRuckusField = new RoverRuckusField();
        roverRuckusField.setWaypoint(new Pose(15, 15));
        roverRuckusField.destination.assignArbitraryDirection();
        roverRuckusField.generatePath(new Pose(60, 60), 1, 9);


/*
        TestField testField = new TestField();
        testField.setWaypoint(new Pose(50,0));
        testField.destination.assignArbitraryDirection();
        System.out.println(testField.destination.location);
        testField.generatePath(new Pose(-50,0), 5, 9);*/
    }
}
