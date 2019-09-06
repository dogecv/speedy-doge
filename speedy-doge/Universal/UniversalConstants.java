package Universal;

import Universal.Math.Pose;
import VF.Objects.*;

/**
 * Contains miscellaneous constants
 */
public class UniversalConstants {

    /*
    how big we treat the robot when avoiding obstacles
     */
    public static final double EFFECTIVE_ROBOT_RADIUS = 9 * Math.sqrt(2) + 2;

    /*
    Used to initialize a robot object, modify this function to return a robot of different shapes using the Robot.add() method
    */
    public static Robot getRobot(Pose location){
        return new Robot(location);
    }

    //TODO: turn this into a function, not a constant
    public static double MINIMUM_APPROACH_DISTANCE = 20;

    public static double AVOIDANCE_THRESHOLD = 3;
}
