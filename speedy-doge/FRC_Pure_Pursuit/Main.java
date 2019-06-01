import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;

public class Main {
    static ArrayList<double[]> coords = new ArrayList<double[]>();
    static double[] distances = new double[] {};
    static double[] curvatures = new double[] {};
    static double[] velocities = new double [] {};
    static double max_rate = 5.6;
    static boolean running = true;
    static double x;
    static double y;
    static double robot_angle;
    static long lastTime = System.currentTimeMillis();

    public static void main(String[] args) {
        run("pathLeft");
    }

    private static void  pathLeft(){
        startPath();
        //for the wayPoint function enter in relativity to center of the field. In rover ruckus for example, it would
        //be the center of the lander
        wayPoint(6, 4);
        wayPoint(7, 7);
        wayPoint(8, 9);
        wayPoint(10, 12);
        endPath();
    }



    private static void wayPoint(double x1, double y1){
        coords.add(new double[]{x1*24, y1*24});
        for(int i = 0; i<coords.size(); i++){
            // System.out.println(Arrays.toString(coords.get(i)));
        }
    }

    private static ArrayList<double[]> injectPoints(double x1, double y1, double x2, double y2) {
        double spacing = 0.98;
        double vectorAx = x2-x1;
        double vectorAy = y2-y1;
        double vectorMag = distance_formula(x1, y1 , x2, y2);
        int num_of_point = (int) Math.ceil(vectorMag / spacing);
        double vectNormX = (vectorAx/vectorMag) * spacing;
        double vectNormY = (vectorAy/vectorMag) * spacing;

        ArrayList<double[]> new_points = new ArrayList<double[]>();
        for(int i = 0; i<num_of_point; i++){
            new_points.add(new double[]{x1+vectNormX*i, y1+vectNormY*i});
        }

        return new_points;
    }

    private static void injectPointsIntoPath(){
        ArrayList<double[]> temp = new ArrayList<double[]>();
        for(int i = 0; i<coords.size()-1; i++){
            double[] start = coords.get(i); //start point of segment
            double[] end = coords.get(i+1); //end point of segment
            ArrayList<double[]> pointsForSegment = injectPoints(start[0],start[1],end[0],end[1]);
            temp.addAll(pointsForSegment);
        }
        temp.add(coords.get(coords.size()-1)); //add last point
        coords = temp;
    }


    private static double[][] smoother(double[][] path, double weight_data, double weight_smooth, double tolerance)
    {

        //copy array
        double[][] newPath = path.clone();

        double change = tolerance;
        while(change >= tolerance)
        {
            change = 0.0;
            for(int i=1; i<path.length-1; i++)
                for(int j=0; j<path[i].length; j++)
                {
                    double aux = newPath[i][j];
                    newPath[i][j] += weight_data * (path[i][j] - newPath[i][j]) + weight_smooth * (newPath[i-1][j] + newPath[i+1][j] - (2.0 * newPath[i][j]));
                    change += Math.abs(aux - newPath[i][j]);
                }
        }
        //System.out.println(Arrays.deepToString(newPath));
        return newPath;
    }

    private static void calculateDistances(){
        distances = new double[coords.size()];
        double sum = 0;
        distances[distances.length-1] = 0;

        for(int i = distances.length-2; i>=0; i--){ //start at 2nd to last point work backwards
            double[] end = coords.get(i+1); //end point
            double[] start = coords.get(i); //start point

            double segmentLength = distance_formula(start[0], start[1], end[0], end[1]);

            sum+=segmentLength; //add to total

            distances[i] = sum; //store value
        }

    }

    private static void calculateCurve(){
        curvatures = new double[coords.size()];
        for(int i = 1; i<coords.size()-1; i++){
            double[] p1 = coords.get(i-1); //first point
            //System.out.println(Arrays.toString(p1));
            double[] p2 = coords.get(i); //middle point
            //System.out.println(Arrays.toString(p2));
            double[] p3 = coords.get(i+1); //last point
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

    private static void calculateVelocity(){

        velocities = new double[coords.size()];
        double max_velocity_path = 6;
        double k = 5;
        int lastPointVelocity = 0;
        velocities[coords.size()-1] = lastPointVelocity;
        for (int x = coords.size()-2; x >= 0; x--){

            double oldVelo = Math.min(max_velocity_path, (k/curvatures[x]));
            double[] firstPoint = coords.get(x+1);
            double[] secondPoint = coords.get(x);
            double distance = distance_formula(firstPoint[0], firstPoint[1], secondPoint[0], secondPoint[1]);
            double newVelo = Math.min(oldVelo, Math.sqrt(Math.pow(velocities[x+1], 2) + 2 * max_rate * distance));
            velocities[x] = newVelo;

        }


    }

    private static void startPath(){
        //other init stuff
    }

    private static void endPath(){
        injectPointsIntoPath();
        double[][] multiPointPath = new double[coords.size()][2];
        for(int x = 0; x<coords.size(); x++){
            multiPointPath[x][0] = coords.get(x)[0]; //x coord
            multiPointPath[x][1] = coords.get(x)[1]; //y coord
        }
        double[][] smoothPath = smoother(multiPointPath, 0.001, 0.999, 0.001); //a is 1-b
        coords.clear(); //empty current coords array
        //loop through smoothPath and copy the data to coords
        for(int i =0; i<smoothPath.length; i++){
            coords.add(smoothPath[i]);
        }
        calculateDistances();
        calculateCurve();
        calculateVelocity();
    }

    private static double constrain(double x, double min, double max){
        return Math.min(Math.max(x, min), max);
    }

    static double lastLimiterOutput = 0;
    private static double rateLimit(double x, double maxRate, double timeBetweencalls){
        double maxChange = timeBetweencalls * maxRate;
        lastLimiterOutput += constrain(x - lastLimiterOutput, -maxChange, maxChange);
        return lastLimiterOutput;
    }

    private static int findClosestPoint(){
        double robotX = 0; //get from odometry
        double robotY = 0; //get from odometry

        double[] pointDistances = new double[coords.size()]; //to store distances
        for(int i = 0; i<coords.size(); i++){ //loop through points
            //coords of point in path
            double x = coords.get(i)[0];
            double y = coords.get(i)[0];

            pointDistances[i] = distance_formula(robotX, robotY, x, y); //find distance for that point
        }

        int index = findSmallestIndex(pointDistances);
        return index;
    }

    static double lastLookaheadPointIndex = 0;
    static double[] lookaheadPoint = {0, 0};
    public static double[] findLookeaheadPoint(){
        for(int i = (int) Math.floor(lastLookaheadPointIndex); i<coords.size()-1; i++){//loop through line segments starting at index of last lookahead point until second to last point
            Vector E = new Vector(coords.get(i)); //start of segment
            Vector L = new Vector(coords.get(i+1)); //end of segment
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


    private static double[] lookahead(){
        odometry();
        int closestPointIndex = findClosestPoint();
        double pathCurvature = curvatures[closestPointIndex];
        double lookaheadDistance = 2; //or something like constrain(1/curvature, 2, 4) but start with the constant it'll be easier I think

        double lookaheadCurvature = calculateCurvatureLookAheadArc(x, y, robot_angle, lookaheadPoint, lookaheadDistance);
        double trackWidth = 16; // robot wheel width, inches
        double targetRobotVel = velocities[closestPointIndex];
        double[] velocities = calculateWheelVelocity(lookaheadCurvature, trackWidth, targetRobotVel);
        double[] actualVels = {0, 0}; //get from sensors
        double[] motorPowers = motorSpeedClosedLoop(velocities, actualVels, 0.1, 0.05); //make sure to tune kV and kP.
        return motorPowers;
    }

    private static double[] calculateWheelVelocity(double curvature, double trackWidth, double targetVel){
        //L = V * (2 + CT)/2
        //R = V * (2 − CT)/2
        double left = targetVel * (2 + curvature*trackWidth)/2.0;
        double right = targetVel * (2 - curvature*trackWidth)/2.0;

        return new double[] {left, right};
    }

    public static double[] motorSpeedClosedLoop(double[] targetVels, double[] actualVels, double kV, double kP){
        double[] motorPowers = new double[2];
        for(int i = 0; i<2; i++){ //do left/right side
            motorPowers[i] += targetVels[i] * kV; //Feedforward
            motorPowers[i] += (targetVels[i]- actualVels[i]) * kP; //Feedback
        }
        return motorPowers;
    }

    private static boolean donePath(){
        double wheelDiameter = 4;
        double ticks_per_revolution = 1120;
        double mass = 23;
        double leftFrontVelocity = 5; //get velocity from motor motor.getVelocity()
        double leftBackVelocity = 5; //get velocity from motor motor.getVelocity()
        double leftVelocity = (((leftFrontVelocity+leftBackVelocity)/2)/ticks_per_revolution)*(wheelDiameter*Math.PI); //get left sides velocity
        double rightFrontVelocity = 5; //get velocity from motor motor.getVelocity()
        double rightBackVelocity = 5; //get velocity from motor motor.getVelocity()
        double rightVelocity = (((rightFrontVelocity+rightBackVelocity)/2)/ticks_per_revolution)*(wheelDiameter*Math.PI); //get right side velocity
        double velocity = (leftVelocity+rightVelocity)/2;
        double momentum = mass * velocity;
        double[] lastPoint = coords.get(coords.size()-1);
        boolean done = distance_formula(x, y, lastPoint[0], lastPoint[1]) < momentum; //stop when within two inches of endpoint
        return done;
    }


    private static void run(String pathToRun){
        if (pathToRun== "pathLeft"){
            pathLeft();
        }
        running = true; //say you are starting a path
        while(running){ //while opmode is active
            long currentTime = System.currentTimeMillis();
            double timeBetweencalls = (currentTime - lastTime) / 1000.0;
            double[] motorPowers = lookahead();
            //LeftMotor.setPower(motorPowers[0]);
            //RightMotor.setPower(motorPowers[1]);
            boolean done = donePath();
            running = !done;

            lastTime = currentTime;
            //make a 20 ms delay here, you would want to do this however you are supposed to for FTC robots.
        }
    }

    private static int findSmallestIndex (double [] arr1) {
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

    private static void odometry(){ //better odometry system goes here
        x = 0;
        y = 0;
        robot_angle = -180;
        int oldTickR = 0; //getCurrentPos
        int newTickR = 1120; //getCurrentPos
        int oldTickL = 0; //getCurrentPos
        int newTickL = 1120; //getCurrentPos
        double robot_radius = 9;
        robot_angle += (newTickL - newTickR) / (robot_radius * Math.PI);
        System.out.println("The new heading is " + robot_angle);
        double distance = ((newTickL - oldTickL) + (newTickR - oldTickR)) / 2;
        System.out.println("The new distance is " + distance);
        if ((robot_angle == 90) || (robot_angle == -90)){
            x = 0;
        }

        else{
            x += distance * Math.cos(Math.toRadians(robot_angle));
        }

        if ((robot_angle == 180) || (robot_angle == -180)){
            y = 0;
        }

        else{
            y += distance * Math.sin(Math.toRadians(robot_angle));
        }

        System.out.println("The new x coordinate is " + x);
        System.out.println("The new y coordinate is " + y);

    }

    private static double distance_formula(double x1, double y1, double x2, double y2){
        double distance = Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1), 2));
        return distance;
    }



}
