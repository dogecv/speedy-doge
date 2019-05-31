package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;

public class PointField implements Obstical {
    public double strength, falloff;
    public Pose location;
    public PointField (Pose location, double strength, double falloff) {
        this.strength = strength;
        this.falloff = falloff;
        this.location = location;
    }

    //given a destination, creates the vector induced by the field at position
    public Vector2 interact(Pose position, Vector2 destination) {
        //zeroes the field at 0, 0, 0 and translates the position and destination to match
        Vector2 dest = destination.clone();
        dest.subtract(location.toVector());
        Vector2 point = new Vector2();
        point.x = position.x - location.x;
        point.y = position.y - location.y;
        point.rotate(-dest.angle());

        //creates output vector and sets its magnitude
        Vector2 output = new Vector2(point.x, point.y);
        double strength = getStrength(output.magnitude());

        //if the obstical is in the way...
        if(Math.abs(output.angle()) > Math.acos(output.magnitude() / dest.magnitude())){

            //refedines the vector as perpendicular to its original direction
            output.setFromPolar(strength, output.angle() + Math.PI / 2);
            if(point.y > 0)
                output.setFromPolar(strength, -output.angle());
        }

        //if the obstical is out of the way...
        else {

            //shoot straight for the destination
            output = new Vector2(dest.x - output.x, dest.y - output.y);
            output.setFromPolar(strength, output.angle());
        }

        //rotates the vector to its original angle
        output.rotate(dest.angle());
        return output;
    }

    //returns the magnitude of a vector whose origin is a distance d away from the field's origin
    public double getStrength (double d) {
        return strength / d * Math.pow(Math.E, falloff * (strength - d));
    }
}