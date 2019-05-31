package Universal;

import Universal.Math.Pose;

public class CoordinateSys{

    //coordinate system that relativeLocation is defined in
    public CoordinateSys masterSys;

    //location of the system relative to masterSys
    public Pose relativeLocation;

    //the number of coordinate systems abstracting the system from the origin
    private int depth;

    public CoordinateSys() {
        depth = 0;
        relativeLocation = new Pose();
    }
    public CoordinateSys(CoordinateSys masterSys, Pose locationRelativeToMaster) {
        this.masterSys = masterSys;
        depth = findDepth();
        relativeLocation = locationRelativeToMaster;
    }

    //calculates the depth of the system
    private int findDepth() {
        int testDepth = 1;
        CoordinateSys tempSys = new CoordinateSys();
        tempSys = masterSys.clone();
        while (tempSys.getDepth() > 0) {
            tempSys = tempSys.masterSys.clone();
            testDepth++;
        }
        return testDepth;
    }

    public Pose translate (CoordinateSys destinationSys) {
        return translate(destinationSys, new Pose());
    }

    public Pose translate (CoordinateSys destinationSys, Pose startingPose) {
        return translate(destinationSys, new Pose(), startingPose);
    }

    //defines startingPose (a pose defined in this coordinate system) relative to destinationPose (a pose defined in destinationSys)
    public Pose translate (CoordinateSys destinationSys, Pose destinationPose, Pose startingPose) {
        Pose relativeDestination = destinationSys.translateToOrigin(destinationPose);
        Pose relativeStartingPose = translateToOrigin(startingPose);
        relativeStartingPose.x -= relativeDestination.x;
        relativeStartingPose.y -= relativeDestination.y;
        relativeStartingPose.rotate(-relativeDestination.angle);
        return relativeStartingPose;
    }

    //defines startingPose relative to the origin
    public Pose translateToOrigin(Pose startingPose) {
        Pose translatedPose = new Pose ();
        Pose tempRelativeLocation = new Pose();
        CoordinateSys tempSys = new CoordinateSys();
        tempSys = this.clone();
        for (int i = depth; i > 0; i--) {
            tempRelativeLocation = tempSys.relativeLocation.clone();
            tempSys = masterSys.clone();
            translatedPose.rotate(-tempRelativeLocation.angle);
            translatedPose.x += tempRelativeLocation.x;
            translatedPose.y += tempRelativeLocation.y;
        }
        translatedPose.add(startingPose);
        return translatedPose;
    }

    //creates a copy in memory of the coordinate system
    public CoordinateSys clone(){
        return new CoordinateSys (masterSys, relativeLocation);
    }

    //returns the depth of the system
    public int getDepth() {
        return depth;
    }
}