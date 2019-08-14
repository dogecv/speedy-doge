package VF.Objects;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalFunctions;
import VF.VectorFieldComponent;
import VF.VectorShapes.VectorRectangle;

public class Lander extends VectorFieldComponent {
    public final VectorRectangle landerBody, landerLeg1, landerLeg2;
    public Lander(double strength, double falloff){
        super(new Pose(0, 0, 0), strength, falloff);
        landerBody = new VectorRectangle(new Pose(0, 0, Math.PI / 4), 23.3, 23.3, strength, falloff);
        landerLeg1 = new VectorRectangle(new Pose(0, 0, Math.PI / 4), 44.8, 0, strength, falloff);
        landerLeg2 = new VectorRectangle(new Pose(0, 0, Math.PI / 4), 44.8, 0, strength, falloff);
    }

    public Vector2 interact(Pose pose){
        double distanceToBody = landerBody.shape.getClosestPoint(pose.toVector()).magnitude();
        double distanceToLeg1 = landerLeg1.shape.getClosestPoint(pose.toVector()).magnitude();
        double distanceToLeg2 = landerLeg2.shape.getClosestPoint(pose.toVector()).magnitude();

        double minimumDistance = UniversalFunctions.min(distanceToBody, distanceToLeg1, distanceToLeg2);
        Vector2 output = new Vector2();
        if(minimumDistance == distanceToBody)
            output = landerBody.interact(pose);
        else if(minimumDistance == distanceToLeg1)
            output = landerLeg1.interact(pose);
        else
            output = landerLeg2.interact(pose);
        return output;
    }
}
