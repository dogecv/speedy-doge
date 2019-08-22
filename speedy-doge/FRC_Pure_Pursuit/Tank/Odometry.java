package FRC_Pure_Pursuit.Tank;
public class Odometry {
    static double x;
    static double y;
    static double robot_angle;

    public static void system(){ //better odometry system goes here
        x = 0;
        y = 0;
        robot_angle = -180;
        int oldTickR = 0; //getCurrentPos
        int newTickR = 1120; //getCurrentPos
        int oldTickL = 0; //getCurrentPos
        int newTickL = 1120; //getCurrentPos
        double robot_radius = 9;
        robot_angle += (newTickL - newTickR) / (robot_radius * Math.PI);
        //System.out.println("The new heading is " + robot_angle);
        double distance = ((newTickL - oldTickL) + (newTickR - oldTickR)) / 2;
        //System.out.println("The new distance is " + distance);
        if ((robot_angle == 90) || (robot_angle == -90)){
            x = 0;
        }

        else{
            x += distance * Math.cos(Math.toRadians(robot_angle));
        }

        if ((robot_angle == 180) || (robot_angle == -180)){
            y = 0;
        }

        else{
            y += distance * Math.sin(Math.toRadians(robot_angle));
        }

        //System.out.println("The new x coordinate is " + x);
        //System.out.println("The new y coordinate is " + y);

    }
}
