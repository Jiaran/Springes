package jboxGlue.Node;

import jboxGlue.Assembly;
import jgame.JGColor;


/**
 * Subclass specific to mass that user can create
 * when clicking on screen because it
 * is able to move based on mouse positon
 * 
 * @author lalitamaraj
 * 
 */
public class UserMovableMass extends Mass {

    /**
     * Constructor
     */
    public UserMovableMass (String id,
                            int collisionId,
                            JGColor color,
                            float mass,
                            double xPos,
                            double yPos,
                            Assembly assembly) {
        super(id, collisionId, color, mass, xPos, yPos, assembly);

    }

    /**
     * Removes mass if mouse is released
     */
    private void removeMassWhenMouseReleased () {
        if (!eng.getMouseButton(1)) {
            this.remove();

        }
    }

    @Override
    /**
     *Move method that meoves object based on 
     *mouse location
     *Also, removes mass when mouse is released      
     */
    public void move () {

        this.setPos(eng.getMouseX(), eng.getMouseY());
        removeMassWhenMouseReleased();

    }

}
