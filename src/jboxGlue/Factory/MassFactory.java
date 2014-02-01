package jboxGlue.Factory;

import java.util.HashMap;
import jboxGlue.Node.Mass;
import jgame.JGColor;


/**
 * Factory for Mass
 * 
 */
public class MassFactory extends FixedMassFactory {
    private final String MASS_MASS = "mass";
    private final String VELOCITY_Y = "vy";
    private final String VELOCITY_X = "vx";
    private double vx;
    private double vy;
    private double mass;
  

    protected void addObjectInAssembly(){
        Mass newMass =
                new Mass(name, 2, JGColor.yellow, mass, initialX, initialY, (float) vx, (float) vy,
                         this.assembly);
        this.assembly.addObject(name, newMass);
    }
    
    protected boolean readInformation (HashMap<String, String> information) {
        super.readInformation(information);
        vx = StringtoDouble(information.get(VELOCITY_X));
        vy = StringtoDouble(information.get(VELOCITY_Y));
        mass = StringtoDouble(information.get(MASS_MASS));
        return true;
    }
    
    

}
