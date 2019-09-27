package VF.Feilds;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.Objects.FieldWall;
import VF.Objects.Skystone.Foundation;
import VF.Objects.Skystone.Stone;
import VF.VectorField;
import VF.VectorShapes.VectorRectangle;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SkystoneField extends VectorField {

    FieldWall fieldWall = new FieldWall();

    public Foundation redFoundation = new Foundation(new Pose(14.334316434,-47.526015563));
    public Foundation blueFoundation = new Foundation(new Pose(-14.334316434,-47.526015563));

    public Stone redStone1 = new Stone(new Pose(-21.357417959,22.626015563+4));
    public Stone redStone2 = new Stone(new Pose(-21.357417959,22.626015563+4*3));
    public Stone redStone3 = new Stone(new Pose(-21.357417959,22.626015563+4*5));
    public Stone redStone4 = new Stone(new Pose(-21.357417959,22.626015563+4*7));
    public Stone redStone5 = new Stone(new Pose(-21.357417959,22.626015563+4*9));
    public Stone redStone6 = new Stone(new Pose(-21.357417959,22.626015563+4*11));

    public Stone blueStone1 = new Stone(new Pose(21.357417959,22.626015563+4));
    public Stone blueStone2 = new Stone(new Pose(21.357417959,22.626015563+4*3));
    public Stone blueStone3 = new Stone(new Pose(21.357417959,22.626015563+4*5));
    public Stone blueStone4 = new Stone(new Pose(21.357417959,22.626015563+4*7));
    public Stone blueStone5 = new Stone(new Pose(21.357417959,22.626015563+4*9));
    public Stone blueStone6 = new Stone(new Pose(21.357417959,22.626015563+4*11));

    public VectorRectangle neutralSkybridge = new VectorRectangle(new Pose(), 48, 18, 24, 0.01);

    public ArrayList<Stone> redStones = new ArrayList<>(), blueStones = new ArrayList<>();


    public SkystoneField(){
        super(new ArrayList<>(), new ArrayList<>());

        obstacles.add(blueStone1);
        obstacles.add(blueStone2);
        obstacles.add(blueStone3);
        obstacles.add(blueStone4);
        obstacles.add(blueStone5);
        obstacles.add(blueStone6);

        obstacles.add(redStone1);
        obstacles.add(redStone2);
        obstacles.add(redStone3);
        obstacles.add(redStone4);
        obstacles.add(redStone5);
        obstacles.add(redStone6);

        obstacles.add(neutralSkybridge);

        boundaries.add(fieldWall);

        redStones.add(0, redStone6);
        redStones.add(0, redStone5);
        redStones.add(0, redStone4);
        redStones.add(0, redStone3);
        redStones.add(0, redStone2);
        redStones.add(0, redStone1);

        blueStones.add(0, blueStone6);
        blueStones.add(0, blueStone5);
        blueStones.add(0, blueStone4);
        blueStones.add(0, blueStone3);
        blueStones.add(0, blueStone2);
        blueStones.add(0, blueStone1);

    }

    public boolean inBuildingZone(Pose position){
        return position.y < 0;
    }

    public boolean inLoadingZone(Pose position){
        return !inBuildingZone(position);
    }

    public void setSkystones(int stoneIndex){
        while(stoneIndex > 3)
            stoneIndex-=3;
        for(int i = 1; i <= 3; i++) {
            if(i==stoneIndex){
                blueStones.get(i-1).skystone = true;
                blueStones.get(i+2).skystone = true;
                redStones.get(i-1).skystone = true;
                redStones.get(i+2).skystone = true;
            }
            else{
                blueStones.get(i-1).skystone = false;
                blueStones.get(i+2).skystone = false;
                redStones.get(i-1).skystone = false;
                redStones.get(i+2).skystone = false;
            }
        }
    }
    public int getSkystoneIndex(){
        for(int i = 0; i < 3; i++){
            if(blueStones.get(i).skystone)
                return i+1;
        }
        return 0;
    }

    public Pose getClosestStoneLocation(Pose startingPose){
        Pose output = new Pose(1000, 1000);
        if(startingPose.x > 0){
            for(Stone stone : blueStones){
                Vector2 temp = stone.shape.getClosestPoint(startingPose.toVector());
                if(temp.magnitude() < output.magnitude())
                    output = new Pose(temp.x, temp.y);
            }
        }
        else{
            for(Stone stone : redStones){
                Vector2 temp = stone.shape.getClosestPoint(startingPose.toVector());
                if(temp.magnitude() < output.magnitude())
                    output = new Pose(temp.x, temp.y);
            }
        }
        return output;
    }

    public Pose getClosestSkystoneLocation(Pose startingPose) {
        Pose output = new Pose(1000, 1000);
        if(startingPose.x > 0){
            for(Stone stone : blueStones){
                Vector2 temp = stone.shape.getClosestPoint(startingPose.toVector());
                if(temp.magnitude() < output.magnitude() && stone.skystone)
                    output = new Pose(temp.x, temp.y);
            }
        }
        else{
            for(Stone stone : redStones){
                Vector2 temp = stone.shape.getClosestPoint(startingPose.toVector());
                if(temp.magnitude() < output.magnitude() && stone.skystone)
                    output = new Pose(temp.x, temp.y);
            }
        }
        return output;
    }

    @Override public void calculateBarriers(){}


}
