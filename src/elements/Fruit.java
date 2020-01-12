package elements;

import Server.fruits;
import utils.Point3D;

public class Fruit {
	
	double value; 
	int type; 
	Point3D pos; 
	
	//ctor
	public Fruit(double v, int t, Point3D p)
	{
		value=v;
		type=t;
		pos=new Point3D(p);
	}
	
	public Fruit(Fruit f)
	{
		value= f.value;
		type=f.type;
		pos=new Point3D(f.pos);
	}
	
	//Default ctor
	public Fruit ()
	{
		value=0;
		type=0;
		pos=new Point3D(0,0,0);
	}
	
	public void setVal(double v)
	{
		value=v;
	}
	
	public void setType(int t)
	{
		type= t;
	}
	public void setPos(Point3D p)
	{
		pos= new Point3D(p);
	}
	
	public double getVal()
	{
		return value;
	}
	
	public int getType()
	{
		return type;
	}
	
	public Point3D getPos()
	{
		return pos;
	}
}
