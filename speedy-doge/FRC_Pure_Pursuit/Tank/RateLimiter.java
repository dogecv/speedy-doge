public class RateLimiter {
    double lastLimiterOutput = 0;
    public double rateLimit(double x, double maxRate, double timeBetweencalls){
        double maxChange = timeBetweencalls * maxRate;
        lastLimiterOutput += Lookahead.constrain(x - lastLimiterOutput, -maxChange, maxChange);
        return lastLimiterOutput;
    }
}
