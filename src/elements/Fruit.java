package elements;

import Server.fruits;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayList;
import java.util.Iterator;

public class Fruit {

	private double value;
	private int type;
	private Point3D pos;

	//constructors
	public Fruit(double v, int t, Point3D p)
	{
		this.value=v;
		this.type=t;
		this.pos=new Point3D(p);
	}

	public Fruit(Fruit f)
	{
		this.value= f.value;
		this.type=f.type;
		this.pos=new Point3D(f.pos);

	}

	//Default constructor
	public Fruit ()
	{
		this.value=0;
		this.type=0;
		this.pos=null;

	}



	public void initFruit(String fruit) {
		try {
			JSONObject obj1 = new JSONObject(fruit);
			JSONObject obj2 = obj1.getJSONObject("Fruit");
			this.type = obj2.getInt("type");
			this.value = obj2.getDouble("value");
			String pos = obj2.getString("pos");
			this.pos = new Point3D(pos);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	//getters and setters
	public void setVal(double v)
	{
		this.value=v;
	}

	public void setType(int t)
	{
		this.type= t;
	}

	public void setPos(Point3D p)
	{
		pos= new Point3D(p);
	}



	public double getVal() { return this.value; }

	public int getType() { return this.type; }

	public Point3D getPos() { return this.pos; }


}