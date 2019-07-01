package definitely-not-MPC.Gradient_Descent

import com.qualcomm.robotcore.util.Range
import kotlinx.coroutines.*
import definitely-not-MPC.Modified_Math.Pose
import definitely-not-MPC.Modified_Math.Vector2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class MPC_Pathfinder(start : Pose, end : Pose, obstacles : ArrayList<Pose>) {
    var start = start
    var end = end
    var obstacles = obstacles

    val CONTROL_HORIZON = 5
    val TIMESTEP = 0.1 //tune this to the average loop time

    val Q = 1
    val R = 0.01
    val O = 0.01

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

        val theta_gradient_vector_1 = Vector2(vector.magnitude() * cos(vector.angle() + Math.toRadians(10.0)), vector.magnitude() * sin(vector.angle() + Math.toRadians(10.0)))
        val theta_gradient_vector_2 = Vector2(vector.magnitude() * cos(vector.angle() - Math.toRadians(10.0)), vector.magnitude() * sin(vector.angle() - Math.toRadians(10.0)))
        val theta_gradient_vector_cost_1 = (Q * theta_gradient_vector_1.compound(base).distanceToVector(end.toVector())) + (R * theta_gradient_vector_1.distanceToVector(previous)) + (O * distanceToAllObstacles(theta_gradient_vector_1.compound(base)))
        val theta_gradient_vector_cost_2 = (Q * theta_gradient_vector_2.compound(base).distanceToVector(end.toVector())) + (R * theta_gradient_vector_2.distanceToVector(previous)) + (O * distanceToAllObstacles(theta_gradient_vector_2.compound(base)))
        val theta_gradient_cost = ((theta_gradient_vector_cost_2 - cost) - (theta_gradient_vector_cost_1 - cost)) / 2 //compute theta gradient at point in rTheta state space
        val theta_step = (THETA_GAIN * theta_gradient_cost) + (STOCHASTIC_SCALE * Random.nextDouble())

        val velo_gradient_vector_1 = Vector2((vector.magnitude() + 1) * cos(vector.angle()), (vector.magnitude() + 1) * sin(vector.angle()))
        val velo_gradient_vector_2 = Vector2((vector.magnitude() - 1) * cos(vector.angle()), (vector.magnitude() - 1) * sin(vector.angle()))
        val velo_gradient_vector_cost_1 = (Q * velo_gradient_vector_1.compound(base).distanceToVector(end.toVector())) + (R * velo_gradient_vector_1.distanceToVector(previous)) + (O * distanceToAllObstacles(velo_gradient_vector_1.compound(base)))
        val velo_gradient_vector_cost_2 = (Q * velo_gradient_vector_2.compound(base).distanceToVector(end.toVector())) + (R * velo_gradient_vector_2.distanceToVector(previous)) + (O * distanceToAllObstacles(velo_gradient_vector_2.compound(base)))
        val velo_gradient_cost = ((velo_gradient_vector_cost_2 - cost) - (velo_gradient_vector_cost_1 - cost)) / 2 //compute velo gradient at point in rTheta state space
        val velo_step = (VELO_GAIN * velo_gradient_cost) + (STOCHASTIC_SCALE * Random.nextDouble())

        var newVelo = Range.clip(vector.magnitude() + velo_step, 0.0, MAX_VELOCITY)
        val accel = Range.clip((newVelo - vector.magnitude()) / (if (dt.equals(0.0)) 1 else dt)  , 0.0, MAX_ACCEL)
        newVelo = Range.clip(vector.magnitude() + (accel * dt), 0.0, MAX_VELOCITY)
        val newTheta = (vector.angle() + theta_step) % Math.toRadians(360.0) //compute new vector

        return Vector2(newVelo * cos(newTheta), newVelo * sin(newTheta))
    }

    fun solveOptimalControlSequence(pos : Pose) = runBlocking{
        repeat(CONTROL_HORIZON){i ->
            while (active && total_iterations < BAILOUT){
                val base = pos.toVector()
                for (j in 0 until i){
                    var addition = optimal_control_sequence[j]
                    addition.scalarMultiply(TIMESTEP)
                    base.add(addition)
                }
                val action = optimizeSingleVector(optimal_control_sequence[i], base, if (i == 0) previous else optimal_control_sequence[i - 1])
                optimal_control_sequence[i] = action
                total_iterations++
            }
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

    fun stop(){
        active = false
        reset()
    }

    fun getOptimalControlAction() : Vector2{ //assumes the action taken is immediately executed
        val response = optimal_control_sequence[0]
        previous = response
        reset()
        return response
    }

    fun getCost(pos : Vector2) : Double{
        return (Q * pos.distanceToVector(end.toVector())) + (O * distanceToAllObstacles(pos))
    }

    fun getConfidence(pos : Pose) : Double{
        var cost : Double = 0.0
        var curr_pos = pos.toVector()
        var previous = optimal_control_sequence[0]
        for (action in optimal_control_sequence){
            curr_pos.add(action)
            cost += getCost(curr_pos) + (R * previous.distanceToVector(action))
        }
        return cost
    }
}