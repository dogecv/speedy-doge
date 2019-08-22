package org.firstinspires.ftc.teamcode.Universal.Motion;

import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;

import java.util.ArrayList;

public abstract class Kinematics {
    public abstract Pose wheelToRobotVelocities(ArrayList<Double> wheels);

    public abstract ArrayList<Double> robotToWheelVelocities(Pose v);

    long prev_time = System.currentTimeMillis();

    Pose prevOffset = new Pose();

    public Pose globalOdometryUpdate(Pose robotOffset, Pose fieldPose){
        long dt = System.currentTimeMillis() - prev_time;
        prev_time = System.currentTimeMillis();

        double mx = 2 * (robotOffset.x - prevOffset.x) / Math.pow(dt, 2);
        double my = 2 * (robotOffset.y - prevOffset.y) / Math.pow(dt, 2);
        double mTheta = 2 * (robotOffset.angle - prevOffset.angle) / Math.pow(dt, 2);
        prevOffset = robotOffset;
        double timestep = dt / 10;
        Pose finalOffset = fieldPose;

        for (int i = 1; i < 11; i++){
            finalOffset = constantVeloUpdate(new Pose(mx * timestep * i, my * timestep * i, mTheta * timestep * i), finalOffset);
        }
        return finalOffset;
    }

    public Pose constantVeloUpdate(Pose robotOffset, Pose fieldPose){
        double dTheta = robotOffset.angle;
        double sinTerm = 0.0;
        double cosTerm = 0.0;

        if (Math.abs(dTheta) < 0.001){
            sinTerm = 1.0 - dTheta * dTheta / 6.0;
            cosTerm = dTheta / 2.0;
        }
        else{
            sinTerm = Math.sin(dTheta) / dTheta;
            cosTerm = (1 - Math.cos(dTheta)) / dTheta;
        }
        Vector2 fieldDelta = new Vector2(sinTerm * robotOffset.x - cosTerm * robotOffset.y, cosTerm * robotOffset.x + sinTerm * robotOffset.y);
        fieldDelta.rotate(fieldPose.angle);
        return new Pose(fieldDelta.x, fieldDelta.y, fieldPose.angle + dTheta);
    }
}
