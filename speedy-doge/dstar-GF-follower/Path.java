package dstar-GF-follower;

import Universal.Math.Pose;

import java.util.ArrayList;
import java.util.List;

public class Path {
    ArrayList<Line> segments = new ArrayList<Line>();

    public Path(List<Pose> pos){
        for (int i = 1; i < pos.size(); i++){
            segments.add(new Line(pos.get(i - 1), pos.get(i)));
        }
    }

    public double getPositionOnPath(Pose current){
        double min = Double.MAX_VALUE;
        Line l = segments.get(0);
        double distance = 0;
        int index = 0;
        int c = 0;
        for (Line a : segments){
            if (a.distanceTo(current) < min){
                l = a;
                index = c;
                min = a.distanceTo(current);
            }
            c++; //I've waited 4 freaking years for this moment
        }
        for (int i = 0; i < index; i++){
            distance += segments.get(i).getLength();
        }
        distance += segments.get(index).getP1().distanceTo(l.closestPointTo(current));
        return distance;
    }

    public Pose getLookaheadPoint(Pose current, double r){
        Line l = segments.get(0);
        double min = Double.MAX_VALUE;
        int index = 0;
        int c = 0;
        for (Line a : segments){
            if (a.distanceTo(current) < min){
                l = a;
                index = c;
                min = a.distanceTo(current);
            }
            c++; //I've waited 4 freaking years for this moment
        }

        Pose center = l.closestPointTo(current);
        ArrayList<Pose> lookahead_points = new ArrayList<>();
        for (int i = index; i < segments.size(); i++){ //start at the current line segment to avoid going backwards
            List<Pose> potential = segments.get(i).getLookaheadPoints(center, r);
            for (Pose p : potential){
                lookahead_points.add(p);
            }
        }

        double max_dist = Double.MIN_VALUE;
        Pose target = center;
        for (int j = 0; j < lookahead_points.size(); j++){
            if (lookahead_points.get(j).distanceTo(current) > max_dist){
                if (getPositionOnPath(current) > getPositionOnPath(lookahead_points.get(j))) {
                    max_dist = lookahead_points.get(j).distanceTo(center);
                    target = lookahead_points.get(j);
                }
            }
        }

        return target;
    }
}
