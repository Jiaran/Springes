package jboxGlue.Barrier;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jbox2d.common.Vec2;
import springies.EnvironmentVariables;
import jboxGlue.Assembly;
import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectRect;
import jboxGlue.PhysicalSolidObject;
import jgame.JGColor;
import jgame.JGObject;
import jgame.platform.JGEngine;


/**
 * Wall superclass that all types of walls
 * can extend
 * wallID is used to identify it as either wall 1,2,3, or 4
 * wallPositionType specifies which axis wall is closest to: positive or negative
 */
public abstract class Walls extends PhysicalObjectRect {
    private String wallID;
    protected int wallMovementConstant;
    protected int maxPosition;
    protected int minPosition;
    private double repulsionExponent;
    private double repulsionMagnitude;
    private boolean turnRepulsionOn = false;
    protected final double DAMPING_FACTOR = .8;
    protected double EXPAND_WALL_DIMENSION = 2;

    /**
     * Constructor
     * 
     * @param id : unique id for object
     * @param collisionId : id to identify object in event of collision
     * @param color : color to assign object
     * @param width : width of object
     * @param height : height of object
     * @param givenRepulsionExponent : wall repulsion exponent
     * @param givenRepulsionMagnitude : wall repulsion magnitude
     * @param xPos : x position
     * @param yPos : y positionb
     * @param minPos : minimum position wall can drecrease to
     * @param maxPos : max position wall can increase to
     * @param wallMovementConstant : constant at which wall moves - used to change velocity and x/y
     */
    public Walls (String id,
                  int collisionId,
                  JGColor color,
                  double width,
                  double height,
                  double givenRepulsionExponent,
                  double givenRepulsionMagnitude,
                  double xPos,
                  double yPos,
                  int minPos,
                  int maxPos,
                  int wallMovementConstant) {
        super(id, collisionId, color, width, height);
        initWallSettings(id, xPos, yPos, givenRepulsionExponent, givenRepulsionMagnitude, minPos,
                         maxPos, wallMovementConstant);
    }

    /**
     * Inititalizes settings for wall like position and its repulsion magnitude and exponent
     * It also defines that the wall can collide with the masses
     * 
     * @param xPos - x position to place wall
     * @param yPos -y position to place wall
     * @param givenRepulsionExponent - the defined repulsion exponent for given wall
     * @param givenRepulsionMagnitude - the defined repulsion magnitude for given wall
     * @param minPos : minimum position wall can drecrease to
     * @param maxPos : max position wall can increase to
     * @param wallMovementConstant : constant at which wall moves - used to change velocity and x/y
     */
    private void initWallSettings (String id,
                                   double xPos,
                                   double yPos,
                                   double givenRepulsionExponent,
                                   double givenRepulsionMagnitude,
                                   int minPos,
                                   int maxPos,
                                   int velocity) {
        // defines that wall can collide with masses for jbox
        setObjectsToCollideWith(2, 1);
        wallID = id;
        this.setPos(xPos, yPos);
        repulsionExponent = givenRepulsionExponent;
        repulsionMagnitude = givenRepulsionMagnitude;
        maxPosition = maxPos;
        minPosition = minPos;
        wallMovementConstant = velocity;
    }

    /**
     * Helper function to compute amplitude for wall repulsion
     * 
     * @param objectPosition - used to calculate distance
     * @param wallPosition - used to calculate distance between wall and object
     * @return amplitude of force
     */
    protected double computeAmplitude (double objectPosition, double wallPosition) {
        double distance = objectPosition - wallPosition;
        double neg = 1;
        if (distance < 0) {
            neg = -1;
        }
        if (distance == 0) {

            distance = 1;
        }
        distance = Math.abs(distance);
        double amplitude = repulsionMagnitude * (1.0 / Math.pow(distance, repulsionExponent));
        return neg * amplitude;

    }

    /**
     * Currently, hit method makes the mass bounce
     * 
     * @param o - the mass that hits wall
     */
    @Override
    public void hit (JGObject o) {
        PhysicalObject obj = (PhysicalObject) o;
        applyBounce(obj);
    }

    /**
     * The method to calculate bounce is different for a ceiling/floor wall
     * vs. a side wall. Therefore, each subclass will handle its bounce calculation
     * 
     * @param velocity
     */
    protected abstract void calculateBounce (Vec2 velocity);

    /**
     * Helper method to apply the bounce on object that hits wall
     * 
     * @param obj - object to apply bounce
     */
    private void applyBounce (PhysicalObject obj) {
        Vec2 velocity = obj.getBody().getLinearVelocity();
        calculateBounce(velocity);
        obj.getBody().setLinearVelocity(velocity);
    }

    /**
     * A JGame method that is continiously called so we can
     * call other functions that need to be continiously called like
     * applying/disabling repulsion forces.
     * Additionally, we need to always have the ability to move walls
     * so the method to do so is called here
     * 
     */
    @Override
    public void move () {
        activateandDisableRepulsion();
        applyRepulsionToMassObjects();
        decideWallMovement();

        super.move();
    }

    private String getWallType () {
        return wallID;
    }

    /**
     * Both activates and disables repulsion forces
     * by listening for key presses that correspond to the wall id specified
     * by the XML
     */
    private void activateandDisableRepulsion () {
        int keyRepulsion = this.getWallType().toCharArray()[0];
        turnRepulsionOn = eng.getKey(keyRepulsion) ^ turnRepulsionOn;
        eng.clearKey(keyRepulsion);
    }

    /**
     * Method that applies the appropriate repulsion force on a mass.
     * Each subclass handles the calculation because repulsion force can differ
     * based on wall position and type of wall (ie ceiling wall vs. a side wall)
     * 
     * @param objectWithMass - Mass to apply repulsion
     */
    protected abstract void applyReplusionForce (PhysicalSolidObject objectWithMass);

    /**
     * Iterates through all PhysicalSolidObject
     * and applies repulsion forces
     */
    private void applyRepulsionToMassObjects () {
        if (turnRepulsionOn) {

            List<Assembly> assemblies = EnvironmentVariables.getAssemblyList();
            Iterator<Assembly> assembliesIterator = assemblies.iterator();

            while (assembliesIterator.hasNext()) {
                Iterator<Map.Entry<String, PhysicalSolidObject>> solidObjectsIterator =
                        assembliesIterator.next().getAllSolidObjects().entrySet().iterator();
                while (solidObjectsIterator.hasNext()) {
                    PhysicalSolidObject solidObject = solidObjectsIterator.next().getValue();
                    applyReplusionForce(solidObject);

                }
            }

        }
    }

    /**
     * Listens to keypresses to move walls
     * Calls on increaseWalledArea and
     * decreaseWalledArea to handle
     * different wall movement functionality and
     * calculation
     */
    private void decideWallMovement () {
        if (eng.getKey(JGEngine.KeyUp)) {
            increaseWalledArea();
        }
        if (eng.getKey(JGEngine.KeyDown)) {
            decreaseWalledArea();
        }

    }

    /**
     * Method to dcrease the walled area
     * by moving the walls and determing
     * how to move each wall
     * based on WallAxisPosition
     */
    private void decreaseWalledArea () {
        if (okToDecreaseWalledArea()) {
            moveWall(-1);
        }
    }

    /**
     * Method to increase the walled areas
     * by moving the walls and determing
     * how to move each wall
     * based on Wall Position Type
     */
    private void increaseWalledArea () {
        if (okToIncreaseWalledArea()) {
            moveWall(1);
        }
    }

    /**
     * Check to ensure it is feasible to expand walled area
     * 
     * @return boolean
     */
    protected abstract boolean okToIncreaseWalledArea ();

    /**
     * Check to ensure it is feasible to decrease walled area
     * 
     * @return boolean
     */
    protected abstract boolean okToDecreaseWalledArea ();

    /**
     * Move wall moves the actual wall by
     * setting the velocity and adjusting the physical position
     * 
     * @param velocity : velocity wall should move. Can be negative or positive
     *        depending on what axis wall is closest to
     * @param wallMovementScalle : used to position wall
     */
    private void moveWall (int direction) {
        myBody.setLinearVelocity(setVelocity(direction * wallMovementConstant));
        adjustPositionAndDimension(direction * wallMovementConstant, direction);
        this.setPos(this.x, this.y);
    }

    /**
     * Set velocity to appropriate x or y component
     * 
     * @param velocityValue
     * @return vector with updated velocity values
     */
    protected abstract Vec2 setVelocity (float velocityValue);

    /**
     * Adjust the wall position and dimension
     * to scale with adjacent wall movement
     * 
     * @param wallMovementScale - factor by which to move wall
     */
    protected abstract void adjustPositionAndDimension (float wallMovementScale, float direction);
}
