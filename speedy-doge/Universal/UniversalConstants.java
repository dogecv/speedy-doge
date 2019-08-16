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

    public static Robot getRobot(Pose location){
        return new Robot(location);
    }

    //TODO: turn this into a function, not a constant
    public static double MINIMUM_APPROACH_DISTANCE = 20;
}
