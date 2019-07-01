package definitely-not-MPC.Gradient_Descent

import definitely-not-MPC.Modified_Math.Pose
import definitely-not-MPC.Modified_Math.Vector2

class MPC_Handler(i_pathfinder : MPC_Pathfinder) : Runnable{
    var pathfinder = i_pathfinder
    var pos = Pose()

    override fun run(){
        pathfinder.start()
        pathfinder.solveOptimalControlSequence(pos)
    }

    fun start(){
        run()
    }

    fun stop(){
        pathfinder.stop()
    }

    fun reset(){
        pathfinder.reset()
    }

    fun getOptimalControlAction() : Vector2 {
        return pathfinder.getOptimalControlAction()
    }

    fun getConfidence() : Double{
        return pathfinder.getConfidence()
    }

    fun addObstacle(obs : Pose){
        pathfinder.addObstacle(obs)
    }

    fun init(start : Pose, end : Pose){
        pathfinder.init(start, end)
    }
}