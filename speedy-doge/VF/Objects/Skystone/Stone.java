package VF.Objects.Skystone;

import Universal.Math.Pose;
import VF.VectorShapes.VectorRectangle;

public class Stone extends VectorRectangle {
    public boolean skystone = false;
    public Stone(Pose pose){
        super(pose, 4,8, 24, 0.01);
    }


}
