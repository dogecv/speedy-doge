package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalFunctions;

public class Waypoint {
    public Pose location;
    public Solanoid directionalField;
    public AttractionField nonDirectionalField;
    private boolean specifiedDirection = false;

    public Waypoint(Pose location){
        this.location = location;
        directionalField = new Solanoid(location, 1, 1);
        nonDirectionalField = new AttractionField(location, 1, 1);
    }

    public boolean isDirectionSpecified(){
        return specifiedDirection;
    }

    public void specifyDirection(double angle){
        specifyDirection(angle, directionalField.strength, directionalField.falloff);
    }

    public void specifyDirection(double angle, double strength, double falloff){
        nonDirectionalField.deactivate();
        directionalField.activate();
        directionalField.location.angle = angle;

        directionalField.strength = strength;
        directionalField.falloff = falloff;
        nonDirectionalField.strength = strength;
        nonDirectionalField.falloff = falloff;
    }

    public void assignArbitraryDirection(){
        nonDirectionalField.activate();
        directionalField.deactivate();
    }

    public Vector2 interact(Pose position) {
        Vector2 output = directionalField.interact(position);
        output.add(nonDirectionalField.interact(position));
        return output;
    }


    public class AttractionField extends VectorFieldComponent{


        public AttractionField(Pose location, double strength, double falloff) {
            super(location, strength, falloff);
        }


        public Vector2 interact (Pose position) {
            Vector2 output = position.toVector();
            output.subtract(location.toVector());
            output.setFromPolar(getStrength(output.magnitude()), output.angle());
            return output;
        }

        //TODO: fix this
        @Override
        public double getStrength(double d){
            if(!isActive())
                return UniversalFunctions.clamp(1, 1 - super.getStrength(d), 0);
            else
                return 1;
        }
    }

    public class Solanoid extends VectorFieldComponent{

        public Solanoid(Pose location, double strength, double falloff) {
            super(location, strength, falloff);
        }


        public Vector2 interact (Pose position) {
            if(isActive()) {
                //zeroes the field at 0, 0, 0 and translates the position to match
                Vector2 output = new Vector2();
                output.x = position.x - location.x;
                output.y = position.y - location.y;
                output.rotate(-location.angle);

                double magnitude = getStrength(output.magnitude());
                double radius = (Math.pow(output.x, 2) + Math.pow(output.y, 2)) / (2 * output.y);
                output.y -= radius;

                output.rotate(Math.PI / 2 * Math.signum(output.y));
                output.setFromPolar(magnitude, output.angle() + location.angle);
                return output;
            }
            return new Vector2();
        }

        @Override
        public Pose getTarget(){
            return location;
        }
    }
}
