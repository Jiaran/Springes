package jboxGlue.Factory;

import java.util.HashMap;
import jboxGlue.Assembly;
import jboxGlue.Barrier.TopBottomWalls;
import jboxGlue.Barrier.WallSide;
import jgame.JGColor;


/**
 * Factory to create the two different walls
 * 
 */
public class WallFactory extends Factory {

    private static final int WALL1_MIN_POSITION = 4;
    private static final int WALL1_MAX_POSITION = 230;
    private static final int WALL3_MAX_POSITION = 252;
    private static final int WALL3_MIN_POSITION = 476;
    private static final int WALL4_MAX_POSITION = 227;
    private static final int WALL4_MIN_POSITION = 5;
    private static final int WALL2_MAX_POSITION = 623;
    private static final int WALL2_MIN_POSITION = 849;
    private static final double DISPLAY_HEIGHT = 480;
    private static final int DISPLAY_WIDTH = 853;
    private final int WALL_MOVEMENT_CONSTANT = 1;

    @Override
    public void create (HashMap<String, String> information, Assembly assembly) {
        addWall(information.get("id"), StringtoDouble(information.get("magnitude")),
                StringtoDouble(information.get("exponent")));
       
    }

    /**
     * Method to determine which wall to create
     * Either creates a CeilingFloor wall or
     * all WallSide
     * 
     * @param id - wall id defined by xml
     * @param magnitude - wall repulsion magnitude
     * @param exponent - wall repulsion exponent
     */
    private void addWall (String id, double magnitude, double exponent) {
        final double WALL_MARGIN = 10;
        final double WALL_THICKNESS = 10;
        final double WALL_WIDTH = DISPLAY_WIDTH - WALL_MARGIN * 2 + WALL_THICKNESS;
        final double WALL_HEIGHT = DISPLAY_HEIGHT - WALL_MARGIN * 2 + WALL_THICKNESS;
        JGColor color = JGColor.green;
        switch (id) {
            case "1":
                new TopBottomWalls("1", 1, color, WALL_WIDTH, WALL_THICKNESS, exponent, magnitude,
                                   DISPLAY_WIDTH / 2, WALL_MARGIN, WALL1_MAX_POSITION,
                                   WALL1_MIN_POSITION, -1 * WALL_MOVEMENT_CONSTANT);
                break;
            case "3":
                new TopBottomWalls("3", 1, color, WALL_WIDTH, WALL_THICKNESS, exponent, magnitude,
                                   DISPLAY_WIDTH / 2, DISPLAY_HEIGHT -
                                                      WALL_MARGIN, WALL3_MAX_POSITION,
                                   WALL3_MIN_POSITION, WALL_MOVEMENT_CONSTANT);
                break;
            case "4":
                new WallSide("4", 1, color, WALL_THICKNESS, WALL_HEIGHT, exponent, magnitude,
                             WALL_MARGIN, DISPLAY_HEIGHT / 2, WALL4_MAX_POSITION,
                             WALL4_MIN_POSITION, -1 * WALL_MOVEMENT_CONSTANT);
                break;
            case "2":
                new WallSide("2", 1, color, WALL_THICKNESS, WALL_HEIGHT, exponent, magnitude,
                             DISPLAY_WIDTH - WALL_MARGIN, DISPLAY_HEIGHT / 2, WALL2_MAX_POSITION,
                             WALL2_MIN_POSITION, WALL_MOVEMENT_CONSTANT);
                break;
            default:
                break;
        }
        
    }

    @Override
    protected double StringtoDouble (String str) {
        return Double.parseDouble(str);
    }
}
