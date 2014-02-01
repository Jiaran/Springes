package jboxGlue;

import jgame.JGColor;
import org.jbox2d.collision.CircleDef;


/**
 * PhysicalObjectCircle that specifies the specifications
 * to create a circular object by setting up both the JGame and
 * JBox infastructure
 * 
 */
public class PhysicalObjectCircle extends PhysicalSolidObject {
    private double myRadius;

    /**
     * Constructor
     * 
     * @param id : unique identifier
     * @param collisionId : id used to identify object when it collides with other objects
     * @param color : color to paint object with
     * @param radius : radius of the circle
     */
    public PhysicalObjectCircle (String id, int collisionId, JGColor color, double radius) {
        this(id, collisionId, color, radius, 0);
    }

    /**
     * Alternate constructor to specify mass
     * 
     * @param id : unique identifier
     * @param collisionId : id used to identify object when it collides with other objects
     * @param color : color to paint object with
     * @param radius : radius of the circle
     * @param mass : mass for circle
     */
    public PhysicalObjectCircle (String id,
                                 int collisionId,
                                 JGColor color,
                                 double radius,
                                 double mass) {
        super(id, collisionId, color);
        initializeCircleFeatures(radius, mass);
    }

    /**
     * Specify certain features like the shape, radius, density for circle
     * 
     * @param radius
     * @param mass
     */
    private void initializeCircleFeatures (double radius, double mass) {
        // save arguments
        myRadius = radius;

        // make it a circle
        CircleDef shape = new CircleDef();

        shape.radius = (float) radius;
        shape.density = (float) mass;
        createBody(shape);
        setBBox(-(int) radius, -(int) radius, 2 * (int) radius, 2 * (int) radius);
    }

    @Override
    /**
     * method to Paint the shape of the circle based on specified radius , location, and color
     * 
     * @see jboxGlue.PhysicalSolidObject#paintShape()
     */
    public void paintShape () {

        myEngine.setColor(myColor);
        myEngine.drawOval(x, y, (float) myRadius * 2, (float) myRadius * 2, true, true);
    }
}
