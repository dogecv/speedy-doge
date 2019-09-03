package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.Objects.Robot;

/**
 * Defines points in a VectorField with an undefined output
 */
public interface Boundary extends ActivatableComponent {
    /*
    if position is inside the boundary region, returns a vector that points parallel to or away from the boundary, otherwise return destination
     */
    Vector2 interact(Pose position, Vector2 destination);
}
