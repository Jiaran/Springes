package jboxGlue.Factory;

import java.util.HashMap;
import jboxGlue.Assembly;
import jboxGlue.Node.FixedMass;
import jgame.JGColor;

/**
 * @author Jiaran
 * FixedMassFactory. Can be base class for massFactory.
 * For massFactory, just override readInformation, appending
 * its special parameters after FixMassFactory's readInformation 
 * method. Because what FixedMass read is universal for all kinds
 * of mass.
 *
 */
public class FixedMassFactory extends Factory {
    private final String MASS_Y = "y";
    private final String MASS_X = "x";
    private final String MASS_ID = "id";
    private final double DEFAULT_VALUE = 1.0;
    protected Assembly assembly;
    protected String name;
    protected double initialX;
    protected double initialY;
    
    public void create(HashMap<String, String> information, Assembly assembly){
        if(assembly ==null){
            return;
        }
        else{
            this.assembly = assembly;
        }
        readInformation(information);
        addObjectInAssembly();
        
    }
    @Override
    protected double StringtoDouble (String str) {
        if (str == null)
            return DEFAULT_VALUE;
        if (str.equals(""))
            return DEFAULT_VALUE; // 1.0 is the default value for mass related parameter
        else return Double.parseDouble(str);
    }
    protected boolean readInformation (HashMap<String, String> information) {
        
        name = information.get(MASS_ID);
        initialX = StringtoDouble(information.get(MASS_X));
        initialY = StringtoDouble(information.get(MASS_Y));
        return true;
    }
    
    protected void addObjectInAssembly(){
        FixedMass fixedMass=new FixedMass(name, 2, JGColor.yellow, initialX, initialY,assembly);
        this.assembly.addObject(name, fixedMass);
    }
}
