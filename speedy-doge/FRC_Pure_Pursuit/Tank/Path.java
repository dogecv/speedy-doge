package FRC_Pure_Pursuit.Tank;

import Universal.Math.Vector2;

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

    public void wayPoint(double x1, double y1){
        coords.add(new double[]{x1, y1});
    }

    public int getSize(){
        return coords.size();
    }

    public Vector2 pointOnPathByPercentage(double percentage){
        double totalLen = getTotalPathLength();
        double len = 0;
        for(int i = 0; i < coords.size() - 1; i++){
            double[] p1 = coords.get(i), p2 = coords.get(i+1);
            len += Math.hypot(p1[0]-p2[0], p1[1]-p2[1]);
            if (len > totalLen * percentage){
                Vector2 vect = new Vector2(p1[0]-p2[0],p1[1]-p2[1]);
                vect.scalarMultiply(percentage);
                return vect;
            }
        }
        return new Vector2();
    }
    public double getTotalPathLength(){
        double totalPathLength = 0;
        for(int i = 0; i < coords.size() - 1; i++){
            double[] p1 = coords.get(i), p2 = coords.get(i+1);
            totalPathLength += Math.hypot(p1[0]-p2[0], p1[1]-p2[1]);
        }
        return totalPathLength;
    }

}