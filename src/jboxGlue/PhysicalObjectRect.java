package jboxGlue;

import jgame.JGColor;
import org.jbox2d.collision.PolygonDef;


/**
 * Class to create rectangular objects like Walls
 * 
 */
public class PhysicalObjectRect extends PhysicalSolidObject {
    private static final int POINT0 = 0;
    private static final int POINT1 = 1;
    private static final int POINT2 = 2;
    private static final int POINT3 = 3;
    private static final int NUMBER_SIDES = 4;
    private double myWidth;
    private double myHeight;
    private double[] myPolyx;
    private double[] myPolyy;

    /**
     * Constructor
     * 
     * @param id : unique identifier for
     * @param collisionId : id to identify Object in the event of collision
     * @param color : color to paint object
     * @param width : width of object
     * @param height : width of height
     */
    public PhysicalObjectRect (String id,
                               int collisionId,
                               JGColor color,
                               double width,
                               double height) {
        this(id, collisionId, color, width, height, 0);
    }

    /**
     * Alternate Constructor to assign mass
     * 
     * @param id : unique identifier for
     * @param collisionId : id to identify Object in the event of collision
     * @param color : color to paint object
     * @param width : width of object
     * @param height : width of height
     * @param mass : mass to assign to wall
     */
    public PhysicalObjectRect (String id,
                               int collisionId,
                               JGColor color,
                               double width,
                               double height,
                               double mass) {
        super(id, collisionId, color);
        initializeRectangleFeatures(width, height, mass);
    }

    /**
     * Initializer used to features like width and height and
     * create the actual rectangular shape
     * 
     * @param width :width of rectangle to be created
     * @param height: height of rectangle to be created
     * @param mass : mass of rectangle to be created
     */
    public void initializeRectangleFeatures (double width, double height, double mass) {
        myWidth = width;
        myHeight = height;

        // init defaults
        myPolyx = null;
        myPolyy = null;

        // make it a rectangle
        PolygonDef shape = new PolygonDef();
        shape.density = (float) mass;
        shape.setAsBox((float) width, (float) height);
        createBody(shape);
        setBBox(-(int) width / 2, -(int) height / 2, (int) width, (int) height);

    }

    /**
     * Setter for width
     * 
     * @param width
     */
    public void setMyWidth (double width) {
        myWidth = width;
    }

    /**
     * Setter for height
     * 
     * @param height
     */
    public void setMyHeight (double height) {
        myHeight = height;
    }

    /**getter for Width
     * @return myWidth
     */
    public double getMyWidth () {
        return myWidth;
    }

    /**Getter for height
     * @return myHeight
     */
    public double getMyHeight () {
        // TODO Auto-generated method stub
        return myHeight;
    }

    @Override
    /**
     * Paint Rectangle on Screen
     * 
     * @see jboxGlue.PhysicalSolidObject#paintShape()
     */
    public void paintShape () {
        if (myPolyx == null || myPolyy == null) {
            // allocate memory for the polygon
            final int NUMBER_OF_SIDES = 4;
            myPolyx = new double[NUMBER_OF_SIDES];
            myPolyy = new double[NUMBER_OF_SIDES];
        }

        // draw a rotated polygon
        myEngine.setColor(myColor);
        double cos = Math.cos(myRotation);
        double sin = Math.sin(myRotation);
        double halfWidth = myWidth / 2;
        double halfHeight = myHeight / 2;
        myPolyx[POINT0] = (int) (x - halfWidth * cos - halfHeight * sin);
        myPolyy[POINT0] = (int) (y + halfWidth * sin - halfHeight * cos);
        myPolyx[POINT1] = (int) (x + halfWidth * cos - halfHeight * sin);
        myPolyy[POINT1] = (int) (y - halfWidth * sin - halfHeight * cos);
        myPolyx[POINT2] = (int) (x + halfWidth * cos + halfHeight * sin);
        myPolyy[POINT2] = (int) (y - halfWidth * sin + halfHeight * cos);
        myPolyx[POINT3] = (int) (x - halfWidth * cos + halfHeight * sin);
        myPolyy[POINT3] = (int) (y + halfWidth * sin + halfHeight * cos);
        myEngine.drawPolygon(myPolyx, myPolyy, null, NUMBER_SIDES, true, true);
    }
}
