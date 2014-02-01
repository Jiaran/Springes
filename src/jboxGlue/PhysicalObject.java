package jboxGlue;

import jgame.JGColor;
import jgame.JGObject;
import jgame.impl.JGEngineInterface;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import springies.WorldManager;


/**
 * PhysicalObject is the base class for everything in the SPringies World
 * it handles the creation and destroy.
 * The abstraction function named paintShape requires subclass to paint themselves
 * 
 */
public abstract class PhysicalObject extends JGObject {
    protected JGEngineInterface myEngine;
    protected boolean myHasImage;
    protected JGColor myColor;
    protected Body myBody;
    protected float myRotation;

    /**
     * @param name: name
     * @param collisionId: collision id
     * @param color: color
     * @param unique: unique to decide whether it should have a unique id. IMPORTANT for
     *        JGame to handle objects.
     */
    protected PhysicalObject (String name, int collisionId, JGColor color, boolean unique) {
        super(name, unique, 0, 0, collisionId, null);

        myColor = color;
        myHasImage = false;

        init();
    }

    /**
     * Intilialize defaults
     */
    private void init () {
        // init defaults
        myEngine = eng;
    }

    /**
     * Getter method
     * 
     * @return myBody
     */
    public Body getBody () {
        return myBody;
    }

    /**
     * Getter method
     * 
     * @return myColor
     */
    public JGColor getColor () {
        return myColor;
    }

    /**
     * Setter
     * 
     * @param color : color to set PhysicalObject to
     */
    public void setColor (JGColor color) {
        myColor = color;
    }

    @Override
    /**
     * Generic move method
     * 
     * @see jgame.JGObject#move()
     */
    public void move () {
        /*
         * this function should destroy the objects in both jgame and jbox
         * not useful now because no destroying objects happen AT PRESENT
         */
        // if the JGame object was deleted, remove the physical object too
        // if( myBody.m_world != WorldManager.getWorld() )
        // {
        // remove();
        // return;
        // }

    }

    @Override
    /**
     * setPos to set the position in JBox.
     * 
     * @see jgame.JGObject#setPos(double, double)
     */
    public void setPos (double x, double y) {
        // there's no body yet while the game object is initializing
        if (myBody == null) { return; }

        // set the position of the jbox2d object, not the jgame object
        myBody.setXForm(new Vec2((float) x, (float) y), -myRotation);
    }

    @Override
    /**
     * called when destroyed. To make sure jgame world consistent to jbox world
     * 
     * @see jgame.JGObject#destroy()
     */
    public void destroy () {
        // body may not be in actual world. If not, do not call destroyBody.
        if (myBody == null) { return; }

        if (myBody.m_world == WorldManager.getWorld()) {
            // also destroys associated joints
            WorldManager.getWorld().destroyBody(myBody);
        }
    }

    @Override
    public void paint () {
        // only paint something if we need to draw a shape. Images are already drawn
        if (!myHasImage) {
            paintShape();
        }
    }

    /**
     * paint shape of the objects.
     */
    protected abstract void paintShape ();
}
