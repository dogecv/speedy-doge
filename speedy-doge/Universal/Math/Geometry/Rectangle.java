package Universal.Math.Geometry;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import Universal.UniversalFunctions;

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
        if(pose.y > 0){
            if(pose.x > 0){
                pose.x -= width / 2;
                pose.y -= height/2;
                outputVect = new Vector2(width/2, height/2);
                if (pose.y < 0)
                    outputVect.y += pose.y;
                else if (pose.x < 0)
                    outputVect.x += pose.x;

                pose.x += width / 2;
                pose.y += height/2;
            }
            else{
                pose.x += width / 2;
                pose.y -= height/2;
                outputVect = new Vector2(-width/2, height/2);
                if (pose.y < 0)
                    outputVect.y += pose.y;
                else if (pose.x > 0)
                    outputVect.x -= pose.x;

                pose.x -= width / 2;
                pose.y += height/2;
            }
        }
        else {
            if(pose.x > 0){
                pose.x -= width / 2;
                pose.y += height/2;
                outputVect = new Vector2(width/2, -height/2);
                if (pose.y > 0)
                    outputVect.y -= pose.y;
                else if (pose.x < 0)
                    outputVect.x += pose.x;

                pose.x += width / 2;
                pose.y -= height/2;
            }
            else{
                pose.x += width / 2;
                pose.y += height/2;
                outputVect = new Vector2(-width/2, -height/2);
                if (pose.y > 0)
                    outputVect.y -= pose.y;
                else if (pose.x > 0)
                    outputVect.x -= pose.x;

                pose.x -= width / 2;
                pose.y -= height/2;
            }
        }
        outputVect.rotate(position.angle);
        outputVect.x += position.x;
        outputVect.y += position.y;
        return outputVect;
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
