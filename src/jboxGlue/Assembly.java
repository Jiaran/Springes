package jboxGlue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jboxGlue.Link.Spring;
import org.jbox2d.common.Vec2;


/**
 * When an Xml file is read, it creates a Assembly. Every mass in one creation
 * is put in one Assembly so that they can share the same center of mass.
 * The structure of our masses is : the world -----> a list of assembly
 * assembly -----> map mass name to the object itself.
 * This structure allows us to create and manage and delete objects easily.
 */
public class Assembly {
    private Map<String, PhysicalSolidObject> allSolidObjects =
            new HashMap<String, PhysicalSolidObject>();
    private ArrayList<Spring> spring= new ArrayList<Spring>();

    /**
     * Constructor
     */
    public Assembly () {
        
    }

    /**
     * @return return the an object according to its ID
     */
    public PhysicalSolidObject getObject (String id) {
        return allSolidObjects.get(id);
    }

    /**
     * @return allSolidObjects Map
     */
    public Map<String, PhysicalSolidObject> getAllSolidObjects () {
        return allSolidObjects;
    }

    /**
     * @param name : name of the objects
     * @param object: the object
     */
    public void addObject (String name, PhysicalSolidObject object) {
        allSolidObjects.put(name, object);
    }

    /**
     * return the center of mass.
     * 
     * @return a vec2(center x, center y) used by a mass to calculate center-toward forces.
     */
    public Vec2 getCenterOfMass () {
        double sumMassX = 0;
        double sumMassY = 0;
        double totalMass = 0;
        Iterator<Map.Entry<String, PhysicalSolidObject>> it = allSolidObjects.entrySet().iterator();
        while (it.hasNext()) {
            PhysicalSolidObject solidObject = it.next().getValue();
            double mass = solidObject.getBody().getMass();
            totalMass += mass;
            sumMassX += solidObject.getX() * mass;
            sumMassY += solidObject.getY() * mass;

        }
        if (totalMass == 0)
            return new Vec2(0, 0); 
        double centerMassXPos = sumMassX / totalMass;
        double centerMassYPos = sumMassY / totalMass;
        return new Vec2((float) centerMassXPos, (float) centerMassYPos);

    }

    /**
     * clear the assembly.
     */
    public void clear () {
        Iterator<Map.Entry<String, PhysicalSolidObject>> it = allSolidObjects.entrySet().iterator();
        while (it.hasNext()) {
            PhysicalSolidObject solidObject = it.next().getValue();
            solidObject.remove();
            it.remove();

        }
    }

    public void addSpring( Spring s){
        spring.add(s);
    }
    public Spring getSpring( int i){
        if( i >=spring.size())
            return null;
        return spring.get(i);
    }
}
