package Universal.Motion;

import Universal.UniversalFunctions;

public class MotionProfile {
    public double desiredVelocity, startingVelocity, startTime, maxJerk;
    private double intermediateTime = 0;
    State state = State.CONCAVE;
    Direction direction = Direction.INCREASING;

    //Piece of the piecewise velocity curve
    public enum State {
        CONCAVE,
        CONVEX
    }

    public enum Direction {
        INCREASING,
        DECREASING
    }

    public MotionProfile(double maxJerk) {
        desiredVelocity = 0;
        this.maxJerk = maxJerk;
    }

    //create a profile to transition between two velocities
    public void refreshProfile(double startingVelocity, double desiredVelocity) {
        this.startingVelocity = startingVelocity;
        this.desiredVelocity = desiredVelocity;
        direction = Direction.INCREASING;
        if (startingVelocity > desiredVelocity)
            direction = Direction.DECREASING;
        startTime = UniversalFunctions.getTimeInSeconds();
    }

    //obtains the profile's current velocity
    public double getVelocitySetpoint() {
        double setpoint = 0;
        double intermediateVelocity = (startingVelocity + desiredVelocity) / 2;
        switch (direction) {
            case INCREASING:
                switch (state) {
                    case CONCAVE:
                        setpoint = startingVelocity + maxJerk * Math.pow(time(), 2) / 2;
                        if (setpoint > intermediateVelocity) {
                            state = State.CONVEX;
                            intermediateTime = time();
                            startTime = UniversalFunctions.getTimeInSeconds();
                        }
                        break;
                    case CONVEX:
                        if (intermediateTime < time())
                            setpoint = desiredVelocity;
                        else {
                            double acceleration = maxJerk * intermediateTime;
                            setpoint = startingVelocity + intermediateTime * time() - maxJerk * Math.pow(time(), 2) / 2;
                        }
                        break;
                }
                break;
            case DECREASING:
                switch (state) {
                    case CONCAVE:
                        setpoint = 2 * intermediateVelocity - startingVelocity - maxJerk * Math.pow(time(), 2) / 2;
                        if (setpoint < intermediateVelocity) {
                            state = State.CONVEX;
                            intermediateTime = time();
                            startTime = UniversalFunctions.getTimeInSeconds();
                        }
                        break;
                    case CONVEX:
                        if (intermediateTime < time())
                            setpoint = desiredVelocity;
                        else {
                            double acceleration = maxJerk * intermediateTime;
                            setpoint = 2 * intermediateVelocity - startingVelocity - intermediateTime * time() + maxJerk * Math.pow(time(), 2) / 2;
                        }
                }
                break;
        }
        return setpoint;
    }

    private double time() {
        return UniversalFunctions.getTimeInSeconds() - startTime;
    }

    //returns the distancne required to stop the robot
    public double getStoppingDistance(double speed) {
        double time = 4 * Math.sqrt(speed / maxJerk);
        return 2 * speed * time - maxJerk * speed * Math.pow(time, 2) / 4;
    }
}