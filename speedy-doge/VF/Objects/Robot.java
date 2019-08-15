package VF.Objects;

import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Geometry.Rectangle;
import Universal.Math.Geometry.Shape;
import Universal.Math.Pose;
import Universal.Math.Vector2;

import java.util.ArrayList;
/**
* This class simplifies the robot's shape as an 18" square.
* Location is updated in odometry classes.
* A Robot object is defined in UniversalConstants which is used to estimate the minimum distane between the robot and a given point.
* Various shapes can be added to the components ArrayList to create a more accurate, customized robot shape.
* By default, the robot faces the positive Y axis
*/
//TODO: adjust component locations for backwards PP tank driving
public class Robot extends Rectangle {
    public ArrayList<Shape> components = new ArrayList<Shape>();
    public Robot(Pose location){
        super(location, 18,18);
    }

    public void addComponent(Shape shape) {
        components.add(shape);
    }
    @Override
    public Vector2 getClosestPoint(Vector2 point){
        refreshLocation(position);
        double workingDistance = getClosestVector(point).magnitude();
        Vector2 output = super.getClosestPoint(point);
        for(Shape shape : components){
            double potentialDistance = shape.getClosestVector(point).magnitude();
            if(workingDistance > potentialDistance){
                workingDistance = potentialDistance;
                output = shape.getClosestPoint(point);
            }
        }
        return output;
    }

    public Vector2 getClosestPoint(Pose currentPose){
        position = currentPose;
        return getClosestPoint(currentPose.toVector());
    }

    public void refreshLocation(Pose currentLocation){
        for (int i = 0; i < components.size(); i++){
            components.get(i).position.x -= position.x;
            components.get(i).position.y -= position.y;
            components.get(i).position.angle -= position.angle;
            components.get(i).position.rotate(-position.angle);
            components.get(i).position.rotate(currentLocation.angle);
            components.get(i).position.add(currentLocation.toVector());
        }
        position = currentLocation;
    }
}
