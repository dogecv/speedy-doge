package VF.VectorShapes;

import Universal.Math.Geometry.Rectangle;
import Universal.Math.Geometry.Shape;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalConstants;
import Universal.UniversalFunctions;
import VF.Objects.Robot;

/**
 * VectorShape in the form of a rectangle
 */
public class VectorRectangle extends VectorShape {

    double width=1, height = 1;
    //TODO: better names
    boolean firstThing = true;
    boolean clockwise = true;
    int targetSide = 0;
    int currentSide = 0;
    Pose pose;
    public VectorRectangle(){
        super(new Pose(), 1, 1);
        shape = new Rectangle(1,1);
    }
    public VectorRectangle(Pose location, double strength, double falloff, Shape shape){
        super(location, strength, falloff);
        this.shape = shape;
        pose = location;

    }
    public VectorRectangle(Pose location, double width, double height, double strength, double falloff){
        super(location, strength, falloff);
        shape = new Rectangle(location, width, height);
        this.width = width;
        this.height = height;
        pose = location;
    }

    @Override
    public void setTarget(Pose loc) {
        super.setTarget(loc);
        Rectangle rect = new Rectangle(location, width, height);
        targetSide = rect.getClosestSide(loc.toVector());
        firstThing = true;
    }
    @Override
    public Vector2 interact(Pose pose){
        Vector2 vectorLocation = shape.getClosestPoint(pose.toVector());
        Rectangle rect = new Rectangle(this.pose, width, height);
        currentSide = rect.getClosestSide(pose.toVector());
        setLocation(new Pose(vectorLocation.x, vectorLocation.y));
        return interact2(pose);
    }
    private Vector2 interact2(Pose position) {
        Robot robot = UniversalConstants.getRobot(position);

        //zeroes the field at 0, 0, 0 and translates the location and destination to match
        Vector2 dest = getTarget().toVector().clone();
        dest.subtract(location.toVector());
        Vector2 point = new Vector2();
        point.x = position.x - location.x;
        point.y = position.y - location.y;
        point.rotate(-dest.angle());

        //creates output vector and sets its magnitude
        Vector2 output = new Vector2(point.x, point.y);
        Vector2 closestRobotPoint = robot.getClosestPoint(position.toVector());
        closestRobotPoint.subtract(robot.location.toVector());
        double strength = getStrength(output.magnitude() - closestRobotPoint.magnitude());


        //if the obstacle is in the way...
        if(Math.abs(UniversalFunctions.normalizeAngle180Radians(output.angle())) > Math.abs(Math.acos(output.magnitude() / dest.magnitude()))&& point.magnitude() < dest.magnitude()){
            //refedines the vector as perpendicular to its original direction
            output.setFromPolar(strength, output.angle() + Math.PI / 2);

            if(point.y > 0)
                output.setFromPolar(strength, -output.angle());

            output.x = Math.abs(output.x);
            output.rotate(dest.angle());

            if(firstThing)
                clockwise = point.y > 0;
            else if (targetSide == 3 && currentSide == 1) {
                if (clockwise)
                    output.setFromPolar(output.magnitude(), position.angle - Math.PI / 2);
                else
                    output.setFromPolar(output.magnitude(), position.angle + Math.PI / 2);
            }
            else if (targetSide == 4 && currentSide == 2) {
                if (clockwise)
                    output.setFromPolar(output.magnitude(), position.angle);
                else
                    output.setFromPolar(output.magnitude(), position.angle + Math.PI);
            }
            else if (targetSide == 1 && currentSide == 3){
                if (clockwise)
                    output.setFromPolar(output.magnitude(), position.angle + Math.PI/2);
                else
                    output.setFromPolar(output.magnitude(), position.angle - Math.PI/2);
            }
            else if (targetSide == 2 && currentSide == 4){
                if (clockwise)
                    output.setFromPolar(output.magnitude(), position.angle + Math.PI);
                else
                    output.setFromPolar(output.magnitude(), position.angle);
            }
        }

        //if the obstacle is out of the way...
        else {

            //shoot straight for the destination
            output = new Vector2(dest.magnitude() - output.x, -output.y);
            output.setFromPolar(strength, output.angle());
            output.rotate(dest.angle());
        }
        firstThing = false;
        return output;
    }
}
