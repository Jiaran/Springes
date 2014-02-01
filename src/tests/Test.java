package tests;

import java.util.HashMap;
import jboxGlue.Assembly;
import jboxGlue.Factory.AllFactories;
import jboxGlue.Factory.Factory;
import jboxGlue.Factory.FixedMassFactory;
import jboxGlue.Factory.MassFactory;
import jboxGlue.Factory.MuscleFactory;
import jboxGlue.Factory.SpringFactory;
import jboxGlue.Factory.WallFactory;
import jboxGlue.Link.Spring;
import jboxGlue.Node.Mass;
import jgame.JGColor;
import jgame.platform.JGEngine;
import junit.framework.TestCase;
import springies.Springies;
import springies.WorldManager;

public class Test extends TestCase {

    protected void setUp () throws Exception {
        super.setUp();
        JGEngine eng = new Springies(); 
        WorldManager wm = new WorldManager(); 
        wm.initWorld(eng); 
        wm.getWorld(); 
    }

    protected void tearDown () throws Exception  {
        super.tearDown();
    }
    
    public void testFactories (){
        AllFactories factories= new AllFactories();
        Factory factory= factories.getFactory("mass"); 
        assertEquals( true,factory instanceof MassFactory );
        factory= factories.getFactory("spring"); 
        assertEquals( true,factory instanceof SpringFactory );
        factory= factories.getFactory("fixed"); 
        assertEquals( true,factory instanceof FixedMassFactory );
        factory= factories.getFactory("muscle"); 
        assertEquals( true,factory instanceof MuscleFactory );
        factory= factories.getFactory("wall"); 
        assertEquals( true,factory instanceof WallFactory );
        
    }
    
    public void testMassFactory(){
       
        Assembly a= new Assembly();
        MassFactory mf= new MassFactory();
        HashMap<String,String> information= new HashMap<String,String>();
        information.put("id", "1");
        information.put("mass", "1");
        information.put("x", "10");
        information.put("y", "10");
        information.put("vx", "11");
        information.put("vy", "12");
        // no assembly no creating
        mf.create(information, null);
        assertEquals( null,a.getObject("1") );
        
        mf.create(information, a);
        // the mas that should be created
        Mass newMass =
                new Mass("1", 2, JGColor.yellow, 1, 10, 10, (float) 11, (float) 12,
                         a);
        assertEquals( newMass,a.getObject("1") );
    }
    
    public void testMuscleFactory(){
        
        Assembly a= new Assembly();
        SpringFactory mf= new SpringFactory();
        HashMap<String,String> information= new HashMap<String,String>();
        information.put("constant", "1");     
        information.put("restlength", "1");
        information.put("magintude", "1");
        information.put("a", "1");
        information.put("b", "2");
        mf.create(information, null);
        assertEquals( null,a.getSpring(0) );
        //no mass no creating.
        mf.create(information, a);
        assertEquals( null,a.getSpring(0) );
        //creat masses
        createMassInAssembly(a);
        mf.create(information, a);
        // spring should be created
        Spring spring=new Spring(null, 0, JGColor.blue, a.getObject("1"),
                                 a.getObject("2"), 1, 1);
        assertEquals( spring,a.getSpring(0) );
        
    }
    
    private void createMassInAssembly( Assembly a){
        MassFactory mf= new MassFactory();
        HashMap<String,String> information1= new HashMap<String,String>();
        information1.put("id", "1");
        information1.put("mass", "1");
        information1.put("x", "10");
        information1.put("y", "10");
        information1.put("vx", "11");
        information1.put("vy", "12");
        mf.create(information1, a);
        HashMap<String,String> information2= new HashMap<String,String>();
        information2.put("id", "2");
        information2.put("mass", "2");
        information2.put("x", "7");
        information2.put("y", "10");
        information2.put("vx", "11");
        information2.put("vy", "12");
        mf.create(information2, a);
    }
}
