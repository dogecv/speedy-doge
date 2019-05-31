package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;

import java.util.ArrayList;

//TODO: finish
public class VectorField {
    public ArrayList<VectorFieldComponent> obsticals = new ArrayList<>();
    public ArrayList<? extends Obstical> boundries = new ArrayList<>();
    public Waypoint destination;

    public VectorField (ArrayList<VectorFieldComponent> obsticals, ArrayList<? extends Obstical> boundries) {
        this.obsticals = obsticals;
        this.boundries = boundries;
    }
    public Vector2 getVector (Pose point) {
        Vector2 output = new Vector2();
        for (VectorFieldComponent obstical : obsticals) {
            output.add(obstical.interact(point));
        }
        output.add(destination.interact(point));
        for(Obstical boundry : boundries) {
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
}