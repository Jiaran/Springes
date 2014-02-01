package jboxGlue;

import jgame.JGColor;
import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import springies.WorldManager;


/**
 * superclass PhysicalSolidObject used to create
 * Objects with mass
 */
abstract public class PhysicalSolidObject extends PhysicalObject {

    /**
     * Constructor
     * 
     * @param id : unique identifier for
     * @param collisionId : id to identify Object in the event of collision
     * @param color : color to paint object
     */
    protected PhysicalSolidObject (String id, int collisionId, JGColor color) {
        super(id, collisionId, color, true);

    }

    @Override
    /**
     * Each object is different so each class that extends
     * this class needs to define its own paintShape() method
     * 
     * @see jboxGlue.PhysicalObject#paintShape()
     */
    abstract protected void paintShape ();

    /**
     * Initializes the myBody for the purposes of
     * jbox
     * 
     * @param shapeDefinition : used to define myBody shape parameters like
     *        boundary and CategoryBits
     */
    protected void createBody (ShapeDef shapeDefinition) {
        myBody = WorldManager.getWorld().createBody(new BodyDef());
        myBody.createShape(shapeDefinition);
        // for following body back to JGObject
        myBody.setUserData(this);
        myBody.setMassFromShapes();
        myBody.m_world = WorldManager.getWorld();
    }

    /*
     * BBox is set with a marginal term to give the JGame leeway
     * to determine collisions
     * 
     * @see jgame.JGObject#setBBox(int, int, int, int)
     */
    public void setBBox (int x, int y, int width, int height) {
        final int MARGIN_TERM = 8;
        x -= MARGIN_TERM;
        y -= MARGIN_TERM;
        width += MARGIN_TERM * 2;
        height += MARGIN_TERM * 2;
        super.setBBox(x, y, width, height);
    }

    /**
     * sets force on Object
     * 
     * @param xForce : force to apply in x direction
     * @param yForce : force to apply in y direction
     */
    public void setForce (double xForce, double yForce) {
        myBody.applyForce(new Vec2((float) xForce, (float) yForce), myBody.m_xf.position);
    }

    /**
     * For JBox this enables what the Object's category bit is and
     * what Objects they can collide with
     * 
     * @param categoryBit : the category bit you want to assign to Object
     * @param maskBit : category bit of object to enable collision with
     */
    public void setObjectsToCollideWith (int categoryBit, int maskBit) {

        FilterData filterData = new FilterData();
        filterData.categoryBits = categoryBit;
        filterData.maskBits = maskBit;
        myBody.getShapeList().setFilterData(filterData);
    }

    /**
     * Helper function to compute distance between object and another object
     * 
     * @param otherObjectXPos - x position of other object
     * @param otherObjectYpos - y position of other object
     * @return
     */
    public double computeDistance (double otherObjectXPos, double otherObjectYpos) {
        double xDistanceSquared =
                Math.pow(otherObjectXPos - x, 2);
        double yDistanceSquared =
                Math.pow(otherObjectYpos - y, 2);
        double sumOfDistancesSquared = xDistanceSquared + yDistanceSquared;
        double squareRootSumDistances = Math.sqrt(sumOfDistancesSquared);
        return squareRootSumDistances;
    }

    @Override
    /**
     * Generic move method that modifies Object's position
     * 
     * @see jboxGlue.PhysicalObject#move()
     */
    public void move () {
        super.move();
        // copy the position and rotation from the JBox world to the JGame world
        Vec2 position = myBody.getPosition();
        x = position.x;
        y = position.y;
        myRotation = -myBody.getAngle();
    }

    /**
     * @return Object x position
     */
    public double getX () {
        return x;

    }

    /**
     * @return Object y position
     */
    public double getY () {
        return y;
    }

    @Override
    /**
     * Sets position of Object bot in JGame and JBox
     * world
     */
    public void setPos (double x_pos, double y_pos){
        // there's no body yet while the game object is initializing
        if (myBody == null) { return; }
        x = x_pos; // JGame requirements
        y = y_pos; // JGame requirements
        // set the position of the jbox2d object, not the jgame object
        myBody.setXForm(new Vec2((float) x_pos, (float) y_pos), -myRotation);
    }

}
