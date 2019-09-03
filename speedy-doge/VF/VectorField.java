package VF;

import FRC_Pure_Pursuit.Tank.Path;
import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.Math.Geometry.Rectangle;
import Universal.Math.Geometry.Shape;
import Universal.UniversalConstants;
import VF.Objects.Robot;
import VF.VectorShapes.VectorRectangle;

import java.util.ArrayList;

public class VectorField {
    public ArrayList<VectorFieldComponent> obsticals = new ArrayList<>();
    public ArrayList<Boundry> boundries = new ArrayList<>();
    public ArrayList<VectorRectangle> barriers = new ArrayList<>();
    public Waypoint destination;

    public VectorField (ArrayList<VectorFieldComponent> obsticals, ArrayList<Boundry> boundries) {
        this.obsticals = obsticals;
        this.boundries = boundries;
        calculateBarriers();
        destination = new Waypoint(new Pose());
    }
    public Vector2 getVector (Pose point) {

        Vector2 output = destination.interact(point);

        for(VectorRectangle barrier : barriers){
            output.add(barrier.interact(point));
        }

        for (VectorFieldComponent obstical : obsticals) {
            output.add(obstical.interact(point));
        }

        for(Boundry boundry : boundries) {
            output = boundry.interact(point, output);
        }
        return output;

    }

    public void setWaypoint(Pose location) {
        for(int i = 0; i < obsticals.size(); i++){
            obsticals.get(i).setTarget(location);
        }
        destination.updateLocation(location);
    }

    //stepSize < thresholdForDestination
    public Path generatePath(Pose pose, double stepSize, double thresholdForDestination) {
        Vector2 temp = new Vector2(destination.location.x, destination.location.y);
        temp.subtract(pose.toVector());
        Path output = new Path();
        Vector2 temp2 = new Vector2();
        String st = "";
        double min = temp.magnitude();
        int counter = 0;
        while(temp.magnitude() > thresholdForDestination && counter < 500) {
            counter ++;
            temp2 = getVector(pose);
            temp2.setFromPolar(stepSize, temp2.angle());
            pose.x += temp2.x;
            pose.y += temp2.y;
            output.wayPoint(pose.x, pose.y);
            st+= pose.toVector().toString() + ", ";

            temp = new Vector2(destination.location.x, destination.location.y);
        }

        System.out.println(st);
        return output;

    }
//TODO: calculate boundry conditions after the center path is generated
    public Path generateCenterPath(Pose pose, double stepSize, double thresholdForDestination){
        Pose leftSide = new Pose(0, 9, 0), rightSide = new Pose(0, -9, 0);
        leftSide.rotate(pose.angle);
        rightSide.rotate(pose.angle);
        leftSide.add(pose);
        rightSide.add(pose);
        Path path1 = generatePath(leftSide, stepSize, thresholdForDestination);
        Path path2 = generatePath(rightSide, stepSize, thresholdForDestination);
        Path shortPath = path1, longPath = path2;
        if(path1.getTotalPathLength() > path2.getTotalPathLength()){
            shortPath = path2;
            longPath = path1;
        }
        double length = shortPath.getTotalPathLength();
        Path outputPath = new Path();
        for(int i = 1; i < shortPath.getSize(); i++){
            Vector2 v1 = longPath.pointOnPathByPercentage(i/length);
            Vector2 v2 = new Vector2(shortPath.getCoords().get(i)[0], shortPath.getCoords().get(i)[1]);
            v1.add(v2);
            v1.scalarMultiply(0.5);
            outputPath.wayPoint(v1.x, v1.y);
        }
        return outputPath;
    }

    public void calculateBarriers(VectorFieldComponent field1, VectorFieldComponent field2){
        double distance = Math.hypot(field1.location.y - field2.location.y, field1.location.x - field2.location.x);
        //TODO: add MINIMUM_APPROACH_DISTANCE to UniversalConstants or Robot class
        if(distance < UniversalConstants.MINIMUM_APPROACH_DISTANCE && field1.isActive() && field2.isActive()) {
            Vector2 intermediatePoint = new Vector2();
            double lineAngle = Math.atan2(field1.location.y - field2.location.y, field1.location.x - field2.location.x);
            intermediatePoint.setFromPolar(distance / 2, lineAngle);
            //TODO: make absolutely sure I should be adding interMediatePoint.x to field2 and not subtracting it from field1
            Pose intermediatePose = new Pose(intermediatePoint.x + field2.location.x, intermediatePoint.y + field2.location.y, lineAngle);
            VectorRectangle vrect = new VectorRectangle(intermediatePose, distance, 0, field1.strength + field2.strength, (field1.falloff + field2.falloff) / 4);
            barriers.add(vrect);
        }
    }

    public void calculateBarriers(){
        barriers = new ArrayList<>();
        for(int i = 0; i < obsticals.size(); i++){
            for(int j = i; j < obsticals.size(); j++){
                calculateBarriers(obsticals.get(i), obsticals.get(j));
            }
        }
    }
}