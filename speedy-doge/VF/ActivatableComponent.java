package VF;

/**
 * Allows all Obstacles and VectorFieldComponents to change their behavior depending on whether they are "activated"
 */
public interface ActivatableComponent {

    /*
    activates the component
     */
    void activate();

    /*
    deactivates the component
     */
    void deactivate();

    /*
    returns true if the component is active, false if it is inactive
     */
    boolean isActive();
}
