package jboxGlue.Factory;

import java.util.HashMap;

/*
 * This class manages different kinds of factory which allows us to access 
 * the correct specific factory by passing a string.
 * We think doing this instead of if statements is the right way because
 * first we know that all of these factory will be used. Also, this factory
 * only exists as a temporary variable in the reading process. It will not waste memory. 
 * It's more extensible. Everytime one adds a new type, it's really easy to create
 * it using this factory design pattern.
 *     
 */
/**
 * @author Jiaran
 * Factory of all concrete factories
 *
 */
public class AllFactories {
    /**
     *  How our string,factory pairs are stored
     */
    HashMap<String, Factory> factoryHashMap= new HashMap<String,Factory>();
    public AllFactories(){
        factoryHashMap.put("mass", new MassFactory());
        factoryHashMap.put("fixed", new FixedMassFactory());
        factoryHashMap.put("spring", new SpringFactory());
        factoryHashMap.put("muscle", new MuscleFactory());
        factoryHashMap.put("wall",  new WallFactory());
    }
    
    /**
     * @param name: the name of object one wants to create
     * @return: the correct concrete factory.
     */
    public Factory getFactory(String name){
        return factoryHashMap.get(name);
    }

}
