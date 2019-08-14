package dstar-GF-follower;

import Universal.Math.Pose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Line {
    Pose p1;
    Pose p2;

    public void Line(){
        p1 = new Pose();
        p2 = new Pose();
    }

    public Line(Pose p1, Pose p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public void setP1(Pose p1){
        this.p1 = p1;
    }

    public void setP2(Pose p2) {
        this.p2 = p2;
    }

    public Pose getP1() {
        return p1;
    }

    public Pose getP2() {
        return p2;
    }

    public double getLength(){
        return p1.distanceTo(p2);
    }

    public double distanceTo(Pose p){
        double m = (p2.x == p1.x ? 999 : ((p2.y - p1.y) / (p2.x - p1.x)));
        double m2 = -1 / m;
        double b2 = p.y - (m2 * p.x);
        double b = p1.y - (m * p1.x);

        double x = (b2 - b) / (m - m2);
        double y = (m * x) + b;

        return Math.hypot(x - p.x, y - p.y);
    }

    public Pose closestPointTo(Pose p){
        double m = (p2.x == p1.x ? 999 : ((p2.y - p1.y) / (p2.x - p1.x)));
        double m2 = -1 / m;
        double b2 = p.y - (m2 * p.x);
        double b = p1.y - (m * p1.x);

        double x = (b2 - b) / (m - m2);
        double y = (m * x) + b;

        return new Pose(x, y, Math.atan(m));
    }

    public List<Pose> intersectWithCircle(Pose center, double radius){
        double baX = p2.x - p1.x;
        double baY = p2.y - p1.y;
        double caX = center.x - p1.x;
        double caY = center.y - p1.y;

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

        Pose f_p1 = new Pose(p1.x - baX * abScalingFactor1, p1.y
                - baY * abScalingFactor1, Math.atan2(p2.y - p1.y, p2.x - p1.x));
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(f_p1);
        }
        Pose f_p2 = new Pose(p1.x - baX * abScalingFactor2, p1.y
                - baY * abScalingFactor2, Math.atan2(p2.y - p1.y, p2.x - p1.x));
        return Arrays.asList(f_p1, p2);
    }

    public boolean insideLine(Pose p){
        return ((p.x < p1.x && p.x > p2.x) && (p.y < p1.y && p.y > p2.y)) || ((p.x > p1.x && p.x < p2.x) && (p.y > p1.y && p.y < p2.y));
    }

    public boolean isBehind(Pose current, Pose target){
        Pose new_target = closestPointTo(target);
        Pose new_current = closestPointTo(current);

        double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        double m = (p2.x == p1.x ? 999 : ((p2.y - p1.y) / (p2.x - p1.x)));

        double t1 = 0;
        double t2 = 0;

        if (angle > Math.PI / 2 || angle < -Math.PI / 2){
            t1 = p1.x - new_current.x;
            t2 = p1.x - new_target.x;
            return t2 < t1;
        }
        else if (angle < Math.PI / 2 && angle > -Math.PI / 2){
            t1 = -p1.x + new_current.x;
            t2 = -p1.x + new_target.x;
            return t2 < t1;
        }
        else if (angle == Math.PI / 2){
            return new_target.y < new_current.y;
        }
        else if (angle == -Math.PI / 2){
            return new_target.y > new_current.y;
        }
        return false;
    }

    public List<Pose> getLookaheadPoints(Pose p, double r){
        Pose center = closestPointTo(p);
        List<Pose> deltas = intersectWithCircle(center, r);
        int i = 0;
        while (i < deltas.size()){
            Pose potential = deltas.get(i);
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
