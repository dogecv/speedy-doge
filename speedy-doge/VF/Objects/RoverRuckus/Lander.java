package VF.Objects.RoverRuckus;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalFunctions;
import VF.Objects.Robot;
import VF.VectorFieldComponent;
import VF.VectorShapes.VectorRectangle;
/**
 * Generates the vector field eminating from the Lander
 * Field: Rover Ruckus
 **/
public class Lander extends VectorFieldComponent {
    public final VectorRectangle landerBody, landerLeg1, landerLeg2;
    public Lander(Pose location, double strength, double falloff){
        super(location, strength, falloff);
        //landerBody represents the Lander's cargo holds
        landerBody = new VectorRectangle(location, 23.3, 23.3, strength, falloff);
        //landerLeg1 represents the top right and bottom left lander legs
        landerLeg1 = new VectorRectangle(new Pose(location.x, location.y, location.angle + Math.PI / 4), 63, 1, strength, falloff);
        //landerLeg2 represents the top left and bottom right lander legs
        landerLeg2 = new VectorRectangle(new Pose(location.x, location.y, location.angle - Math.PI / 4), 63, 1, strength, falloff);
    }

    public Lander(Pose location){
        this(location, 24, 0.0);
    }

    public Vector2 interact(Pose pose){
        Vector2 bodyVect = landerBody.interact(pose);
        Vector2 leg1Vect = landerLeg1.interact(pose);
        Vector2 leg2Vect = landerLeg2.interact(pose);

        if(bodyVect.magnitude() > Math.max(leg1Vect.magnitude(), leg2Vect.magnitude())){
            return bodyVect;
        }
        else if(leg1Vect.magnitude() > Math.max(leg2Vect.magnitude(), bodyVect.magnitude())){
            return leg1Vect;
        }
        return leg2Vect;
    }


    @Override
    public void setTarget(Pose pose){
        landerBody.setTarget(pose);
        landerLeg1.setTarget(pose);
        landerLeg2.setTarget(pose);
    }
}
