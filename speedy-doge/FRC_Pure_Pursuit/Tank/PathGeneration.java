package FRC_Pure_Pursuit.Tank;

import java.util.ArrayList;

public class PathGeneration {
    static double[] velocities = new double[]{};
    static double max_rate = 5.6;

    public static void injectPointsIntoPath(){
        ArrayList<double[]> temp = new ArrayList<double[]>();
        for(int i = 0; i<Path.coords.size()-1; i++){
            double[] start = Path.coords.get(i); //start point of segment
            double[] end = Path.coords.get(i+1); //end point of segment
            ArrayList<double[]> pointsForSegment = injectPoints(start[0],start[1],end[0],end[1]);
            temp.addAll(pointsForSegment);
        }
        temp.add(Path.coords.get(Path.coords.size()-1)); //add last point
        Path.coords = temp;
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

    public static double distance_formula(double x1, double y1, double x2, double y2){
        double distance = Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1), 2));
        return distance;
    }

    public static double[][] smoother(double[][] path, double weight_data, double weight_smooth, double tolerance)
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

    public static void calculateVelocity() {

        velocities = new double[Path.coords.size()];
        double max_velocity_path = 6;
        double k = 5;
        int lastPointVelocity = 0;
        velocities[Path.coords.size() - 1] = lastPointVelocity;
        for (int x = Path.coords.size() - 2; x >= 0; x--) {

            double oldVelo = Math.min(max_velocity_path, (k / Lookahead.curvatures[x]));
            double[] firstPoint = Path.coords.get(x + 1);
            double[] secondPoint = Path.coords.get(x);
            double distance = PathGeneration.distance_formula(firstPoint[0], firstPoint[1], secondPoint[0], secondPoint[1]);
            double newVelo = Math.min(oldVelo, Math.sqrt(Math.pow(velocities[x + 1], 2) + 2 * max_rate * distance));
            velocities[x] = newVelo;

        }
    }

    public static void createPath(){
        PathGeneration.injectPointsIntoPath();
        double[][] multiPointPath = new double[Path.coords.size()][2];
        for(int x = 0; x<Path.coords.size(); x++){
            multiPointPath[x][0] = Path.coords.get(x)[0]; //x coord
            multiPointPath[x][1] = Path.coords.get(x)[1]; //y coord
        }
        double[][] smoothPath = PathGeneration.smoother(multiPointPath, 0.001, 0.999, 0.001); //a is 1-b
        Path.coords.clear(); //empty current coords array
        //loop through smoothPath and copy the data to coords
        for(int i =0; i<smoothPath.length; i++){
            Path.coords.add(smoothPath[i]);
        }
        Lookahead.calculateDistances();
        Lookahead.calculateCurve();
        calculateVelocity();
    }
}
