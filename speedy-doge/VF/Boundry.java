package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;

/**
 * All objects that modify the output vector of a VectorField after all VectorFieldComponent vectors are added
 */
public interface Boundry extends ActivatableComponent {
    Vector2 interact(Pose position, Vector2 destination);
}
