package springies;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jboxGlue.Assembly;
import jboxGlue.PhysicalSolidObject;
import jboxGlue.Link.Spring;
import jboxGlue.Node.Mass;
import jboxGlue.Node.UserMovableMass;
import jgame.JGColor;
import jgame.platform.JGEngine;
import org.jbox2d.common.Vec2;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/**
 * Springies Engine
 * 
 */
@SuppressWarnings("serial")
public class Springies extends JGEngine{
    private static final int MAX_FRAME_SKIP = 2;
    private static final int FRAME_PER_SECOND = 100;
    private final int DEFAULT_HEIGHT = 480;
    private final double DEFAULT_ASPECT = (16.0 / 9.0);
    private final int DEFAULT_WIDTH = (int) (DEFAULT_HEIGHT * DEFAULT_ASPECT);
    private final ReadData reader = new ReadData();
    JFileChooser fc = new JFileChooser();
    /**
     * Constructor for Springies engine
     * Sets width and height of engine
     */
    public Springies () {
        // set the window size
        initEngine(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public void initCanvas (){

        setCanvasSettings(
                          1, // width of the canvas in tiles
                          1, // height of the canvas in tiles
                          displayWidth(), // width of one tile
                          displayHeight(), // height of one tile
                          null,// foreground colour -> use default colour white
                          null,// background colour -> use default colour black
                          null // standard font -> use default font
        );
    }

    @Override
    public void initGame (){

        setFrameRate(FRAME_PER_SECOND, MAX_FRAME_SKIP);
        WorldManager.initWorld(this);
        WorldManager.getWorld().setGravity(new Vec2(0.0f, 0.0f));

        reader.readEnvironment(new File("./assets/data/environment.xml"));
        reader.readAssembly(new File("./assets/data/example.xml"));

    }

 

    @Override
    public void doFrame () {

        generateUserInputActions();
        WorldManager.getWorld().step(1f, 1);
        moveObjects();
        checkCollision(2, 1);
    }

    @Override
    public void paintFrame () {
        // nothing to do
        // the objects paint themselves
    }

    /**
     * listens for user keyboard/mouse
     * input and initiates action based
     * on input
     */
    private void generateUserInputActions () {
        if (getKey('N')) {
            clearKey('N');
            addNewAssembly();
        }
        if (getKey('C')) {
            clearKey('C');
            clearAssembly();
        }
        if (getKey('V')) {
            clearKey('V');
            EnvironmentVariables.toggleViscosity();

        }
        if (getKey('M')) {
            clearKey('M');
            EnvironmentVariables.toggleCenter();
        }
        if (getKey('G')) {
            clearKey('G');
            EnvironmentVariables.toggleGravity();
        }
        if (this.getKey(KeyMouse1)) {
            clearKey(KeyMouse1);
            generateUserCreatedMovementTool();
           
        }

    }

    /**
     * add a new Assembly
     */
    private void addNewAssembly () {
       
        fc.setCurrentDirectory(new File("./assets/data"));
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            reader.readAssembly(file);
          
        }
        
    }

    /**
     * clear Assembly in three places. JBox, JGame, and the AssemblyList in EnvironmentVaribles
     */
    private void clearAssembly () {

        int returnVal = JOptionPane.showConfirmDialog(null, "delete everything?");
        if (returnVal == JOptionPane.OK_OPTION) {
            List<Assembly> list = EnvironmentVariables.getAssemblyList();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).clear();
                
            }
            list.clear();
        }

    }
    /**
     * Helper function to find the PhysicalSolidObject in the simulation that is closest to the
     * created mass
     * 
     * @param userCreatedMass - the mass created at the location the user clicked
     * @return the closest PhysicalSolidObject
     */
    private Mass findClosestMass (PhysicalSolidObject userCreatedMass) {
        Mass closestSolidObject = null;
        double min = Double.POSITIVE_INFINITY;
        List<Assembly> assemblies = EnvironmentVariables.getAssemblyList();
        Iterator<Assembly> assembliesIterator =
                ((java.util.List<Assembly>) assemblies).iterator();

        while (assembliesIterator.hasNext()) {
            Iterator<Map.Entry<String, PhysicalSolidObject>> solidObjectsIterator =
                    assembliesIterator.next().getAllSolidObjects().entrySet().iterator();
            while (solidObjectsIterator.hasNext()) {
                PhysicalSolidObject solidObject = solidObjectsIterator.next().getValue();
                if (min > userCreatedMass.computeDistance(solidObject.x, solidObject.y)) {
                    min = userCreatedMass.computeDistance(solidObject.x, solidObject.y);
                    closestSolidObject = (Mass) solidObject;

                }

            }
        }
        return closestSolidObject;
    }

    /**
     * Listens for the user to click the mouse
     * When the user clicks the mouse
     * a Mass and Spring connected to the nearest mass
     * are created
     */
    private void generateUserCreatedMovementTool () {

        final int DEFAULT_MASS = 5;
        UserMovableMass userCreatedMass =
                new UserMovableMass("usermass", 2, JGColor.red, DEFAULT_MASS, getMouseX(),
                                    getMouseY(), null);
        PhysicalSolidObject object2 = findClosestMass(userCreatedMass);
        double distance = userCreatedMass.computeDistance(object2.x, object2.y);
        new Spring("spring", 0, JGColor.blue, object2, userCreatedMass, distance, 1);

    }

}
