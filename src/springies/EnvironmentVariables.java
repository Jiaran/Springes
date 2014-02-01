package springies;

import jboxGlue.Link.MuscleWave;
import org.jbox2d.common.Vec2;
import java.util.ArrayList;
import java.util.List;
import jboxGlue.Assembly;


/*
 * Environment Variables class is a
 * static class handle everything related
 * to global environment.
 * 
 * Because we use package to somehow control access,
 * most of the class can only call its getters(public)
 * except the reader
 * 
 * ReadData Class which reads environment
 * variables can use its setters(package access).
 * 
 * We know that STATIC class is not recommended in
 * this class. However, following the advice of
 * "playing with design". We do find this static class
 * is reasonable for the following reason.
 * First:
 * we design it in a way that only the reader and initialize
 * part of the program has access to its setter. Objects in
 * the world just need to read from it. Kind of like a constant class.
 * Second:
 * There is only ONE world in this program. Most of the object shares
 * the same gravity, viscosity, center of mass parameter. One cannot
 * argue that for this mass, the gravity is 1, for another it's 2.
 * 
 * We also come up with two ways to avoid this static class and they are
 * not suitable for our concern so that we list them as the reason why
 * we choose this design.
 * Third(first substitute):
 * Passing it as a parameter to all the masses. That's a bad idea because
 * there should be only one copy of environment and environment dominates
 * the mass not the opposite.
 * Fourth(second substitute):
 * Create a control class. It have access to masses and apply force to it(eg, viscosity force).
 * At the first glance it is good. And it is more extensible. But it is flawed for two reasons.
 * Conceptually, it feels not right. The viscosity and gravity etc,
 * is like a property of the world. It's not like a unexplainable force acts on our masses, instead
 * every mass indulge in our world should apply to the world's rule.
 * Also, if do it this way, one can initialize several classes like this to apply forces. It doesn't
 * feel right. So we put the application of these forces into the objects and let themselves to
 * handle
 * it so that our client will not be botherred.
 */

/**
 * Class that handles Environment
 * 
 */
public class EnvironmentVariables {
    private static double myViscosity;
    private static double centermass_magnitude;
    private static double centermass_exponent;
    private static MuscleWave myMuscleWave;

    // track our assembly and thus all the objects. JGame tracks the objects
    // but some of its mechanism is not explicit. So we do this on our own
    private static List<Assembly> assemblyList = new ArrayList<Assembly>();
    private static boolean gravityIsON;
    private static boolean centerIsON;
    private static boolean viscosityIsON;
    private static double gravityMagnitude;
    private static double gravityDirection;

    static {
        myMuscleWave = new MuscleWave();
    }

    /**
     * @param amplitude : the amp of the muscle who calls this
     * @param phase : the phase of the muscle who calls this( for extensibility)
     * @return the currentRestLength for the muscle
     */
    public static double getMuscleWave (double amplitude, double phase) {
        return myMuscleWave.getSpringLength(amplitude, phase);
    }

    /**
     * if center is not on, return 0
     * 
     * @return center mass magnitude
     */
    public static double getCenterMassMagnitude () {
        if (centerIsON) {
            return centermass_magnitude;
        }
        else {
            return 0;
        }

    }

    /**
     * @param magnitude : the value of center mass magnitude
     */
    public static void setCenterMassMagnitude (double magnitude) {
        centermass_magnitude = magnitude;
        centerIsON = true;
    }

    /**
     * @return : get exponent value for center mass
     */
    public static double getCenterMassExponent () {

        return centermass_exponent;

    }

    /**
     * @param exponent : set exponent value for center mass
     */
    protected static void setCenterMassExponent (double exponent) {
        centermass_exponent = exponent;
    }

    /**
     * is viscosity is not on, return 0;
     * 
     * @return get viscosity value for center mass
     */
    public static double getViscosity () {
        if (viscosityIsON)
            return myViscosity;
        else return 0.0;
    }

    /**
     * @param viscosity return viscosity value for center mass
     */
    protected static void setViscosity (double viscosity) {
        myViscosity = viscosity;
        viscosityIsON = true;
    }

    /**
     * compute the gravity according to magnitude and direction
     * 
     * @param magnitude : magnitude of gravity
     * @param direction : direction of gravity
     */
    protected static void setGravity (double magnitude, double direction) {
        float fx = (float) (magnitude * Math.cos(Math.toRadians(direction)));
        float fy = (float) (magnitude * Math.sin(Math.toRadians(direction)));
        WorldManager.getWorld().setGravity(new Vec2(fx, fy));

    }

    /**
     * initialize the gravity according to magnitude and direction
     * and set it
     * 
     * @param magnitude : magnitude of gravity
     * @param direction : direction of gravity
     */
    protected static void initGravity (double magnitude, double direction) {
        gravityIsON = true;
        gravityMagnitude = magnitude;
        gravityDirection = direction;
        setGravity(magnitude, direction);

    }

    /**
     * @param newAssembly: new assembly added
     */
    protected static void addToAssemblyList (Assembly newAssembly) {
        assemblyList.add(newAssembly);
    }

    /**
     * return the assemblyList
     * 
     * @return assemblyList
     */
    public static List<Assembly> getAssemblyList () {
        return assemblyList;
    }

    /**
     * Turn gravity on/off
     */
    protected static void toggleGravity () {
        if (gravityIsON) {
            setGravity(0, 0);
            gravityIsON = false;
        }
        else {
            setGravity(gravityMagnitude, gravityDirection);
            gravityIsON = true;
        }

    }

    /**
     * Turn viscosity on/off
     */
    protected static void toggleViscosity () {
        viscosityIsON = !viscosityIsON;

    }

    /**
     * Turn Center of Mass on/off
     */
    protected static void toggleCenter () {
        centerIsON = !centerIsON;

    }
}
