package VF.Feilds;

import Universal.Math.Pose;
import Universal.Math.Vector2;
import VF.Boundry;
import VF.Objects.FieldWall;
import VF.Objects.RoverRuckus.Crater;
import VF.Objects.RoverRuckus.Lander;
import VF.Objects.RoverRuckus.SilverSample;
import VF.VectorField;
import VF.VectorFieldComponent;

import javax.swing.text.Position;
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

        Lander lander = new Lander(new Pose(0, 0, Math.PI/4));

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

        ArrayList<VectorFieldComponent> obsticals = new ArrayList<>();

        obsticals.add(lander);
        obsticals.add(crater1);
        obsticals.add(crater2);

        obsticals.add(centerSampleq1);
        obsticals.add(centerSampleq2);
        obsticals.add(centerSampleq3);
        obsticals.add(centerSampleq4);

        obsticals.add(leftSampleq1);
        obsticals.add(leftSampleq2);
        obsticals.add(leftSampleq3);
        obsticals.add(leftSampleq4);

        obsticals.add(rightSampleq1);
        obsticals.add(rightSampleq2);
        obsticals.add(rightSampleq3);
        obsticals.add(rightSampleq4);

        ArrayList<Boundry> boundries = new ArrayList<>();

        boundries.add(wall);

    }

    public void sampleLeft(){
        sample  = Sample.LEFT;

        obsticals.get(LEFT_SAMPLE_Q1_INDEX).deactivate();
        obsticals.get(LEFT_SAMPLE_Q2_INDEX).deactivate();
        obsticals.get(LEFT_SAMPLE_Q3_INDEX).deactivate();
        obsticals.get(LEFT_SAMPLE_Q4_INDEX).deactivate();

        obsticals.get(CENTER_SAMPLE_Q1_INDEX).activate();
        obsticals.get(CENTER_SAMPLE_Q2_INDEX).activate();
        obsticals.get(CENTER_SAMPLE_Q3_INDEX).activate();
        obsticals.get(CENTER_SAMPLE_Q4_INDEX).activate();

        obsticals.get(RIGHT_SAMPLE_Q1_INDEX).activate();
        obsticals.get(RIGHT_SAMPLE_Q2_INDEX).activate();
        obsticals.get(RIGHT_SAMPLE_Q3_INDEX).activate();
        obsticals.get(RIGHT_SAMPLE_Q4_INDEX).activate();

        hasChangedBarriers = true;
    }

    public void sampleCenter(){
        sample  = Sample.CENTER;

        obsticals.get(LEFT_SAMPLE_Q1_INDEX).activate();
        obsticals.get(LEFT_SAMPLE_Q2_INDEX).activate();
        obsticals.get(LEFT_SAMPLE_Q3_INDEX).activate();
        obsticals.get(LEFT_SAMPLE_Q4_INDEX).activate();

        obsticals.get(CENTER_SAMPLE_Q1_INDEX).deactivate();
        obsticals.get(CENTER_SAMPLE_Q2_INDEX).deactivate();
        obsticals.get(CENTER_SAMPLE_Q3_INDEX).deactivate();
        obsticals.get(CENTER_SAMPLE_Q4_INDEX).deactivate();

        obsticals.get(RIGHT_SAMPLE_Q1_INDEX).activate();
        obsticals.get(RIGHT_SAMPLE_Q2_INDEX).activate();
        obsticals.get(RIGHT_SAMPLE_Q3_INDEX).activate();
        obsticals.get(RIGHT_SAMPLE_Q4_INDEX).activate();

        hasChangedBarriers = true;
    }

    public void sampleRight(){
        sample  = Sample.RIGHT;

        obsticals.get(LEFT_SAMPLE_Q1_INDEX).activate();
        obsticals.get(LEFT_SAMPLE_Q2_INDEX).activate();
        obsticals.get(LEFT_SAMPLE_Q3_INDEX).activate();
        obsticals.get(LEFT_SAMPLE_Q4_INDEX).activate();

        obsticals.get(CENTER_SAMPLE_Q1_INDEX).activate();
        obsticals.get(CENTER_SAMPLE_Q2_INDEX).activate();
        obsticals.get(CENTER_SAMPLE_Q3_INDEX).activate();
        obsticals.get(CENTER_SAMPLE_Q4_INDEX).activate();

        obsticals.get(RIGHT_SAMPLE_Q1_INDEX).deactivate();
        obsticals.get(RIGHT_SAMPLE_Q2_INDEX).deactivate();
        obsticals.get(RIGHT_SAMPLE_Q3_INDEX).deactivate();
        obsticals.get(RIGHT_SAMPLE_Q4_INDEX).deactivate();

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
