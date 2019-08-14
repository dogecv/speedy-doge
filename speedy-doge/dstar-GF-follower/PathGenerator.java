package dstar-GF-follower;

import Universal.Math.Pose;

import java.util.ArrayList;
import java.util.List;

public class PathGenerator {
    DStarLite pathfinder = new DStarLite();
    List<Pose> obstacles = new ArrayList<Pose>();

    public PathGenerator(Pose start, Pose target){
        pathfinder.init((int)start.x, (int)start.y, (int)target.x, (int)target.y);
    }

    public void addObstacle(Pose p){
        pathfinder.updateCell((int)p.x, (int)p.y, -1);
        obstacles.add(p);
    }

    public void setPathfinder(DStarLite p){
        this.pathfinder = p;
    }

    public DStarLite getPathfinder() {
        return pathfinder;
    }

    public Path getPath(){
        pathfinder.replan();

        List<State> raw_path = pathfinder.getPath();
        List<Pose> filtered_path = new ArrayList<Pose>();

        double previous_angle = Math.atan2(raw_path.get(1).y - raw_path.get(0).y, raw_path.get(1).x - raw_path.get(0).x);
        filtered_path.add(new Pose(raw_path.get(0).x, raw_path.get(0).y, previous_angle));
        for (int i = 1; i < raw_path.size(); i++){
            double angle = Math.atan2(raw_path.get(i).y - raw_path.get(i - 1).y, raw_path.get(i).x - raw_path.get(i - 1).x);
            filtered_path.add(new Pose(raw_path.get(i).x, raw_path.get(i).y, angle));
        }
        return new Path(filtered_path);
    }

    public double getLookAheadDistance(Pose current, double lookahead_gain, double minimum_distance){ //lookahead gain is a coefficient that decides how aggressively the lookahead distance is incremented given distance from the nearest obstacle
        double min = Double.MAX_VALUE;

        for (int i = 0; i < obstacles.size(); i++){
            if (obstacles.get(i).distanceTo(current) < min){
                min = obstacles.get(i).distanceTo(current);
            }
        }

        return lookahead_gain * (min - minimum_distance);
    }
}
