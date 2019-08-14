package FRC_Pure_Pursuit.Tank;

public class Velocities {


    public static double[] calculateWheelVelocity(double curvature, double trackWidth, double targetVel){
        //L = V * (2 + CT)/2
        //R = V * (2 − CT)/2
        double left = targetVel * (2 + curvature*trackWidth)/2.0;
        double right = targetVel * (2 - curvature*trackWidth)/2.0;

        return new double[] {left, right};
    }

    public static double[] motorSpeedClosedLoop(double[] targetVels, double[] actualVels, double kV, double kP){
        double[] motorPowers = new double[2];
        for(int i = 0; i<2; i++){ //do left/right side
            motorPowers[i] += targetVels[i] * kV; //Feedforward
            motorPowers[i] += (targetVels[i]- actualVels[i]) * kP; //Feedback
        }
        return motorPowers;
    }


}
