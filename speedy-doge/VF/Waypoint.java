package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalFunctions;

/**
 * A Waypoint creates a VectorFieldComponent at the destination: either an AttractionField or a Solanoid
 */

//TODO: remove AttractionField and add comments
public class Waypoint {
    //destination location
    public Pose location;
    //the two VectorFieldComponents (no two can be active at the same time)
    public Solanoid directionalField;
    public AttractionField nonDirectionalField;
    //determines whether directionalField or nonDirectional should be activated
    private boolean specifiedDirection = false;

    public Waypoint(Pose location){
        this.location = location;
        directionalField = new Solanoid(location, 1, 1);
        nonDirectionalField = new AttractionField(location, 1, 1);
    }

    /*
    returns secifiedDirection
     */
    public boolean isDirectionSpecified(){
        return specifiedDirection;
    }

    /*
    specifies a desired angle at the destination (in this case, a solanoidal field will be used)
     */
    public void specifyDirection(double angle){
        specifyDirection(angle, directionalField.strength, directionalField.falloff);
    }

    /*
    specifies a desired angle at the destination and sets the strength strength and falloff of the solanoidal field
     */
    public void specifyDirection(double angle, double strength, double falloff){
        nonDirectionalField.deactivate();
        directionalField.activate();
        directionalField.location.angle = angle;

        directionalField.strength = strength;
        directionalField.falloff = falloff;
        nonDirectionalField.strength = strength;
        nonDirectionalField.falloff = falloff;
    }
    /*
    deactivates the directionalField and activates the nonDirectionalField (when a desired angle does not exist)
     */
    public void assignArbitraryDirection(){
        nonDirectionalField.activate();
        directionalField.deactivate();
    }
    /*
    generates an output vector to draw location towards location (at a specific angle, if specifiedDirection is true)
     */
    public Vector2 interact(Pose position) {
        Vector2 output = directionalField.interact(position);
        output.add(nonDirectionalField.interact(position));
        return output;
    }

    /**
     * VectorFieldComponent with output vectors that all point towards location
     */
    public class AttractionField extends VectorFieldComponent{


        public AttractionField(Pose location, double strength, double falloff) {
            super(location, strength, falloff);
        }

	    public Vector2 interact (Pose position) {
            //change coordinates to be relative to location
            Vector2 output = position.toVector();
            output.subtract(location.toVector());

            //sets the magnitude and angle of the output vector to point towards location
            output.setFromPolar(-1, output.angle());

            //TODO:
            output = new Vector2();
            return output;
        }
      
        public double getStrength(double d){
            //if the field is currently in use...
            if(!isActive())
                //return getStrength and normalize the output to be between 1 and 0
                return UniversalFunctions.clamp(1, 1 - super.getStrength(d), 0);
            //if the field is inactive...
            else
                return 0;
        }
    }
    /**
     * VectorFieldComponent with output vectors that resemble a solanoid
     */
    public class Solanoid extends VectorFieldComponent{

        public Solanoid(Pose location, double strength, double falloff) {
            super(location, strength, falloff);
        }


        public Vector2 interact (Pose position) {
            //if the field is currently active...
            if(super.isActive()) {
                //zeroes the field at 0, 0, 0 and translates the location to match
                Vector2 output = new Vector2();
                output.x = position.x - location.x;
                output.y = position.y - location.y;
                output.rotate(-location.angle);
                //calculates the radius of the ring of the solanoid
                double magnitude = getStrength(output.magnitude());
                double radius = (Math.pow(output.x, 2) + Math.pow(output.y, 2)) / (2 * output.y);
                output.y -= radius;
                //renormalizes the output vector and sets the output's magnitude
                output.setFromPolar(magnitude, output.angle() + location.angle);
                return output;
            }
            return new Vector2();        }

        @Override
        public Pose getTarget(){
            return location;
        }
    }
}
