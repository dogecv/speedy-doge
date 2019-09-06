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
        landerBody = new VectorRectangle(new Pose(location.x, location.y, Math.PI/4), 23.3, 23.3, strength, falloff);
        //landerLeg1 represents the top right and bottom left lander legs
        landerLeg1 = new VectorRectangle(location, 63, 1, strength, falloff);
        //landerLeg2 represents the top left and bottom right lander legs
        landerLeg2 = new VectorRectangle(location, 1, 63, strength, falloff);
    }

    public Lander(Pose location){
        this(location, 12, 0.1);
    }

    public Vector2 interact(Pose pose){
        Vector2 bodyVect = landerBody.interact(pose);
        bodyVect.add(landerLeg1.interact(pose));
        bodyVect.add(landerLeg2.interact(pose));
        return bodyVect;
    }


    @Override
    public void setTarget(Pose pose){
        landerBody.setTarget(pose);
        landerLeg1.setTarget(pose);
        landerLeg2.setTarget(pose);
    }
}
