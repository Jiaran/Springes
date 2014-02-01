package jboxGlue.Barrier;

import jboxGlue.PhysicalSolidObject;
import jgame.JGColor;
import org.jbox2d.common.Vec2;


/**
 * Subclass that represents walls with id 1 or 3
 * They are drawn on the top and bottom of screen
 * Created to handle the specific y axis behavior
 */
public class TopBottomWalls extends Walls {

    public TopBottomWalls (String id,
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
                           int velocity) {
        super(id, collisionId, color, width, height, givenRepulsionExponent,
              givenRepulsionMagnitude,
              xPos, yPos,minPos,maxPos,
              velocity);
    }

    @Override
    protected void calculateBounce (Vec2 velocity) {
        velocity.y *= -DAMPING_FACTOR;
    }

    /**
     * Custom method to apply reuplusion force to y component
     * since the wall is on either on the top or bottom
     */
    @Override
    protected void applyReplusionForce (PhysicalSolidObject objectWithMass) {
        double forceX = 0;
        double forceY = 0;
        forceY = computeAmplitude(objectWithMass.getBody().getXForm().position.y,
                                  myBody.getXForm().position.y);

        objectWithMass.setForce(forceX, forceY);
    }

    protected Vec2 setVelocity (float velocityValue) {
        Vec2 velocity = new Vec2(0, velocityValue);
        return velocity;
    }

    protected void adjustPositionAndDimension (float wallMovementScale,float direction) {
        this.y += wallMovementScale;
        this.setMyWidth(this.getMyWidth() + direction*EXPAND_WALL_DIMENSION);

    }
    protected boolean okToIncreaseWalledArea(){

        return this.y != maxPosition;
    }
    
    protected boolean okToDecreaseWalledArea(){

        return this.y != minPosition;
    }

}
