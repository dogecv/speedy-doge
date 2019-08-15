package Universal.Motion;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import Universal.UniversalFunctions;

public class TankOdometry {
    public final double ENC_PER_INCH;
    public final double DISTANCE_BETWEEN_WHEELS;

    public double previousLeftVal = 0;
    public double previousRightVal = 0;

    public Pose position;

    public TankOdometry(double epi, double dbw, Pose startingPose){
        ENC_PER_INCH = epi;
        DISTANCE_BETWEEN_WHEELS = dbw;
        position = startingPose;
    }

    public void updateLocation(double leftEncVal, double rightEncVal) {
        updateLocation(leftEncVal - previousLeftVal, rightEncVal - previousRightVal);
        previousLeftVal = leftEncVal;
        previousRightVal = rightEncVal;
    }

    public synchronized void updateLocationInternal(double leftChange, double rightChange){
        leftChange /= ENC_PER_INCH;
        rightChange /= ENC_PER_INCH;
        double angle = 0;
        double radius;
        Vector2 turnVector = new Vector2();
        if (rightChange == leftChange)
            turnVector.setFromPolar(rightChange, position.angle);
        else {
            radius = DISTANCE_BETWEEN_WHEELS / 2 * (leftChange + rightChange) / (rightChange - leftChange);
            angle = (rightChange - leftChange) / (DISTANCE_BETWEEN_WHEELS);
            turnVector.x = radius * Math.cos(angle) - radius;
            turnVector.y = radius * Math.sin(angle);
        }
        turnVector.rotate(position.angle);
        position.x += turnVector.x;
        position.y += turnVector.y;
        position.angle += angle;

        UniversalConstants.robot.refreshLocation(position);
    }
}
