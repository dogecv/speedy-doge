package VF;

import Universal.Math.Pose;
import Universal.Math.Vector2;

public interface Obstical {
    public Vector2 interact(Pose position, Vector2 destination);
}
