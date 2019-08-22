package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.Objects.Robot;

/**
 * All objects that modify the output vector of a VectorField after all VectorFieldComponent vectors are added
 */
public interface Boundry extends ActivatableComponent {
    Vector2 interact(Pose position, Vector2 destination);
}
