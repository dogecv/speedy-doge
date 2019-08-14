package VF;

/**
 * Allows all Obsticals and VectorFieldComponents to change their behavior depending on whether they are "activated"
 */
public interface ActivatableComponent {

    void activate();
    void deactivate();
    boolean isActive();
}
