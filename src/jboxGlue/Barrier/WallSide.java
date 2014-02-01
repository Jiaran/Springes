package jboxGlue.Barrier;

import jboxGlue.PhysicalSolidObject;
import jgame.JGColor;
import org.jbox2d.common.Vec2;


/**
 * Subclass that represents walls with id 2 or 4
 * They are drawn on the left and right side of the screen
 * Created to handle the specific x axis behavior
 */
public class WallSide extends Walls {

    public WallSide (String id,
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
              xPos, yPos,minPos,maxPos,velocity);
    }

    protected void calculateBounce (Vec2 velocity) {
        velocity.x *= -DAMPING_FACTOR;

    }
    protected boolean okToIncreaseWalledArea(){
        return this.x != maxPosition;
    }
    protected boolean okToDecreaseWalledArea(){

        return this.x != minPosition;
    }
    /**
     * Custom method to apply reuplusion force to x component
     * since the wall is on either the left or right
     */
    @Override
    protected void applyReplusionForce (PhysicalSolidObject objectWithMass) {
        double forceX = 0;
        double forceY = 0;
        forceX = computeAmplitude(objectWithMass.getBody().getXForm().position.x,
                                  myBody.getXForm().position.x);

        objectWithMass.setForce(forceX, forceY);
    }

    protected Vec2 setVelocity (float velocityValue) {
        Vec2 velocity = new Vec2(velocityValue, 0);
        return velocity;

    }

    protected void adjustPositionAndDimension (float wallMovementScale,float direction) {
        this.x += wallMovementScale;
        this.setMyHeight(this.getMyHeight() +  direction*EXPAND_WALL_DIMENSION);

    }

}
