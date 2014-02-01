package jboxGlue.Link;

import jboxGlue.PhysicalSolidObject;
import springies.EnvironmentVariables;
import jgame.JGColor;


/**
 * Muscle Class extends Spring
 * a kind of Spring whose restLength
 * varies according to a sin wave
 * generated by MuscleWave
 */
public class Muscle extends Spring {
    private double originalLength;
    private double myPhase = 0;
    private double myAmplitude = 0;

    /**
     * @param amplitude : the amplitude of its sin wave
     *        phase can also be assigned in future extension
     */
    public Muscle (String id,
                   int collisionId,
                   JGColor color,
                   PhysicalSolidObject object1,
                   PhysicalSolidObject object2,
                   double length,
                   double stiffness,
                   double amplitude) {

        super(id, collisionId, color, object1, object2, length, stiffness);
        originalLength = length;
        myAmplitude = amplitude;
    }
    @Override
    /**
     * override setSpringLength class in spring class
     * set the restLength according to the
     * sin wave generated by MuscleWave
     * in EnvironmentVariables (cause every muscle shares ne)
     */
    
    protected void setSpringLength () {
        myRestLength =
                originalLength * (EnvironmentVariables.getMuscleWave(myAmplitude, myPhase) + 1);

    }

}
