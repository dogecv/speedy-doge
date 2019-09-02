package Universal.Math.Geometry;

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

        //declares the outputVect variable
        Vector2 outputVect;

        if(pose.y > 0){

            //if pose is in the first quadrant...
            if(pose.x > 0){

                //normalize pose relative to the top right corner
                pose.x -= width / 2;
                pose.y -= height/2;

                //points outputVect to the top right corner
                outputVect = new Vector2(width/2, height/2);

                //if the closest point is on the right edge...
                if (pose.y < 0)

                    //modify outputVect.y to point towards the closest point
                    outputVect.y -= pose.y;

                //if the closest point is on the top edge...
                else if (pose.x < 0)

                    //modify outputVect.x to point towards the closest point
                    outputVect.x -= pose.x;

                //renormalizes from the corner back to the center
                pose.x += width / 2;
                pose.y += height/2;
            }

            //if pose is in the second quadrant...
            else{

                //normalize pose relative to the top left corner
                pose.x += width / 2;
                pose.y -= height/2;

                //points outputVect to the top left corner
                outputVect = new Vector2(-width/2, height/2);

                //if the closest point is on the left edge...
                if (pose.y < 0)

                    //modify outputVect.y to point towards the closest point
                    outputVect.y -= pose.y;

                //if the closest point is on the top edge...
                else if (pose.x > 0)

                    //modify outputVect.x to point towards the closest point
                    outputVect.x += pose.x;

                //renormalizes from the corner back to the center
                pose.x -= width / 2;
                pose.y += height/2;
            }
        }
        else {

            //if pose is in the fourth quadrant...
            if(pose.x > 0){

                //normalize pose relative to the bottom right corner
                pose.x -= width / 2;
                pose.y += height/2;

                //points outputVect to the bottom right corner
                outputVect = new Vector2(width/2, -height/2);

                //if the closest point is on the right edge...
                if (pose.y > 0)

                    //modify outputVect.y to point towards the closest point
                    outputVect.y += pose.y;

                //if the closest point is on the bottom edge...
                else if (pose.x < 0)

                    //modify outputVect.x to point towards the closest point
                    outputVect.x -= pose.x;

                //renormalizes from the corner back to the center
                pose.x += width / 2;
                pose.y -= height/2;
            }

            //if pose is in the third quadrant...
            else{

                //normalize pose relative to the bottom left corner
                pose.x += width / 2;
                pose.y += height/2;

                //points outputVect to the bottom left corner
                outputVect = new Vector2(-width/2, -height/2);

                //if the closest point is on the right edge...
                if (pose.y > 0)

                    //modify outputVect.y to point towards the closest point
                    outputVect.y += pose.y;

                //if the closest point is on the bottom edge...
                else if (pose.x > 0)

                    //modify outputVect.x to point towards the closest point
                    outputVect.x += pose.x;

                //renormalizes from the corner back to the center
                pose.x -= width / 2;
                pose.y -= height/2;
            }
        }

        //renormalizes outputVect to the original location of the Rectangle
        outputVect.rotate(location.angle);
        outputVect.x += location.x;
        outputVect.y += location.y;
        return outputVect;
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
