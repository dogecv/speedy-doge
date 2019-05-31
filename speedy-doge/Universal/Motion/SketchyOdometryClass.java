package Universal.Motion;

import Universal.Math.Pose;
import Universal.Math.Vector2;

public class SketchyOdometryClass {
    public class Wheels3{
        public double encPerInch;

        //the angles are the angles of the motors, not their wheels
        public Pose wheel1 = new Pose();
        public Pose wheel2 = new Pose();
        public Pose wheel3 = new Pose();
        public Wheels3(Pose p1, Pose p2, Pose p3){
            wheel1 = p1;
            wheel2 = p2;
            wheel3 = p3;
        }

        public Pose standardPositionTrack(Pose currentPos, double x/*read3*/, double l/*read2*/, double r/*read1*/){
            double diff1 = Math.cos(wheel1.angleOfVector() - wheel1.angle),
                    diff2 = Math.cos(wheel2.angleOfVector() - wheel2.angle),
                    diff3 = Math.cos(wheel3.angleOfVector() - wheel3.angle),
                    xDiff = wheel1.radius() / wheel2.radius(),
                    angle = (r / diff1 - l / diff2 * xDiff) / ((wheel1.radius() * 2) * encPerInch);

            x -= angle * wheel3.radius() * diff3;
            r -= angle * wheel1.radius() * diff1;
            l -= angle * wheel2.radius() * diff2;

            //assuming the calculations were done correctly, l and r should now be equal
            Vector2 velocity = new Vector2(x, l);
            Vector2 vel2 = new Vector2();

            if (angle != 0) {
                double rad = velocity.magnitude() / angle;
                vel2.setFromPolar(rad, angle);
                vel2.x -= rad;
                vel2.rotate(velocity.angle());
            } else {
                vel2 = velocity;
            }

            currentPos.x += vel2.x;
            currentPos.y += vel2.y;
            currentPos.angle += angle;

            return currentPos;
        }
    }
}