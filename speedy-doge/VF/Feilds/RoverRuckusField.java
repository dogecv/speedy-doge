package VF.Feilds;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.Objects.FieldWall;
import VF.Objects.RoverRuckus.Crater;
import VF.Objects.RoverRuckus.Lander;
import VF.Objects.RoverRuckus.SilverSample;
import VF.VectorField;

import java.util.ArrayList;

public class RoverRuckusField extends VectorField {
    private final int LANDER_INDEX = 14,
            CRATER_1_INDEX = 13,
            CRATER_2_INDEX = 12,
            CENTER_SAMPLE_Q1_INDEX = 11,
            CENTER_SAMPLE_Q2_INDEX = 10,
            CENTER_SAMPLE_Q3_INDEX = 9,
            CENTER_SAMPLE_Q4_INDEX = 8,
            LEFT_SAMPLE_Q1_INDEX = 7,
            LEFT_SAMPLE_Q2_INDEX = 6,
            LEFT_SAMPLE_Q3_INDEX = 5,
            LEFT_SAMPLE_Q4_INDEX = 4,
            RIGHT_SAMPLE_Q1_INDEX = 3,
            RIGHT_SAMPLE_Q2_INDEX = 2,
            RIGHT_SAMPLE_Q3_INDEX = 1,
            RIGHT_SAMPLE_Q4_INDEX = 0;

    public enum Sample {
        LEFT,
        CENTER,
        RIGHT
    }

    private boolean hasChangedBarriers = false;

    public Sample sample = Sample.CENTER;

    public RoverRuckusField(){
        super(new ArrayList<>(), new ArrayList<>());
        FieldWall wall= new FieldWall();

        Lander lander = new Lander(new Pose(0, 0, 0));

        Crater crater1 = new Crater(new Pose(-72, 72, 0));
        Crater crater2 = new Crater(new Pose(72, -72, 0));

        SilverSample centerSampleq1 = new SilverSample(new Pose(36,36,0));
        SilverSample centerSampleq2 = new SilverSample(new Pose(-36,36,0));
        SilverSample centerSampleq3 = new SilverSample(new Pose(-36,-36,0));
        SilverSample centerSampleq4 = new SilverSample(new Pose(36,-36,0));

        SilverSample leftSampleq1 = new SilverSample(new Pose(25,47,0));
        SilverSample leftSampleq2 = new SilverSample(new Pose(-47,25,0));
        SilverSample leftSampleq3 = new SilverSample(new Pose(-25,-47,0));
        SilverSample leftSampleq4 = new SilverSample(new Pose(47,-25,0));

        SilverSample rightSampleq1 = new SilverSample(new Pose(47,25,0));
        SilverSample rightSampleq2 = new SilverSample(new Pose(-25,47,0));
        SilverSample rightSampleq3 = new SilverSample(new Pose(-47,-25,0));
        SilverSample rightSampleq4 = new SilverSample(new Pose(25,-47,0));

        obstacles.add(lander);

        obstacles.add(crater1);
        obstacles.add(crater2);

        obstacles.add(centerSampleq1);
        obstacles.add(centerSampleq2);
        obstacles.add(centerSampleq3);
        obstacles.add(centerSampleq4);

        obstacles.add(leftSampleq1);
        obstacles.add(leftSampleq2);
        obstacles.add(leftSampleq3);
        obstacles.add(leftSampleq4);

        obstacles.add(rightSampleq1);
        obstacles.add(rightSampleq2);
        obstacles.add(rightSampleq3);
        obstacles.add(rightSampleq4);


        boundaries.add(wall);

    }

    public void sampleLeft(){
        sample  = Sample.LEFT;

        obstacles.get(LEFT_SAMPLE_Q1_INDEX).deactivate();
        obstacles.get(LEFT_SAMPLE_Q2_INDEX).deactivate();
        obstacles.get(LEFT_SAMPLE_Q3_INDEX).deactivate();
        obstacles.get(LEFT_SAMPLE_Q4_INDEX).deactivate();

        obstacles.get(CENTER_SAMPLE_Q1_INDEX).activate();
        obstacles.get(CENTER_SAMPLE_Q2_INDEX).activate();
        obstacles.get(CENTER_SAMPLE_Q3_INDEX).activate();
        obstacles.get(CENTER_SAMPLE_Q4_INDEX).activate();

        obstacles.get(RIGHT_SAMPLE_Q1_INDEX).activate();
        obstacles.get(RIGHT_SAMPLE_Q2_INDEX).activate();
        obstacles.get(RIGHT_SAMPLE_Q3_INDEX).activate();
        obstacles.get(RIGHT_SAMPLE_Q4_INDEX).activate();

        hasChangedBarriers = true;
    }

    public void sampleCenter(){
        sample  = Sample.CENTER;

        obstacles.get(LEFT_SAMPLE_Q1_INDEX).activate();
        obstacles.get(LEFT_SAMPLE_Q2_INDEX).activate();
        obstacles.get(LEFT_SAMPLE_Q3_INDEX).activate();
        obstacles.get(LEFT_SAMPLE_Q4_INDEX).activate();

        obstacles.get(CENTER_SAMPLE_Q1_INDEX).deactivate();
        obstacles.get(CENTER_SAMPLE_Q2_INDEX).deactivate();
        obstacles.get(CENTER_SAMPLE_Q3_INDEX).deactivate();
        obstacles.get(CENTER_SAMPLE_Q4_INDEX).deactivate();

        obstacles.get(RIGHT_SAMPLE_Q1_INDEX).activate();
        obstacles.get(RIGHT_SAMPLE_Q2_INDEX).activate();
        obstacles.get(RIGHT_SAMPLE_Q3_INDEX).activate();
        obstacles.get(RIGHT_SAMPLE_Q4_INDEX).activate();

        hasChangedBarriers = true;
    }

    public void sampleRight(){
        sample  = Sample.RIGHT;

        obstacles.get(LEFT_SAMPLE_Q1_INDEX).activate();
        obstacles.get(LEFT_SAMPLE_Q2_INDEX).activate();
        obstacles.get(LEFT_SAMPLE_Q3_INDEX).activate();
        obstacles.get(LEFT_SAMPLE_Q4_INDEX).activate();

        obstacles.get(CENTER_SAMPLE_Q1_INDEX).activate();
        obstacles.get(CENTER_SAMPLE_Q2_INDEX).activate();
        obstacles.get(CENTER_SAMPLE_Q3_INDEX).activate();
        obstacles.get(CENTER_SAMPLE_Q4_INDEX).activate();

        obstacles.get(RIGHT_SAMPLE_Q1_INDEX).deactivate();
        obstacles.get(RIGHT_SAMPLE_Q2_INDEX).deactivate();
        obstacles.get(RIGHT_SAMPLE_Q3_INDEX).deactivate();
        obstacles.get(RIGHT_SAMPLE_Q4_INDEX).deactivate();

        hasChangedBarriers = true;
    }

    @Override
    public Vector2 getVector(Pose pose){
        if(hasChangedBarriers)
            calculateBarriers();
        hasChangedBarriers = false;
        return super.getVector(pose);
    }
}
