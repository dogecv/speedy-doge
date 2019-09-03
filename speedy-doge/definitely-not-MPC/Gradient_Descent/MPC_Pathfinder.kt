package definitely-not-MPC.Gradient_Descent

import com.qualcomm.robotcore.util.Range
import kotlinx.coroutines.*
import definitely-not-MPC.Modified_Math.Pose
import definitely-not-MPC.Modified_Math.Vector2
import kotlin.math.cos
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

class MPC_Pathfinder(start : Pose, end : Pose, obstacles : ArrayList<Pose>) {
    var start = start
    var end = end
    var obstacles = obstacles

    val CONTROL_HORIZON = 5
    val TIMESTEP = 0.1 //tune this to the average loop time

    val Q = 1 //position error
    val R = 0.01 //acceleration error
    val r2 = 0.01 //overall control effort penalizer
    val O = 0.01 //obstacle error

    val EPSILON = 0.001

    val MAX_VELOCITY = 60.0 //tune this to match dt characteristics
    val MAX_ACCEL = 60.0 // tune this to match dt characteristics

    val THETA_GAIN = 1 //todo: tune these
    val VELO_GAIN = 1
    val STOCHASTIC_SCALE = 0.1

    var previous_timestep = System.currentTimeMillis()

    var optimal_control_sequence = ArrayList<Vector2>()
    var previous = Vector2()
    var active = false

    var total_iterations = 0
    val BAILOUT = 100 * CONTROL_HORIZON //todo: tune this, the higher it is the more it tries to optimize the path

    fun init(start : Pose, end : Pose){
        this.start = start
        this.end = end
        for (i in 1..CONTROL_HORIZON){
            optimal_control_sequence.add(Vector2())
        }
    }

    fun addObstacle(obs : Pose){
        obstacles.add(obs)
    }

    fun distanceToAllObstacles(pos : Vector2) : Double{
        var dist = 0.0
        for (obs in obstacles){
            dist += pos.distanceToVector(obs.toVector())
        }
        return dist
    }


    fun optimizeSingleVector(vector : Vector2, base : Vector2, previous : Vector2) : Vector2{
        val deltaControl = Vector2(vector)
        deltaControl.subtract(previous)
        val offset = Vector2(base)
        offset.add(vector)
        offset.subtract(end.toVector())
        val cost = (Q * offset.magnitude()) + (R * deltaControl.magnitude()) + (O * distanceToAllObstacles(base.compound(vector)))//compute cost for given action

        val dt = (System.currentTimeMillis() - previous_timestep) / 1000 //calculate dt for accel calculations and constraints
        previous_timestep = System.currentTimeMillis()

        val step = solveRThetaCostGradient(vector, base, cost)

        var newVelo = Range.clip(vector.magnitude() + step.x, 0.0, MAX_VELOCITY)
        val accel = Range.clip((newVelo - vector.magnitude()) / (if (dt.equals(0.0)) 1 else dt)  , 0.0, MAX_ACCEL)
        newVelo = Range.clip(vector.magnitude() + (accel * dt), 0.0, MAX_VELOCITY)
        val newTheta = (vector.angle() + step.y) % Math.toRadians(360.0) //compute new vector

        return Vector2(newVelo * cos(newTheta), newVelo * sin(newTheta))
    }

    private fun solveThetaCostGradient(vector : Vector2, base : Vector2, cost : Double) : Double{
        val gradient_vector_1 = Vector2(vector.magnitude() * cos(vector.angle() + Math.toRadians(10.0)), vector.magnitude() * sin(vector.angle() + Math.toRadians(10.0)))
        val gradient_vector_2 = Vector2(vector.magnitude() * cos(vector.angle() - Math.toRadians(10.0)), vector.magnitude() * sin(vector.angle() - Math.toRadians(10.0)))
        val gradient_vector_cost_1 = (Q * gradient_vector_1.compound(base).distanceToVector(end.toVector()).pow(2)) + (R * gradient_vector_1.distanceToVector(previous).pow(2)) + (O * distanceToAllObstacles(gradient_vector_1.compound(base)).pow(2)) + (r2 * gradient_vector_1.magnitude().pow(2))
        val gradient_vector_cost_2 = (Q * gradient_vector_2.compound(base).distanceToVector(end.toVector()).pow(2)) + (R * gradient_vector_2.distanceToVector(previous).pow(2)) + (O * distanceToAllObstacles(gradient_vector_2.compound(base)).pow(2)) + (r2 * gradient_vector_2.magnitude().pow(2))
        val gradient_cost = ((gradient_vector_cost_2 - cost) - (gradient_vector_cost_1 - cost)) //gradient at point in rTheta state space
        val step = (THETA_GAIN * gradient_cost) + (STOCHASTIC_SCALE * Random.nextDouble())
        return step
    }

    private fun solveVeloCostGradient(vector : Vector2, base : Vector2, cost : Double) : Double{
        val velo_gradient_vector_1 = Vector2((vector.magnitude() + 1) * cos(vector.angle()), (vector.magnitude() + 1) * sin(vector.angle()))
        val velo_gradient_vector_2 = Vector2((vector.magnitude() - 1) * cos(vector.angle()), (vector.magnitude() - 1) * sin(vector.angle()))
        val velo_gradient_vector_cost_1 = (Q * velo_gradient_vector_1.compound(base).distanceToVector(end.toVector()).pow(2)) + (R * velo_gradient_vector_1.distanceToVector(previous).pow(2)) + (O * distanceToAllObstacles(velo_gradient_vector_1.compound(base)).pow(2)) + (r2 * velo_gradient_vector_1.magnitude().pow(2))
        val velo_gradient_vector_cost_2 = (Q * velo_gradient_vector_2.compound(base).distanceToVector(end.toVector()).pow(2)) + (R * velo_gradient_vector_2.distanceToVector(previous).pow(2)) + (O * distanceToAllObstacles(velo_gradient_vector_2.compound(base)).pow(2)) + (r2 * velo_gradient_vector_2.magnitude().pow(2))
        val velo_gradient_cost = ((velo_gradient_vector_cost_2 - cost) - (velo_gradient_vector_cost_1 - cost)) //compute velo gradient at point in rTheta state space
        val velo_step = (VELO_GAIN * velo_gradient_cost) + (STOCHASTIC_SCALE * Random.nextDouble())
        return velo_step
    }

    private fun solveRThetaCostGradient(vector : Vector2, base : Vector2, cost : Double) : Vector2{ //NOTE: this returns the step vector in the rTheta space, not x-y, so conversions are needed to convert to x-y if necessary
        return Vector2(solveVeloCostGradient(vector, base, cost), solveThetaCostGradient(vector, base, cost))
    }

    fun solveOptimalControlSequence(pos : Pose) = runBlocking{
        repeat(CONTROL_HORIZON){i ->
            do{
                val base = pos.toVector()
                var cost = 0.0
                for (j in 0 until i){
                    val addition = optimal_control_sequence[j]
                    addition.scalarMultiply(TIMESTEP)
                    cost += (R * addition.distanceToVector(base).pow(2)) + (r2 * addition.magnitude().pow(2))
                    base.add(addition)
                    cost += getCost(addition)
                }
                val action = optimizeSingleVector(optimal_control_sequence[i], base, if (i == 0) previous else optimal_control_sequence[i - 1])
                optimal_control_sequence[i] = action
                total_iterations++
            } while ((active && total_iterations < BAILOUT) && abs(cost) < EPSILON)
        }
    }

    fun start(){
        active = true
        reset()
    }

    fun reset(){
        total_iterations = 0
        optimal_control_sequence.clear()
        for (i in 1..CONTROL_HORIZON){
            optimal_control_sequence.add(Vector2())
        }
    }

    fun update(start : Pose, end : Pose){
        pause()
        total_iterations = 0
        this.start = start
        this.end = end
        optimal_control_sequence.removeAt(0)
        optimal_control_sequence.add(Vector2(0.0, 0.0))
        resume()
    }

    fun update(start : Pose){
        update(start,  this.end)
    }

    fun update(){
        val v = start.toVector()
        v.add(previous)
        v.scalarMultiply(TIMESTEP) //estimate the next position given the command velocity and angle bc we don't really have much else to work with
        update(Pose(v.x, v.y, start.angle), end)
    }

    fun pause(){
        active = false
    }

    fun resume(){
        active = true
    }

    fun stop(){
        active = false
        reset()
    }

    fun getOptimalControlAction() : Vector2{ //assumes the action taken is immediately executed
        val response = optimal_control_sequence[0]
        previous = response
        update()
        //removes the first executed node of the control sequence, pushing the control sequence up one index
        //this supplies the next stage of the GD a starting point in the GD optimization
        return response
    }

    fun getCost(pos : Vector2) : Double{
        return (Q * pos.distanceToVector(end.toVector()).pow(2)) + (O * distanceToAllObstacles(pos).pow(2))
    }

    fun getConfidence(pos : Pose) : Double{
        var cost = 0.0
        val curr_pos = pos.toVector()
        for (action in optimal_control_sequence){
            action.scalarMultiply(TIMESTEP)
            curr_pos.add(action)
            cost += getCost(curr_pos) + (R * previous.distanceToVector(action).pow(2)) + (r2 * action.magnitude().pow(2))
        }
        return cost
    }
}
