package org.firstinspires.ftc.teamcode.Universal.Math;

public class Pose {
    public double x, y, angle;

    public Pose(){
        x = 0;
        y = 0;
        angle = 0;
    }
    public Pose(double x, double y){
        this(x, y, 0);
    }
    public Pose(Pose pose){
        this(pose.x, pose.y, pose.angle);
    }
    public Pose(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
    public void add(Vector2 v){
        x += v.x;
        y += v.y;
    }
    public void add(Pose p){
        x += p.x;
        y += p.y;
        angle += p.angle;
    }
    public Vector2 toVector(){
        return new Vector2(x, y);
    }
    public double radius(){
        return Math.hypot(x, y);
    }
    public double angleOfVector(){
        return Math.atan2(y, x);
    }
    public Pose clone(){
        return new Pose(x, y, angle);
    }
    public void rotate(double angle){
        Vector2 temp = new Vector2(x, y);
        temp.rotate(angle);
        x = temp.x;
        y = temp.y;
    }
    public double distanceTo(Pose p){
        return Math.hypot(x - p.x, y - p.y);
    }
    public String toString(){
        return x + ", " + y + ", " + angle;
    }
}
