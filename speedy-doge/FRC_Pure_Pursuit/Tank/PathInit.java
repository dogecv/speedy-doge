public class PathInit {
    static boolean running = true;

    static long lastTime = System.currentTimeMillis();
    static double timeBetweencalls = 0;


    private static boolean donePath(){
        double wheelDiameter = 4;
        double ticks_per_revolution = 1120;
        double mass = 23;
        double leftFrontVelocity = 5; //get velocity from motor motor.getVelocity()
        double leftBackVelocity = 5; //get velocity from motor motor.getVelocity()
        double leftVelocity = (((leftFrontVelocity+leftBackVelocity)/2)/ticks_per_revolution)*(wheelDiameter*Math.PI); //get left sides velocity
        double rightFrontVelocity = 5; //get velocity from motor motor.getVelocity()
        double rightBackVelocity = 5; //get velocity from motor motor.getVelocity()
        double rightVelocity = (((rightFrontVelocity+rightBackVelocity)/2)/ticks_per_revolution)*(wheelDiameter*Math.PI); //get right side velocity
        double velocity = (leftVelocity+rightVelocity)/2;
        double momentum = mass * velocity;
        double[] lastPoint = Path.coords.get(Path.coords.size()-1);
        boolean done = PathGeneration.distance_formula(Odometry.x, Odometry.y, lastPoint[0], lastPoint[1]) < momentum; //stop when within two inches of endpoint
        return done;
    }

    public static void run(Path pathToRun){
        Path.coords = pathToRun.getCoords();
        PathGeneration.createPath(); //run path initialization
        running = true; //say you are starting a path
        while(running){ //while opmode is active
            long currentTime = System.currentTimeMillis();
            timeBetweencalls = ((currentTime - lastTime) / 1000.0);
            double[] motorPowers = Lookahead.lookahead(timeBetweencalls);
            //LeftMotor.setPower(motorPowers[0]);
            //RightMotor.setPower(motorPowers[1]);
            boolean done = donePath();
            running = ! done;

            lastTime = currentTime;
            //make a 20 ms delay here, you would want to do this however you are supposed to for FTC robots.
        }
    }
}

