package jboxGlue.Node;

import jboxGlue.Assembly;
import jgame.JGColor;


public class FixedMass extends Mass {

    /**
     * Constructor for FixedMass which extends the Mass class
     * A mass of 0 is given for fixed mass because it is massless
     * 
     * @param id : unique identifier of object
     * @param collisionId : collision id used to identify objects in the event of collision
     * @param color : color to paint fixed mass
     * @param xPos : x position to place fixed mass
     * @param yPos : y position to place fixed mass
     */
    public FixedMass (String id,
                      int collisionId,
                      JGColor color,
                      double xPos,
                      double yPos,
                      Assembly assembly) {
        super(id, collisionId, color, 0, xPos, yPos, assembly);
    }

    @Override
    /**
     * The fixed mass is not intended to move so
     * override to avoid fixed mass from moving
     * 
     * @see jboxGlue.Mass#move()
     */
    public void move () {
        // prevent the Mass From moving
    }

    @Override
    /**
     * Forces should not act on this fixedMass because it
     * should not move
    
     */
    public void setForce (double force_x, double force_y) {
        // prevent anything from imparting a force on Mass

    }

}
