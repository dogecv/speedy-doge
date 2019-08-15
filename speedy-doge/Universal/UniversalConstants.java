package Universal;

import Universal.Math.Pose;
import VF.Objects.*;
import VF.Boundry;
import VF.Objects.RoverRuckus.Crater;
import VF.Objects.RoverRuckus.Lander;
import VF.Objects.RoverRuckus.SilverSample;
import VF.VectorField;
import VF.VectorFieldComponent;

import java.util.ArrayList;

public class UniversalConstants {

    //how big we treat the robot when avoiding obsticals
    public static final double EFFECTIVE_ROBOT_RADIUS = 9 * Math.sqrt(2) + 2;

    public static Robot robot = new Robot(new Pose());



    public static VectorField initializeRoverRuckusField(){
        FieldWall wall= new FieldWall();

        Lander lander = new Lander(new Pose(0, 0, Math.PI/4), 1, 1);

        Crater crater1 = new Crater(new Pose(-72, 72, 0), 1, 1);
        Crater crater2 = new Crater(new Pose(72, -72, 0), 1, 1);

        SilverSample centerSampleq1 = new SilverSample(new Pose(36,36,0), 1, 1);
        SilverSample centerSampleq2 = new SilverSample(new Pose(-36,36,0), 1, 1);
        SilverSample centerSampleq3 = new SilverSample(new Pose(-36,-36,0), 1, 1);
        SilverSample centerSampleq4 = new SilverSample(new Pose(36,-36,0), 1, 1);

        SilverSample leftSampleq1 = new SilverSample(new Pose(25,47,0), 1, 1);
        SilverSample leftSampleq2 = new SilverSample(new Pose(-47,25,0), 1, 1);
        SilverSample leftSampleq3 = new SilverSample(new Pose(-25,-47,0), 1, 1);
        SilverSample leftSampleq4 = new SilverSample(new Pose(47,-25,0), 1, 1);

        SilverSample rightSampleq1 = new SilverSample(new Pose(47,25,0), 1, 1);
        SilverSample rightSampleq2 = new SilverSample(new Pose(-25,47,0), 1, 1);
        SilverSample rightSampleq3 = new SilverSample(new Pose(-47,-25,0), 1, 1);
        SilverSample rightSampleq4 = new SilverSample(new Pose(25,-47,0), 1, 1);

        ArrayList<VectorFieldComponent> obsticals = new ArrayList<>();

        obsticals.add(lander);
        obsticals.add(crater1);
        obsticals.add(crater2);

        obsticals.add(centerSampleq1);
        obsticals.add(centerSampleq2);
        obsticals.add(centerSampleq3);
        obsticals.add(centerSampleq4);

        obsticals.add(leftSampleq1);
        obsticals.add(leftSampleq2);
        obsticals.add(leftSampleq3);
        obsticals.add(leftSampleq4);

        obsticals.add(rightSampleq1);
        obsticals.add(rightSampleq2);
        obsticals.add(rightSampleq3);
        obsticals.add(rightSampleq4);

        ArrayList<Boundry> boundries = new ArrayList<>();

        boundries.add(wall);

        VectorField rr2Field = new VectorField(obsticals,boundries);

        return rr2Field;
    }
}
