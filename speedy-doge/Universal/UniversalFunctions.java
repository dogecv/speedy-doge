package Universal;

import java.util.Arrays;

/**
 * Contains miscellaneous useful functions
 */
public class UniversalFunctions {

    /*
    Caps the test value at min and max
     */
    public static double clamp(double min, double test, double max) {
        if (max < min) {
            double temp = min;
            min = max;
            max = temp;
        }
        return Math.max(Math.min(max, test), min);
    }

    /*
    Returns if test falls between two given deviations from the given center point
     */
    public static boolean withinTolerance(double test, double centerPoint, double lowerLimit, double upperLimit) {
        return test == clamp(centerPoint - lowerLimit, test, centerPoint + upperLimit);
    }

    /*
    Returns if test falls between two given values
     */
    public static boolean withinTolerance(double test, double lowerLimit, double upperLimit) {
        return test == clamp(lowerLimit, test, upperLimit);
    }

    /*
    Rounds the given double variable away from zero
     */
    public static double round(double d) {
        if (d < 0) {
            return Math.floor(d);
        }
        return Math.ceil(d);
    }

    /*
    Returns a new angle which represents the normalized difference of the two given angles in degrees
     */
    public static double normalizeAngleDegrees(double angle, double newStartAngle) {
        angle -= newStartAngle;
        return normalizeAngleDegrees(angle);
    }

    /*
    Returns a new angle which represents the given angle normalized to between 0 degrees and 360 degrees
     */
    public static double normalizeAngleDegrees(double angle) {
        double a2 = Math.abs(angle) % 360;
        if (Math.abs(angle) != angle) {
            return 360 - a2;
        }
        return a2;
    }

    /*
    Returns a new angle which represents the normalized difference of the two given angles
     */
    public static double normalizeAngleRadians(double angle, double newStartAngle) {
        angle -= newStartAngle;
        return normalizeAngleRadians(angle);
    }

    /*
    Returns a new angle which represents the given angle normalized to between 0 degrees and 2pi radians
     */
    public static double normalizeAngleRadians(double angle) {
        return Math.toRadians(normalizeAngleDegrees(Math.toDegrees(angle)));
    }

    /*
    Returns a new angle which represents the given angle between two angles, normalized to between -180 degrees and 180 degrees
     */
    public static double normalizeAngle180(double angle, double newStartAngle) {
        double ang = normalizeAngleDegrees(angle, newStartAngle);
        return normalizeAngle180(ang);
    }

    /*
    Returns a new angle which represents the given angle normalized to between -180 degrees and 180 degrees
     */
    public static double normalizeAngle180(double angle) {
        double ang = normalizeAngleDegrees(angle);
        if(ang > 180){
            ang = -360 + ang;
        }
        return ang;
    }
    /*
    Returns a new angle which represents the given angle normalized to between -pi radians and pi radians
     */
    public static double normalizeAngle180Radians(double angle){
        return Math.toRadians(normalizeAngle180(Math.toDegrees(angle)));
    }
    /*
    Returns a new angle which represents the given angle between two angles, normalized to between -pi radians and pi radians
     */
    public static double normalizeAngle180Radians(double angle, double newStartAngle){
        return Math.toRadians(normalizeAngle180(Math.toDegrees(angle), Math.toDegrees(newStartAngle)));
    }
    /*
    Returns the maximum value of the parameters
     */
    public static double max(double... ds) {
        switch(ds.length){
            case 0: return 0.0;
            case 1: return ds[0];
            case 2: return Math.max(ds[0], ds[1]);
            default: return Math.max(ds[0],
                    max(Arrays.copyOfRange(ds, 1, ds.length)));
        }
    }

    /*
    Returns the maximum value of the absolute value of the parameters
     */
    public static double maxAbs(double... ds) {
        for (int i = 0; i < ds.length; ++i){
            ds[i] = Math.abs(ds[i]);
        }
        switch(ds.length){
            case 0: return 0.0;
            case 1: return ds[0];
            case 2: return Math.max(ds[0], ds[1]);
            default: return Math.max(ds[0],
                    max(Arrays.copyOfRange(ds, 1, ds.length)));
        }
    }

    /*
    Returns the minimum value of the parameters
     */
    public static double min(double... ds){
        switch(ds.length){
            case 0: return 0.0;
            case 1: return ds[0];
            case 2: return Math.min(ds[0], ds[1]);
            default: return Math.min(ds[0],
                    min(Arrays.copyOfRange(ds,1, ds.length)));
        }
    }

    /*
    Returns the minimum value of the absolute value of the parameters
     */
    public static double minAbs(double... ds){
        for (int i = 0; i < ds.length; ++i){
            ds[i] = Math.abs(ds[i]);
        }
        switch(ds.length){
            case 0: return 0.0;
            case 1: return ds[0];
            case 2: return Math.min(ds[0], ds[1]);
            default: return Math.min(ds[0],
                    min(Arrays.copyOfRange(ds,1, ds.length)));
        }
    }

    /*
    Returns an array whose elements make up the contents of the given string separated by commas
     */
    public static String[] formatArrayStr(String str, int len){
        String[] ret = new String[len];
        int i = 0, ind = str.indexOf(","), next;
        while (ind != -1) {
            next = str.indexOf(",", ind + 1);
            if (next == -1)
                next = str.length();
            ret[i] = str.substring(ind, next);
        }
        return ret;
    }

    public static double getTimeInSeconds(){
        return System.nanoTime() / Math.pow(10, 9);
    }
    /*
    Rho represents the xy angle, theta represents the zy angle
     */
    public static double[] sphericalToCartesian(double rad, double theta, double rho) {
        double  x = rad * Math.sin(theta) * Math.cos(rho),
                y = rad * Math.sin(theta) * Math.sin(rho),
                z = rad * Math.cos(theta);
        double[] cartesian = {x, y, z};
        return cartesian;
    }

    public static double[] cartesianToSpherical(double x, double y, double z){
        double  radius = Math.sqrt(x * x + y * y + z * z),
                theta  = Math.atan2(Math.hypot(x, y), z),
                rho    = Math.atan2(y, x);
        double[] spherical = {radius, theta, rho};
        return spherical;
    }

    public static double[] convertToArray(double x){
        double[] output = {x};
        return output;
    }
}