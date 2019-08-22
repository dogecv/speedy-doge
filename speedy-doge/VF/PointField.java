package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import Universal.UniversalFunctions;
import VF.Objects.Robot;

/**
 * VectorFieldComponent that when interacted with, directs position away from location and towards target
 * TESTED
 */
public class PointField extends VectorFieldComponent {
    public PointField (Pose location, double strength, double falloff) {
        super(location, strength, falloff);
    }

    public Vector2 interact(Pose position) {
        Robot robot = UniversalConstants.getRobot(position);
        //zeroes the field at 0, 0, 0 and translates the position and destination to match
        Vector2 dest = getTarget().toVector().clone();
        dest.subtract(location.toVector());
        Vector2 point = new Vector2();
        point.x = position.x - location.x;
        point.y = position.y - location.y;
        point.rotate(-dest.angle());

        //creates output vector and sets its magnitude
        Vector2 output = new Vector2(point.x, point.y);
        double strength = getStrength(output.magnitude() - robot.getClosestPoint(position).magnitude());

        //if the obstacle is in the way...
        if(UniversalFunctions.normalizeAngle180Radians(output.angle()) > Math.acos(output.magnitude() / dest.magnitude())){
            //refedines the vector as perpendicular to its original direction
            output.setFromPolar(strength, output.angle() + Math.PI / 2);
            if(point.y > 0)
                output.setFromPolar(strength, -output.angle());
          	output.rotate(-dest.angle());
    		output.x *= -1;
        }

        //if the obstacle is out of the way...
        else {

            //shoot straight for the destination
            output = new Vector2(dest.x - position.x, dest.y - position.y);
            output.setFromPolar(strength, output.angle());
        }
        return output;
    }
}
