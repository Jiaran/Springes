package jboxGlue.Factory;

import java.util.HashMap;
import jboxGlue.Assembly;
import jboxGlue.PhysicalSolidObject;
import jboxGlue.Link.Spring;
import jgame.JGColor;


/**
 * SpringFactory can read stiffness,restLength, objects it
 * connects to create a Spring.
 * subclass can add other reader to read more information to
 * create more special Spring( for example, Muscle read amplitude);
 * 
 */
public class SpringFactory extends Factory {
    private final String RESTLENGTH = "restlength";
    private final String STIFFNESS = "constant";
    private final String OBJECT2 = "b";
    private final String OBJECT1 = "a";
    private final double DEFAULT_VALUE = 1.0;
    protected PhysicalSolidObject object1;
    protected PhysicalSolidObject object2;
    protected double stiffness;
    protected double restLength;
    protected Assembly assembly;

    @Override
    protected double StringtoDouble (String str) {
        if (str == null)
            return DEFAULT_VALUE;
        if (str.equals(""))
            return DEFAULT_VALUE; // 1.0 is the default value for mass related parameter
        else return Double.parseDouble(str);
    }

    @Override
    public void create (HashMap<String, String> information, Assembly assembly) {
        if (assembly == null) {
            return;
        }
        else {
            this.assembly = assembly;
        }

        if (readInformation(information)) {
            addObject();

        }

    }

    protected boolean readInformation (HashMap<String, String> information) {
        String obeject1Name = information.get(OBJECT1);
        String objecct2Name = information.get(OBJECT2);
        object1 = assembly.getObject(obeject1Name);
        object2 = assembly.getObject(objecct2Name);
        stiffness = StringtoDouble(information.get(STIFFNESS));
        restLength = StringtoDouble(information.get(RESTLENGTH));
        return object1 != null && object2 != null;

    }

    protected void addObject () {
        Spring spring=new Spring(null, 0, JGColor.blue, object1,
                   object2, restLength, stiffness);
        assembly.addSpring(spring);
    }
}
