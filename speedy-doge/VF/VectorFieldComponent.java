package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;

public abstract class VectorFieldComponent implements ActivatableComponent {
    public double strength, falloff;
    public Pose location;
    private boolean isActive = true;
    private Pose target;
    public VectorFieldComponent (Pose location, double strength, double falloff) {
        this.strength = strength;
        this.falloff = falloff;
        this.location = location;
    }

    //returns the magnitude of a vector whose origin is a distance d away from the field's origin
    public double getStrength (double d) {
        return strength / d * Math.pow(Math.E, falloff * (strength - d));
    }

    public abstract Vector2 interact(Pose position);

    public void setTarget(Pose point) {
        target = point.clone();
        location.angle = Math.atan2(point.y - location.y, point.x - location.x);
    }
    public Pose getTarget(){
        return target;
    }
    public void activate(){
        isActive = true;
    }
    public void deactivate(){
        isActive = false;
    }
    public boolean isActive(){
        return isActive;
    }
}
