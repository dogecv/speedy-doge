package Universal.Math;

/**
 * 2d vector with an additional angle
 */
public class Pose extends Vector2{
    public double angle;

    public Pose(){
        x = 0;
        y = 0;
        angle = 0;
    }
    public Pose(double x, double y){
        this(x, y, 0);
    }
    public Pose(Pose pose){
        this(pose.x, pose.y, pose.angle);
    }
    public Pose(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void add(Pose p){
        x += p.x;
        y += p.y;
        angle += p.angle;
    }

    /*
    returns a Vector2 containing x and y only
     */
    public Vector2 toVector(){
        return new Vector2(x, y);
    }

    /*
    returns a copy of this Pose
     */
    public Pose clone(){
        return new Pose(x, y, angle);
    }
    @Override
    public String toString(){
        return x + ", " + y + ", " + angle;
    }
}
