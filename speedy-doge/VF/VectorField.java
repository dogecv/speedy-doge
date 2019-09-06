package VF;

import FRC_Pure_Pursuit.Tank.Path;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import VF.VectorShapes.VectorRectangle;

import java.util.ArrayList;

public class VectorField {
    public ArrayList<VectorFieldComponent> obstacles = new ArrayList<>();
    public ArrayList<Boundary> boundaries = new ArrayList<>();
    public ArrayList<VectorRectangle> barriers = new ArrayList<>();
    public Waypoint destination;

    public VectorField (ArrayList<VectorFieldComponent> obstacles, ArrayList<Boundary> boundaries) {
        this.obstacles = obstacles;
        this.boundaries = boundaries;
        calculateBarriers();
        destination = new Waypoint(new Pose());
    }

    /*
    returns the field's vector at the given point
     */
    public Vector2 getVector (Pose point) {

        Vector2 output = destination.interact(point);

        //adds all barrier vectors to output
        for(VectorRectangle barrier : barriers){
            output.add(barrier.interact(point));
        }

        //adds all obstacle vectors to output
        for (VectorFieldComponent obstacle : obstacles) {
            output.add(obstacle.interact(point));
        }

        for(Boundary boundry : boundaries) {
            output = boundry.interact(point, output);
        }
        return output;

    }

    /*
    updates the position of the destination
     */
    public void setWaypoint(Pose location) {
        for(int i = 0; i < obstacles.size(); i++){
            obstacles.get(i).setTarget(location);
        }
        destination.updateLocation(location);
    }

    /*
    Generates a pure pursuit path using the VectorField

    stepSize < thresholdForDestination
     */
public Path generatePath(Pose pose, double stepSize, double thresholdForDestination) {
    Vector2 temp = new Vector2(destination.location.x, destination.location.y);
    temp.subtract(pose.toVector());
    Path output = new Path();
    Vector2 temp2 = new Vector2();
    int counter = 0;
    while(temp.magnitude() > thresholdForDestination && counter < 500) {
        counter ++;
        temp2 = getVector(pose);
        temp2.setFromPolar(stepSize, temp2.angle());
        pose.x += temp2.x;
        pose.y += temp2.y;
        output.wayPoint(pose.x, pose.y);
        pose.angle = temp2.angle();

        temp = new Vector2(destination.location.x, destination.location.y);
    }
    return output;

}
    public Path printPath(Pose pose, double stepSize, double thresholdForDestination) {
        Vector2 temp = new Vector2(destination.location.x, destination.location.y);
        temp.subtract(pose.toVector());
        Path output = new Path();
        Vector2 temp2 = new Vector2();
        String st = "";
        int counter = 0;
        while(temp.magnitude() > thresholdForDestination && counter < 500) {
            counter ++;
            temp2 = getVector(pose);
            temp2.setFromPolar(stepSize, temp2.angle());
            pose.x += temp2.x;
            pose.y += temp2.y;
            output.wayPoint(pose.x, pose.y);
            st+= pose.toVector().toString() + ", ";
            pose.angle = temp2.angle();

            temp = new Vector2(destination.location.x, destination.location.y);
        }

        System.out.println(st);
        return output;

    }

    /*
    creates obstacles between all objects that the robot cannot pass through
     */
    public void calculateBarriers(VectorFieldComponent field1, VectorFieldComponent field2){
        double distance = Math.hypot(field1.location.y - field2.location.y, field1.location.x - field2.location.x);
        if(distance < UniversalConstants.MINIMUM_APPROACH_DISTANCE && field1.isActive() && field2.isActive()) {
            Vector2 intermediatePoint = new Vector2();
            double lineAngle = Math.atan2(field1.location.y - field2.location.y, field1.location.x - field2.location.x);
            intermediatePoint.setFromPolar(distance / 2, lineAngle);
            //TODO: make absolutely sure I should be adding interMediatePoint.x to field2 and not subtracting it from field1
            Pose intermediatePose = new Pose(intermediatePoint.x + field2.location.x, intermediatePoint.y + field2.location.y, lineAngle);
            VectorRectangle vrect = new VectorRectangle(intermediatePose, distance, 0, field1.strength + field2.strength, (field1.falloff + field2.falloff) / 4);
            barriers.add(vrect);
        }
    }

    public void calculateBarriers(){
        barriers = new ArrayList<>();
        for(int i = 0; i < obstacles.size(); i++){
            for(int j = i; j < obstacles.size(); j++){
                calculateBarriers(obstacles.get(i), obstacles.get(j));
            }
        }
    }
}