package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import elements.Robot;
import utils.Point3D;

class RobotTest {

	static String str = "{\"Robot\":{\"id\":1,\"value\":7,\"src\":8,\"dest\":9,\"speed\":1,\"pos\":\"35.701,32.104,0.0\"}}";
	
	
	@Test
	void testInitRobotJson() {
        Robot r = new Robot();
        Point3D obj1 = new Point3D(35.701,32.104);
        Robot obj2 = r.initRobotJson(str);
        assertEquals(8,obj2.getSrc());
        assertEquals(1,obj2.getID());
        assertEquals(0,obj2.getVal());        
        assertEquals(9,obj2.getDest());
        assertEquals(obj1,obj2.getPos());
	}


	@Test
	void testSetSrc() {
		Robot r1 =new Robot();
		Robot r2 =new Robot();
		Robot r3 =new Robot();
		
		r1.setSrc(8);
		r2.setSrc(-1);
		r3.setSrc(0);
		
		assertEquals(8, r1.getSrc());
		assertEquals(-1, r2.getSrc());
		assertEquals(0, r3.getSrc());
	}

	@Test
	void testSetPos() {
		Robot r1 = new Robot();
		Robot r2 = new Robot();
		Robot r3 = new Robot();
		
		Point3D p1 = new Point3D(1,2,3);
		Point3D p2 = new Point3D(0,0,0);
		Point3D p3 = new Point3D(-1,-2,-3);
		
		r1.setPos(p1);
		r2.setPos(p2);
		r3.setPos(p3);
		
		assertEquals(p1, r1.getPos());
		assertEquals(p2, r2.getPos());
		assertEquals(p3, r3.getPos());
	}

	@Test
	void testSetID() {
		Robot r1 =new Robot();
		Robot r2 =new Robot();
		Robot r3 =new Robot();
		
		r1.setID(8);
		r2.setID(-1);
		r3.setID(0);
		
		assertEquals(8, r1.getID());
		assertEquals(-1, r2.getID());
		assertEquals(0, r3.getID());
	}

	@Test
	void testSetVal() {
		Robot r1 =new Robot();
		Robot r2 =new Robot();
		Robot r3 =new Robot();
		
		r1.setVal(8);
		r2.setVal(-1);
		r3.setVal(0);
		
		assertEquals(8, r1.getVal());
		assertEquals(-1, r2.getVal());
		assertEquals(0, r3.getVal());
	}

	@Test
	void testSetSpeed() {
		Robot r =new Robot();
		
		r.setSpeed(1);
		
		assertEquals(1, r.getSpeed());
	}
	
	@Test
	void testSetDest() {
		Robot r1 =new Robot();
		Robot r2 =new Robot();
		Robot r3 =new Robot();
		
		r1.setDest(8);
		r2.setDest(0);
		r3.setDest(-1);
		
		assertEquals(8, r1.getDest());
		assertEquals(0, r2.getDest());
		assertEquals(-1, r3.getDest());
	}

	@Test
	void testGetSrc() {
		Robot r1 =new Robot();
		Robot r2 =new Robot();
		Robot r3 =new Robot();
		
		r1.setSrc(8);
		r2.setSrc(-1);
		r3.setSrc(0);
		
		assertEquals(8, r1.getSrc());
		assertEquals(-1, r2.getSrc());
		assertEquals(0, r3.getSrc());
	}

	@Test
	void testGetPos() {
		Robot r1 = new Robot();
		Robot r2 = new Robot();
		Robot r3 = new Robot();
		
		Point3D p1 = new Point3D(1,2,3);
		Point3D p2 = new Point3D(0,0,0);
		Point3D p3 = new Point3D(-1,-2,-3);
		
		r1.setPos(p1);
		r2.setPos(p2);
		r3.setPos(p3);
		
		assertEquals(p1, r1.getPos());
		assertEquals(p2, r2.getPos());
		assertEquals(p3, r3.getPos());
	}

	@Test
	void testGetID() {
		Robot r1 =new Robot();
		Robot r2 =new Robot();
		Robot r3 =new Robot();
		
		r1.setID(8);
		r2.setID(-1);
		r3.setID(0);
		
		assertEquals(8, r1.getID());
		assertEquals(-1, r2.getID());
		assertEquals(0, r3.getID());
	}

	@Test
	void testGetVal() {
		Robot r1 =new Robot();
		Robot r2 =new Robot();
		Robot r3 =new Robot();
		
		r1.setVal(8);
		r2.setVal(-1);
		r3.setVal(0);
		
		assertEquals(8, r1.getVal());
		assertEquals(-1, r2.getVal());
		assertEquals(0, r3.getVal());
	}

	@Test
	void testGetSpeed() {
		Robot r =new Robot();
		
		r.setSpeed(1);
		
		assertEquals(1, r.getSpeed());
	}
	
	@Test
	void testGetDest() {
		Robot r1 =new Robot();
		Robot r2 =new Robot();
		Robot r3 =new Robot();
		
		r1.setDest(8);
		r2.setDest(0);
		r3.setDest(-1);
		
		assertEquals(8, r1.getDest());
		assertEquals(0, r2.getDest());
		assertEquals(-1, r3.getDest());
	}


}
