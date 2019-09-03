package Universal.Math.Geometry;

import FRC_Pure_Pursuit.Tank.Vector;
import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalFunctions;

/**
* TESTED
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
        this.position = location;
    }

    public Vector2 getClosestPoint (Vector2 pose) {
        pose.x -= position.x;
        pose.y -= position.y;
        pose.rotate(-position.angle);

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

        outputVect.rotate(position.angle);
        outputVect.x += position.x;
        outputVect.y += position.y;
        return outputVect;
    }

    public Vector2 getClosestVector(Vector2 pose) {
        Vector2 temp = position.toVector();
        temp.subtract(pose);
        return polarVector(temp.angle());
    }
    public Vector2 polarVector (double angle) {
        angle -= position.angle;
        Vector2 output = new Vector2();
        angle = UniversalFunctions.normalizeAngleRadians(angle);
        if (angle < Math.atan2(height, width))
            output = new Vector2(width / 2, Math.tan(angle) * width / 2);

        else if (angle < Math.PI / 2 + Math.atan2(width, height))
            output = new Vector2(-Math.tan(angle - Math.PI / 2) * height / 2, height / 2);

        else if (angle < Math.PI + Math.atan2(height, width))
            output = new Vector2(-width / 2, -Math.tan(angle - Math.PI) * width / 2);

        else if (angle < Math.PI * 3 / 2 + Math.atan2(width, height))
            output = new Vector2(Math.tan(angle - Math.PI / 2) * height / 2, height / 2);

        else
            output = new Vector2(width / 2, Math.tan(angle) * width / 2);

        return output;
    }
}
