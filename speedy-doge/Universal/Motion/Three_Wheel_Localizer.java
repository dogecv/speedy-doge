package org.firstinspires.ftc.teamcode.Universal.Motion;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Universal.Math.Pose;

import java.util.ArrayList;

public class Three_Wheel_Localizer extends Kinematics{

    double x0 = 4.5;
    double TRACK_WIDTH = 18; //todo: tune these as necessary

    @Override
    public Pose wheelToRobotVelocities(ArrayList<Double> wheels){
        return new Pose(); //placeholder
    }

    @Override
    public ArrayList<Double> robotToWheelVelocities(Pose v) {
        return new ArrayList<Double>(); //placeholder
    }

    public Pose update(ArrayList<Double> offsets, Pose fieldPose){
        double dTheta = (offsets.get(0) - offsets.get(1)) / TRACK_WIDTH;
        double dx = (x0 / TRACK_WIDTH) * (offsets.get(0) - offsets.get(1)) + offsets.get(2);
        double dy = (offsets.get(0) + offsets.get(1)) / 2;

        Pose offset = new Pose(dx, dy, dTheta);

        return globalOdometryUpdate(offset, fieldPose);
    }
}
