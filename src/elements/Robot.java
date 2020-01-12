package elements;

import Server.robot;
import utils.Point3D;

public class Robot {

	int src;
	Point3D pos; 
	int id;
	int dest;
	int value;
	int speed; 

	public Robot(int src, Point3D p, int id, int dest, int v, int speed)
	{
		src=src; 
		pos=new Point3D(p);
		id=id;
		dest=dest;
		value=v;
		speed= speed;
	}

	public Robot(Robot r)
	{
		src=r.src;
		pos=new Point3D(r.pos);
		id=r.id;
		dest=r.dest;
		value=r.value;
		speed=r.speed;
	}

	//Default ctor
	public Robot()
	{

	}

	public void setSrc(int s)
	{
		src=s;
	}

	public void setPos(Point3D p)
	{
		pos=new Point3D(p);
	}

	public void setID(int id)
	{
		id=id;
	}

	public void setDest(int d)
	{
		dest=d;
	}

	public void setVal(int val)
	{
		value=val;
	}
	public void setSpeed(int s)
	{
		speed =s;
	}

	public int getSrc()
	{
		return src;
	}

	public Point3D getPos()
	{
		return pos;
	}

	public int getID()
	{
		return id;
	}

	public int getDest()
	{
		return dest;
	}

	public int getVal()
	{
		return value;
	}

	public int getSpeed()
	{
		return speed;
	}


}
