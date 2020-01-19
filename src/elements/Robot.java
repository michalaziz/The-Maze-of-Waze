package elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import utils.Point3D;

public class Robot {

    private int src;
    private Point3D pos;
    private int id;
    private double value;
    private int speed;
	private int dest;

    //constructors
    public Robot(int src,int dest, Point3D p, int id, double v, int speed)
    {
        this.src=src;
        this.pos=new Point3D(p);
        this.id=id;
        this.value=v;
        this.speed= speed;
        this.dest=dest;

    }
    
    public Robot(int id, int src, Point3D p)
    {
        this.src=src;
        this.pos=new Point3D(p);
        this.id=id;

    }

    public Robot(Robot r)
    {
        this.src=r.src;
        this.pos=new Point3D(r.pos);
        this.id=r.id;
        this.value=r.value;
        this.speed=r.speed;
    }

    //Default constructor
    public Robot(){
    	this.id=0;
    	this.pos=null;
    	this.src=0;
    	this.dest=0;
    }

    public void initRobot(String str){
        try {
            JSONObject obj1 = new JSONObject(str);
            JSONObject obj2 = obj1.getJSONObject("Robot");
            this.id = obj2.getInt("id");
            this.src = obj2.getInt("src");
            this.dest = obj2.getInt("dest");
            String pos = obj2.getString("pos");
            this.pos = new Point3D(pos);
        }catch (Exception e){e.printStackTrace();}

    }
    
    public Robot initRobotJson(String str) {
    	Robot temp = new Robot();

        try {
            JSONObject obj1 = new JSONObject(str);
            JSONObject obj2 = obj1.getJSONObject("Robot");
            temp.id = obj2.getInt("id");
            temp.src = obj2.getInt("src");
            temp.dest = obj2.getInt("dest");
            String pos = obj2.getString("pos");
            temp.pos = new Point3D(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    
	public ArrayList<Robot> initRobots(List<String>robotList) {
		ArrayList<Robot> ans = new ArrayList<>();
		for(String r:robotList)
			ans.add(initRobotJson(r));
		return ans;
	}

    // getters and setters

    public void setSrc(int s)
    {
        this.src=s;
    }

    public void setPos(Point3D p)
    {
       this.pos=new Point3D(p);
    }

    public void setID(int id)
    {
        this.id=id;
    }

    public void setVal(int val)
    {
        this.value=val;
    }

    public void setSpeed(int s)
    {
        this.speed =s;
    }


    public int getSrc()
    {
        return this.src;
    }

    public Point3D getPos()
    {
        return this.pos;
    }

    public int getID()
    {
        return this.id;
    }

    public double getVal()
    {
        return this.value;
    }

    public int getSpeed()
    {
        return this.speed;
    }

}