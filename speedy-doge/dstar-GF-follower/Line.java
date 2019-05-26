package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Line {
    Position p1;
    Position p2;

    public void Line(){
        p1 = new Position();
        p2 = new Position();
    }

    public Line(Position p1, Position p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public void setP1(Position p1){
        this.p1 = p1;
    }

    public void setP2(Position p2) {
        this.p2 = p2;
    }

    public Position getP1() {
        return p1;
    }

    public Position getP2() {
        return p2;
    }

    public double getLength(){
        return p1.distanceTo(p2);
    }

    public double distanceTo(Position p){
        double m = (p2.getX() == p1.getX() ? 999 : ((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())));
        double m2 = -1 / m;
        double b2 = p.getY() - (m2 * p.getX());
        double b = p1.getY() - (m * p1.getX());

        double x = (b2 - b) / (m - m2);
        double y = (m * x) + b;

        return Math.hypot(x - p.getX(), y - p.getY());
    }

    public Position closestPointTo(Position p){
        double m = (p2.getX() == p1.getX() ? 999 : ((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())));
        double m2 = -1 / m;
        double b2 = p.getY() - (m2 * p.getX());
        double b = p1.getY() - (m * p1.getX());

        double x = (b2 - b) / (m - m2);
        double y = (m * x) + b;

        return new Position(x, y, Math.atan(m));
    }

    public List<Position> intersectWithCircleDeltas(Position center, double radius){
        double baX = p2.getX() - p1.getX();
        double baY = p2.getY() - p1.getY();
        double caX = center.x - p1.getX();
        double caY = center.y - p1.getY();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }
        // if disc == 0 ... dealt with later
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Position f_p1 = new Position(p1.getX() - baX * abScalingFactor1, p1.getY()
                - baY * abScalingFactor1, Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(f_p1);
        }
        Position f_p2 = new Position(p1.getX() - baX * abScalingFactor2, p1.getY()
                - baY * abScalingFactor2, Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        return Arrays.asList(f_p1, p2);
    }

    public boolean insideLine(Position p){
        return ((p.getX() < p1.getX() && p.getX() > p2.getX()) && (p.getY() < p1.getY() && p.getY() > p2.getY())) || ((p.getX() > p1.getX() && p.getX() < p2.getX()) && (p.getY() > p1.getY() && p.getY() < p2.getY()));
    }

    public boolean isBehind(Position current, Position target){
        Position new_target = closestPointTo(target);
        Position new_current = closestPointTo(current);

        double angle = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
        double m = (p2.getX() == p1.getX() ? 999 : ((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())));

        double t1 = 0;
        double t2 = 0;

        if (angle > Math.PI / 2 || angle < -Math.PI / 2){
            t1 = p1.getX() - new_current.getX();
            t2 = p1.getX() - new_target.getX();
            return t2 < t1;
        }
        else if (angle < Math.PI / 2 && angle > -Math.PI / 2){
            t1 = -p1.getX() + new_current.getX();
            t2 = -p1.getX() + new_target.getX();
            return t2 < t1;
        }
        else if (angle == Math.PI / 2){
            return new_target.getY() < new_current.getY();
        }
        else if (angle == -Math.PI / 2){
            return new_target.getY() > new_current.getY();
        }
        return false;
    }

    public List<Position> getLookaheadPoints(Position p, double r){
        Position center = closestPointTo(p);
        List<Position> deltas = intersectWithCircleDeltas(center, r);
        int i = 0;
        while (i < deltas.size()){
            Position potential = center.add(deltas.get(i));
            if (insideLine(potential) && !isBehind(center, potential)){
                deltas.set(i, potential);
                i++;
            }
            else if (!isBehind(center, potential) && !insideLine(potential)){
                deltas.remove(i);
            }
            else if (isBehind(center, potential)){
                deltas.remove(i);
            }
        }
        return deltas;
    }
}
