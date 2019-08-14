package FRC_Pure_Pursuit.Tank;

import java.util.ArrayList;

public class Path{

    public static  ArrayList<double[]> coords;

    public Path() {
        coords = new ArrayList<double[]>();
    }

    public static Path leftPath(){
        Path path = new Path();
        path.wayPoint(6, 4);
        path.wayPoint(7, 7);
        path.wayPoint(8, 9);
        path.wayPoint(10, 12);
        return path;
    }

    public static Path rightPath(){
        Path path = new Path();
        path.wayPoint(3, 2);
        path.wayPoint(4, 5);
        return path;
    }

    public ArrayList<double[]> getCoords() { //public method to get coords
        return coords;
    }

    private void wayPoint(double x1, double y1){
        coords.add(new double[]{x1, y1});
    }

}