package jboxGlue.Factory;

import java.util.HashMap;
import jboxGlue.Link.Muscle;
import jgame.JGColor;


/**
 * Factory for Muscle. Utilize the superclass but add a new reader
 * cause to create Muscle we need a new parameter: amplitude
 * 
 */
public class MuscleFactory extends SpringFactory {

    private final String AMPLITUDE = "amplitude";
    private double amplitude;
    
    protected boolean readInformation (HashMap<String, String> information) {
        if(super.readInformation(information)){
            amplitude = StringtoDouble(information.get(AMPLITUDE));
            return true;
        }
        else return false;
    }
    protected void addObject () {
        Muscle muscle=new Muscle(null, 0, JGColor.blue, object1,
                   object2, restLength, stiffness, amplitude);
        assembly.addSpring(muscle);

    }
}
