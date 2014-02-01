package jboxGlue.Factory;

import java.util.HashMap;
import jboxGlue.Assembly;


/*
 * The Factory class is the super class for different factory in which
 * create must be implemented. To create an object, as we are reading
 * xml file where everything is string, client needs to pass a hash map.
 * The hashmap store the attribution name as the key and its value as
 * the value so that create can use that to determine the parameters to
 * create the objects. The other function decide how to change the string
 * to a valid value we want.
 * So in the class handles reading, what we need to do is to obtain a Factory
 * and call create, letting the polymorphism to handle which objects to
 * create according to the specific Factory type.
 * 
 * We make this design because in this project,the information used to create
 * an object can be so different. Eg, for wall, exponent, maginitude, id but
 * for mass its x,y,vx,vy,mass. So we use factory method pattern to let our 
 * subclasses to handle the concrete creating( because they can be so different)
 * Then one flaw of factory method is that we have to know the concrete type but
 * that's exactly what we don't want to do. A good fix is combining this with
 * abstract factory pattern. We wrap all these concrete class into a hash map.
 * The client reading the xml could just pass the correct name to get the correct
 * concrete type. The readData part is totally closed.
 * Also, because some of the objects created can have inheritance structure. We have
 * a readInformation function to take advantages of the inheritance. For example,in spring,
 * readInformation read all the basic information for a spring. In muscle, what we need to 
 * do is call super.readInformation and read the additional information( amplitude). By using
 * this inheritance of concrete factory, we can avoid lots of duplicated code.
 */
/**
 * @author Jiaran
 * Base class for all concrete factories.
 *
 */
public abstract class Factory {

    /**
     * @param information : information needed to create
     * @param assembly: which assembly to put the object.
     */
    public abstract  void create (HashMap<String, String> information, Assembly assembly);

    /**
     * as we read string as value, sometimes we need to convert that string into
     * the format we want. One can also implement other conversion function to
     * read the information. For example, in Mass, when its mass field in the node
     * is null or empty. Then return a default value. This conversion function can 
     * deal with the messness.
     * @param str: the string to be converted
     * @return: value
     */
    protected abstract double StringtoDouble (String str);
    protected boolean readInformation(HashMap<String, String> information){
        return true;
    }

}
