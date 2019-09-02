package Universal;

import Universal.Math.Pose;
import VF.Objects.*;

public class UniversalConstants {

    //how big we treat the robot when avoiding obstacles
    public static final double EFFECTIVE_ROBOT_RADIUS = 9 * Math.sqrt(2) + 2;

    public static Robot getRobot(Pose location){
        return new Robot(location);
    }

    //TODO: turn this into a function, not a constant
    public static double MINIMUM_APPROACH_DISTANCE = 20;
}
