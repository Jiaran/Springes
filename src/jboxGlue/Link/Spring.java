package jboxGlue.Link;

import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalSolidObject;
import jboxGlue.Node.Mass;
import jgame.JGColor;


/**
 * Spring class extends PhysicalObject:
 * It is a spring subject to Hook's Law
 * Apply forces to the masses it links.
 * 
 */
public class Spring extends PhysicalObject {
    protected double myRestLength;
    protected PhysicalSolidObject myObject1;
    protected PhysicalSolidObject myObject2;
    private final double myStifness;
    private double lastFx = 0;   // only for the cases where two masses overlap( in no collision mode)
    private double lastFy = 0;

    /**
     * @param id : name
     * 
     * @param collisionId : id for checkcollision
     * 
     * @param color : in which color you want to draw it
     * 
     * @param object1, object2: nodes it links
     * 
     * @param length: rest length for a spring
     * 
     * @param stifness: the stiffness of the spring
     */
    public Spring (String id,
                   int collisionId,
                   JGColor color,
                   PhysicalSolidObject object1,
                   PhysicalSolidObject object2,
                   double length,
                   double stifness)
    {
        super(id, collisionId, color, true);
        myObject1 = object1;
        myObject2 = object2;
        myRestLength = length;
        myStifness = stifness;
        
    }

    @Override
    /**
     * override move function in JGObject. Handle the movement and force
     */
    public void move () {
        super.move();
        if(!myObject1.isAlive()|| !myObject2.isAlive()){
            remove();
            return;
        }
        setSpringLength();
        generateForce();

    }

    /**
     * This function is called in move function.
     * Different kind of springies may act different.
     * Subclass of Spring may want to override this.
     */
    protected void setSpringLength () {

    }

    /**
     * apply the force calculate by Hook's Law to
     * masses it links.
     */
    protected void generateForce () {

        double currentLength = getCurrentLength();
        boolean isContract = currentLength <= myRestLength;
        double distance = Math.abs(currentLength - myRestLength);
        double amplitude = distance * myStifness;
        if (amplitude == 0)
            return;
        double centerX = (myObject1.getX() + myObject2.getX()) / 2;

        double centerY = (myObject1.getY() + myObject2.getY()) / 2;

        double fx = isContract ? myObject1.getX() - centerX : centerX - myObject1.getX();
        double fy = isContract ? myObject1.getY() - centerY : centerY - myObject1.getY();
        if (currentLength == 0) {
            fx = lastFx;
            fy = lastFy;
        }
        else {
            fx = fx / (currentLength / 2) * amplitude;
            fy = fy / (currentLength / 2) * amplitude;
            lastFx = fx;
            lastFy = fy;
        }
        myObject1.setForce(fx, fy);
        myObject2.setForce(-fx, -fy);

    }

    /**
     * @return return the currentLength
     */
    private double getCurrentLength () {
        double currentLength = Math.sqrt(Math.pow((myObject1.getX() - myObject2.getX()), 2) +
                                         Math.pow((myObject1.getY() - myObject2.getY()), 2));
        return currentLength;
    }

    @Override
    /**
     * Paint the spring using drawLine();
     */
    public void paintShape () {
        myEngine.setColor(myColor);
        myEngine.drawLine(myObject1.getX(), myObject1.getY(), myObject2.getX(), myObject2.getY(),
                          true);
    }
    
    @Override
    public boolean equals(Object o){
        if(! (o instanceof Spring))
            return false;
        Spring s = (Spring)o;
        return myRestLength==s.myRestLength&&
       myObject1==s.myObject1&&
       myObject2==s.myObject2&&
       myStifness==s.myStifness;
       
    }

}
