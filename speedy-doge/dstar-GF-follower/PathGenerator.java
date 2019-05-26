package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.List;

public class PathGenerator {
    DStarLite pathfinder = new DStarLite();

    public PathGenerator(Position start, Position target){
        pathfinder.init((int)start.getX(), (int)start.getY(), (int)target.getX(), (int)target.getY());
    }

    public void addObstacle(Position p){
        pathfinder.updateCell((int)p.getX(), (int)p.getY(), -1);
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
        List<Position> filtered_path = new ArrayList<Position>();

        double previous_angle = Math.atan2(raw_path.get(1).y - raw_path.get(0).y, raw_path.get(1).x - raw_path.get(0).x);
        filtered_path.add(new Position(raw_path.get(0).x, raw_path.get(0).y, previous_angle));
        for (int i = 1; i < raw_path.size(); i++){
            double angle = Math.atan2(raw_path.get(i).y - raw_path.get(i - 1).y, raw_path.get(i).x - raw_path.get(i - 1).x);
            filtered_path.add(new Position(raw_path.get(i).x, raw_path.get(i).y, angle));
        }
        return new Path(filtered_path);
    }
}
