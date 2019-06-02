import java.sql.Array;
import java.util.ArrayList;

public class Lookahead {
    private static RateLimiter rateLimiter = new RateLimiter();
    static double[] distances = new double[] {};
    static double[] curvatures = new double[] {};

    public static void calculateDistances(){
        distances = new double[Path.coords.size()];
        double sum = 0;
        distances[distances.length-1] = 0;

        for(int i = distances.length-2; i>=0; i--){ //start at 2nd to last point work backwards
            double[] end = Path.coords.get(i+1); //end point
            double[] start = Path.coords.get(i); //start point

            double segmentLength = PathGeneration.distance_formula(start[0], start[1], end[0], end[1]);

            sum+=segmentLength; //add to total

            distances[i] = sum; //store value
        }

    }

    public static void calculateCurve(){
        curvatures = new double[Path.coords.size()];
        for(int i = 1; i<Path.coords.size()-1; i++){
            double[] p1 = Path.coords.get(i-1); //first point
            //System.out.println(Arrays.toString(p1));
            double[] p2 = Path.coords.get(i); //middle point
            //System.out.println(Arrays.toString(p2));
            double[] p3 = Path.coords.get(i+1); //last point
            //System.out.println(Arrays.toString(p3));
            if (p1[0] == p2[0]){
                p1[0]= p1[0] + 0.001;
            }
            double k1 = 0.5*(Math.pow(p1[0], 2)+ Math.pow(p1[1], 2) - Math.pow(p2[0] ,2) - Math.pow(p2[1], 2))/ (p1[0]-p2[0]);
            double k2 = (p1[1]-p2[1])/(p1[0]-p2[0]);
            double b = 0.5*Math.pow(p2[1], 2)-2*p2[0]*k1+Math.pow(p2[1], 2)-Math.pow(p3[0],2)+2*p3[0]*k1-Math.pow(p3[1], 2)/(Math.pow(p3[0], 2) * k2 - p3[1] + p2[1] - p2[0] * k2);
            double a = k1 - k2 * b;
            double r = Math.sqrt((Math.pow((p1[0]-a), 2) + Math.pow((p1[1] - b), 2)));
            double curvature = 1/r;
            //System.out.println(curvature);
            curvatures[i] = curvature;

        }
        //System.out.println(Arrays.toString(curvatures));
        curvatures[0] = 10000;
        curvatures[curvatures.length-1] = 10000;
    }

    public static int findClosestPoint(){
        double robotX = 0; //get from odometry
        double robotY = 0; //get from odometry

        double[] pointDistances = new double[Path.coords.size()]; //to store distances
        for(int i = 0; i<Path.coords.size(); i++){ //loop through points
            //coords of point in path
            double x = Path.coords.get(i)[0];
            double y = Path.coords.get(i)[0];

            pointDistances[i] = PathGeneration.distance_formula(robotX, robotY, x, y); //find distance for that point
        }

        int index = findSmallestIndex(pointDistances);
        return index;
    }

    public static int findSmallestIndex (double [] arr1) {
        int index = 0;
        double min = arr1[index];

        for (int i=1; i<arr1.length; i++) {


            if (arr1[i] < min) {
                min = arr1[i];
                index = i;
            }

        }
        return index;
    }

    static double lastLookaheadPointIndex = 0;
    static double[] lookaheadPoint = {0, 0};
    public static double[] findLookeaheadPoint(){
        for(int i = (int) Math.floor(lastLookaheadPointIndex); i<Path.coords.size()-1; i++){//loop through line segments starting at index of last lookahead point until second to last point
            Vector E = new Vector(Path.coords.get(i)); //start of segment
            Vector L = new Vector(Path.coords.get(i+1)); //end of segment
            Vector C = new Vector(new double[] {0, 0});//insert robot xy coords
            double r = 1; //insert lookahead distance

            Vector d = E.minus(L);
            Vector f = E.minus(C);
            //more math here

            double a = d.dot(d);
            double b = 2*f.dot(d);
            double c = f.dot(f) - Math.pow(r, 2);
            double discriminant = Math.pow(b, 2) - 4 * a * c;
            double t_value = 0;

            if (discriminant < 0) {
                //return lookaheadPoint;
            }else{
                discriminant = Math.sqrt(discriminant);
                double t1 = (-b - discriminant)/(2*a);
                double t2 = (-b + discriminant)/(2*a);

                if (t1 >= 0 && t1 <=1){
                    t_value = t1;
                }else if (t2 >= 0 && t2 <=1){
                    t_value  =t2;
                }
                return lookaheadPoint;
            }


            if(t_value + i > lastLookaheadPointIndex){
                Vector lookaheadPoint = E.plus(d.scale(t_value));
                return lookaheadPoint.data; //get the x y coords from the vector
            }else{
                //return lookaheadPoint;
            }
        }
        return lookaheadPoint; //(so no syntax error) (replace with last lookahead point)

    }

    public static double constrain(double x, double min, double max){
        return Math.min(Math.max(x, min), max);
    }



    public static double calculateCurvatureLookAheadArc(double currPosX, double currPosY, double heading, double[] lookahead, double lookaheadDistance) {
        double a = -Math.tan(heading);
        double b = 1;
        double c = (Math.tan(heading) * currPosX) - currPosY;
        double x = Math.abs(a * lookahead[0] + b * lookahead[1] + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        double cross = (Math.sin(heading) * (lookahead[0] - currPosX)) - (Math.cos(heading) * (lookahead[1] - currPosY));
        double side = cross > 0 ? 1 : -1;
        double curvature = (2 * x) / (Math.pow(lookaheadDistance, 2));
        return curvature * side;
    }

    public static double[] lookahead(double timeRate){
        Odometry.system();
        int closestPointIndex = findClosestPoint();
        double pathCurvature = curvatures[closestPointIndex];
        double lookaheadDistance = constrain(1/pathCurvature, 2, 4);

        double lookaheadCurvature = calculateCurvatureLookAheadArc(Odometry.x, Odometry.y, Odometry.robot_angle, lookaheadPoint, lookaheadDistance);
        double trackWidth = 16; // robot wheel width, inches
        double targetRobotVel = PathGeneration.velocities[closestPointIndex];
        double limitedTargetVel = rateLimiter.rateLimit(targetRobotVel, PathGeneration.max_rate, timeRate);
        double[] velocities = Velocities.calculateWheelVelocity(lookaheadCurvature, trackWidth, limitedTargetVel);
        double[] actualVels = {0, 0}; //get from sensors
        double[] motorPowers = Velocities.motorSpeedClosedLoop(velocities, actualVels, 0.1, 0.05); //make sure to tune kV and kP.
        return motorPowers;
    }



}
