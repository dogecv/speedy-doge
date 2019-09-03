package VF.Objects;

import Universal.Math.Geometry.Rectangle;
import Universal.Math.Pose;
import Universal.Math.Vector2;

/**
* This class simplifies the robot's shape as an 18" square.
* Location is updated in odometry classes.
* A Robot object is defined in UniversalConstants which is used to estimate the minimum distane between the robot and a given point.
* Various shapes can be added to the components ArrayList to create a more accurate, customized robot shape.
* By default, the robot faces the positive Y axis
*/
//TODO: add ability to change robot shape
public class Robot extends Rectangle {
    //public ArrayList<Shape> components = new ArrayList<Shape>();
    public Robot(Pose location){
        super(location, 18,18);
    }
    public Robot(){
        this(new Pose());
    }
    /*public void addComponent(Shape shape) {
        components.add(shape);
    }*/
    @Override
    public Vector2 getClosestPoint(Vector2 point){
        //refreshLocation(location);
        //double workingDistance = getClosestVector(point).magnitude();
        Vector2 output = super.getClosestPoint(point);
        /*for(Shape shape : components){
            double potentialDistance = shape.getClosestVector(point).magnitude();
            if(workingDistance > potentialDistance){
                workingDistance = potentialDistance;
                output = shape.getClosestPoint(point);
            }
        }*/
        return output;
    }

    public Vector2 getClosestPoint(Pose currentPose){
        location = currentPose;
        return getClosestPoint(currentPose.toVector());
    }
/*
    public void refreshLocation(Pose currentLocation){
        for (int i = 0; i < components.size(); i++){
            components.get(i).location.x -= location.x;
            components.get(i).location.y -= location.y;
            components.get(i).location.angle -= location.angle;
            components.get(i).location.rotate(-location.angle);
            components.get(i).location.rotate(currentLocation.angle);
            components.get(i).location.add(currentLocation.toVector());
        }
        location = currentLocation;
    }*/
}
