package jboxGlue.Node;

import jboxGlue.Assembly;
import jboxGlue.PhysicalObjectCircle;
import org.jbox2d.common.Vec2;
import springies.EnvironmentVariables;
import jgame.JGColor;
import jgame.JGObject;


/**
 * Mass class that extends PhysicalObjectCircle
 */
public class Mass extends PhysicalObjectCircle {

    private static final double DEFAULT_RADIUS = 5;
    private Assembly myAssembly;

    /**
     * Constructor to create object when mass and velocity are specified
     * 
     * @param id : unique identifier
     * @param collisionId : id used to identify object when it collides with other objects
     * @param color : color to paint object with
     * @param mass : mass specified for Mass
     * @param xPos : x position to place Mass
     * @param yPos : y position to place Mass
     * @param xVelocity : x velocity
     * @param yVelocity : y velocity
     */
    public Mass (String id,
                 int collisionId,
                 JGColor color,
                 double mass,
                 double xPos,
                 double yPos,
                 float xVelocity,
                 float yVelocity,
                 Assembly assembly) {
        super(id, collisionId, color, DEFAULT_RADIUS, mass);
        initializeMassSettings(mass, xPos, yPos, xVelocity, yVelocity, assembly);

    }

    /**
     * Constructor to create object when velocity is not specified
     * 
     * @param id : unique identifier
     * @param collisionId : id used to identify object when it collides with other objects
     * @param color : color to paint object with
     * @param mass : mass specified for Mass
     * @param xPos : x position to place Mass
     * @param yPos : y position to place Mass
     */
    public Mass (String id,
                 int collisionId,
                 JGColor color,
                 float mass,
                 double xPos,
                 double yPos,
                 Assembly assembly) {
        this(id, collisionId, color, mass, xPos, yPos, 0, 0, assembly);
    }

    /**
     * Initializes settings for mass like mass, position, velocity and
     * Objects the Mass can collide with
     * 
     * @param mass : mass to assign object
     * @param xPos : x position to place Mass
     * @param yPos : y position to place Mass
     * @param xVelocity : x velocity
     * @param yVelocity : y velocity
     */
    private void initializeMassSettings (double mass,
                                         double xPos,
                                         double yPos,
                                         float xVelocity,
                                         float yVelocity,
                                         Assembly assembly) {
        Mass.this.setPos(xPos, yPos);
        myBody.m_mass = (float) mass;
        setObjectsToCollideWith(1, 2);
        initializeVelocity(xVelocity, yVelocity);
        myAssembly = assembly;
    }

    /**
     * Initializes the velocity of mass based on parameters
     * 
     * @param xVelocity : velocity in x direction
     * @param yVelocity : velocity in y direction
     */
    private void initializeVelocity (float xVelocity, float yVelocity) {
        Vec2 velocity = myBody.getLinearVelocity();
        velocity.x = xVelocity;
        velocity.y = yVelocity;
        myBody.setLinearVelocity(velocity);
    }

    /**
     * Applies the viscocity to the mass based on the
     * viscocity specified in EnvironmentVariables
     * Viscocity is specified when XML is read but Mass
     * does not need to worry about how Viscocity is determined so long
     * as it is in EnvironmentVariables
     */
    private void applyViscocity () {
        Vec2 velocity = myBody.getLinearVelocity();
        float forceY = (float) (EnvironmentVariables.getViscosity() * velocity.y) * -1;
        float forceX = (float) (EnvironmentVariables.getViscosity() * velocity.x) * -1;
        this.setForce(forceX, forceY);

    }

    /**
     * Applies the Force due to center of mass
     * by computing the distance then the amplitude
     * using the magnitude and exponent specified for the environment
     */
    private void applyCenterOfMassForce () {
        Vec2 centerMassValues = myAssembly.getCenterOfMass();
        double distance = computeDistance(centerMassValues.x, centerMassValues.y);
        if (distance == 0) {
            return;
        }
        double amplitude =
                EnvironmentVariables.getCenterMassMagnitude() *
                        Math.pow(distance, EnvironmentVariables.getCenterMassExponent() * -1);
        double forceX =
                (amplitude / distance) * (centerMassValues.x - myBody.getXForm().position.x);
        double forceY =
                (amplitude / distance) * (centerMassValues.y - myBody.getXForm().position.y);
        this.setForce(forceX, forceY);
    }

    @Override
    public void move () {
        super.move();
        applyViscocity();
        applyCenterOfMassForce();

    }

    @Override
    /**
     * Mass currently does not impact other objects
     * it hits
     */
    public void hit (JGObject other) {

    }
    @Override
    public boolean equals(Object o){
        if(! (o instanceof Mass))
            return false;
        Mass m= (Mass)o;
        boolean equalVelocity= myBody.getLinearVelocity().x==m.myBody.getLinearVelocity().x
                &&myBody.getLinearVelocity().y==m.myBody.getLinearVelocity().y;
       
        return m.getBody().m_mass==getBody().m_mass &&
                m.x==x &&
                m.y==y &&
                equalVelocity;
                
    }

}
