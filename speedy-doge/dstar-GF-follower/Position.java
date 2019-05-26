package org.firstinspires.ftc.teamcode;

public class Position {
    public double x;
    public double y;
    public double theta;

    public Position(){
        this.x = 0;
        this.y = 0;
        this.theta = 0;
    }

    public Position(double x, double y, double theta){
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta;
    }

    public double distanceTo(Position p){
        return Math.hypot(p.getX() - this.x, p.getY() - this.y);
    }

    public double angleTo(Position p){
        return Math.atan2(p.getY() - this.y, p.getX() - this.x);
    }

    public Position add(Position p){
        double tx = this.x + p.getX();
        double ty = this.y + p.getY();
        double t_theta = this.theta + p.getTheta();

        return new Position(tx, ty, t_theta);
    }


    public String toString(){
        return "(" + Double.toString(this.x) + ", " + Double.toString(this.y) + ", " + Double.toString(theta) + ")";
    }
}
