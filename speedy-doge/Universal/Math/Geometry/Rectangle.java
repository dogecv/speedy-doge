package Universal.Math.Geometry;

import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalFunctions;

/**
* Used to represent rectangular objects
*/

public class Rectangle extends Shape {

    public double width, height;

    public Rectangle (double width, double height){
        this.width = width;
        this.height = height;
    }

    public Rectangle (Pose location, double width, double height){
        this.width = width;
        this.height = height;
        this.location = location;
    }

    public Vector2 getClosestPoint (Vector2 pose) {

        //normalizes pose relative to the center of the Rectangle
        pose.x -= location.x;
        pose.y -= location.y;
        pose.rotate(-location.angle);

        Vector2 outputVect = new Vector2();

        if(pose.y > height/2){
            if(pose.x > width/2 )
                outputVect = new Vector2(width/2, height/2);
            else if (pose.x < -width/2)
                outputVect = new Vector2(-width/2, height /2);
            else
                outputVect = new Vector2(pose.x, height/2);
        }
        else if(pose.y < -height/2){
            if(pose.x > width/2 )
                outputVect = new Vector2(width/2, -height/2);
            else if (pose.x < -width/2)
                outputVect = new Vector2(-width/2, -height /2);
            else
                outputVect = new Vector2(pose.x, -height/2);
        }
        else{
            if(pose.x > width/2)
                outputVect = new Vector2(width/2, pose.y);
            else if(pose.x < -width/2)
                outputVect = new Vector2(-width/2, pose.y);
        }

        //renormalizes outputVect to the original location of the Rectangle
        outputVect.rotate(location.angle);
        outputVect.x += location.x;
        outputVect.y += location.y;
        return outputVect;
    }
    public int getClosestSide (Vector2 pose) {
        System.out.println("closestpose "+pose);
        pose = pose.clone();
        //normalizes pose relative to the center of the Rectangle
        pose.x -= location.x;
        pose.y -= location.y;
        pose.rotate(-location.angle);
        System.out.println("newclosestpose "+pose);

        if(pose.y > height/2 && pose.x < width/2 && pose.x > -width/2)
            return 2;
        else if(pose.y < -height/2 && pose.x < width/2 && pose.x > -width/2)
            return 4;
        else if(pose.x > width/2 && pose.y > -height/2 && pose.y < height/2)
                return 1;
        else if(pose.x < -width/2 && pose.y > -height/2 && pose.y < height/2)
            return 3;
        return 0;
    }
    /*
    returns a vector originating from the center of the Rectangle at a given angle that lies on the Rectangle
     */
    public Vector2 polarVector (double angle) {

        //reorients and normalizes angle relative to location.angle
        angle -= location.angle;
        Vector2 output;
        angle = UniversalFunctions.normalizeAngleRadians(angle);

        //if the angle points to the top part of the right edge...
        if (angle < Math.atan2(height, width))
            output = new Vector2(width / 2, Math.tan(angle) * width / 2);

        //if the angle points to the top edge...
        else if (angle < Math.PI / 2 + Math.atan2(width, height))
            output = new Vector2(-Math.tan(angle - Math.PI / 2) * height / 2, height / 2);

        //if the angle points to the left edge...
        else if (angle < Math.PI + Math.atan2(height, width))
            output = new Vector2(-width / 2, -Math.tan(angle - Math.PI) * width / 2);

        //if the angle points to the bottom edge...
        else if (angle < Math.PI * 3 / 2 + Math.atan2(width, height))
            output = new Vector2(Math.tan(angle - Math.PI / 2) * height / 2, height / 2);

        //if the angle points to the bottom part of the right edge...
        else
            output = new Vector2(width / 2, Math.tan(angle) * width / 2);

        return output;
    }
}
