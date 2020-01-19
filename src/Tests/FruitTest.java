package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.micromata.opengis.kml.v_2_2_0.Point;
import elements.Fruit;
import utils.Point3D;

class FruitTest {


	@Test
	void testSetVal() {
        Fruit f1 = new Fruit();
        Fruit f2 = new Fruit();
        Fruit f3 = new Fruit();
        
        f1.setVal(8);
        f2.setVal(0);
        f3.setVal(-1);
        
        assertEquals(8, f1.getVal());
        assertEquals(0, f2.getVal());
        assertEquals(-1, f3.getVal());
	}

	@Test
	void testSetType() {
		Fruit f1 = new Fruit();
		Fruit f2 = new Fruit();
		
		f1.setType(1);
		f2.setType(-1);
		
		assertEquals(1, f1.getType());
		assertEquals(-1, f2.getType());
		
	}

	@Test
	void testSetPos() {
        Fruit f1 = new Fruit();
        Fruit f2 = new Fruit();
        Fruit f3 = new Fruit();
        
        Point3D p1 = new Point3D(1,2,3);
        Point3D p2 = new Point3D(0,0,0);
        Point3D p3 = new Point3D(-1,-2,-3);
        
        f1.setPos(p1);
        f2.setPos(p2);
        f3.setPos(p3);
        
        assertEquals(p1, f1.getPos());
        assertEquals(p2, f2.getPos());
        assertEquals(p3, f3.getPos());
        
	}

	@Test
	void testGetVal() {
        Fruit f1 = new Fruit();
        Fruit f2 = new Fruit();
        Fruit f3 = new Fruit();
        
        f1.setVal(8);
        f2.setVal(0);
        f3.setVal(-1);
        
        assertEquals(8, f1.getVal());
        assertEquals(0, f2.getVal());
        assertEquals(-1, f3.getVal());
	}

	@Test
	void testGetType() {
		Fruit f1 = new Fruit();
		Fruit f2 = new Fruit();
		
		f1.setType(1);
		f2.setType(-1);
		
		assertEquals(1, f1.getType());
		assertEquals(-1, f2.getType());
	}

	@Test
	void testGetPos() {
        Fruit f1 = new Fruit();
        Fruit f2 = new Fruit();
        Fruit f3 = new Fruit();
        
        Point3D p1 = new Point3D(1,2,3);
        Point3D p2 = new Point3D(0,0,0);
        Point3D p3 = new Point3D(-1,-2,-3);
        
        f1.setPos(p1);
        f2.setPos(p2);
        f3.setPos(p3);
        
        assertEquals(p1, f1.getPos());
        assertEquals(p2, f2.getPos());
        assertEquals(p3, f3.getPos());
	}

	@Test
	void testGetVisit() {
        Fruit f1 = new Fruit();
        Fruit f2 = new Fruit();
        
        f1.setVisit(true);
        f2.setVisit(false);
        
        assertEquals(true, f1.getVisit());
        assertEquals(false, f2.getVisit());
	}

	@Test
	void testSetVisit() {
        Fruit f1 = new Fruit();
        Fruit f2 = new Fruit();
        
        f1.setVisit(true);
        f2.setVisit(false);
        
        assertEquals(true, f1.getVisit());
        assertEquals(false, f2.getVisit());
	}

}
