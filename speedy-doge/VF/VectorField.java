package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;

import java.util.ArrayList;

//TODO: finish
public class VectorField {
        ArrayList<? extends Obstical> obsticals = new ArrayList<>();
        ArrayList<? extends Obstical> boundries = new ArrayList<>();


    public VectorField (ArrayList<? extends Obstical> obsticals, ArrayList<? extends Obstical> boundries) {
        this.obsticals = obsticals;
        this.boundries = boundries;
        }
    public Vector2 getVector (Pose point, Vector2 destination) {
        Vector2 output = new Vector2();
        for (Obstical obstical : obsticals) {
        output.add(obstical.interact(point, destination));
        }

        for(Obstical boundry : boundries) {
        output = boundry.interact(point, output);
        }
        return output;
    }
}