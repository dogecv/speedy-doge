package VF.Objects;

import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Geometry.Rectangle;
import Universal.Math.Geometry.Shape;
import Universal.Math.Pose;
import Universal.Math.Vector2;

import java.util.ArrayList;

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
