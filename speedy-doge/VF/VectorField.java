package VF;

import FRC_Pure_Pursuit.Tank.Path;
import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Pose;
import Universal.Math.Vector2;

import java.util.ArrayList;

//TODO: finish
public class VectorField {
    public ArrayList<VectorFieldComponent> obsticals = new ArrayList<>();
    public ArrayList<Boundry> boundries = new ArrayList<>();
    public Waypoint destination;

    public VectorField (ArrayList<VectorFieldComponent> obsticals, ArrayList<Boundry> boundries) {
        this.obsticals = obsticals;
        this.boundries = boundries;
    }
    public Vector2 getVector (Pose point) {
        Vector2 output = new Vector2();
        for (VectorFieldComponent obstical : obsticals) {
            output.add(obstical.interact(point));
        }
        output.add(destination.interact(point));
        for(Boundry boundry : boundries) {
            output = boundry.interact(point, output);
        }
        return output;
    }

    public void setWaypoint(Pose location) {
        for(int i = 0; i < obsticals.size(); i++){
            obsticals.get(i).setTarget(location);
        }
        destination.location = location;
    }

    //stepSize < thresholdForDestination
    public Path generatePath(Pose pose, double stepSize, double thresholdForDestination) {
        Vector2 temp = new Vector2(destination.location.x, destination.location.y);
        temp.subtract(pose.toVector());
        Path output = new Path();
        Vector2 temp2 = new Vector2();

        while(temp.magnitude() < thresholdForDestination) {
            temp2 = getVector(pose);
            temp2.setFromPolar(stepSize, temp2.angle());

            pose.x += temp.x;
            pose.y += temp.y;
            output.wayPoint(pose.x, pose.y);
        }
        return output;
    }
}