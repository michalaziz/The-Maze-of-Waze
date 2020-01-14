package elements;

import org.json.JSONObject;
import utils.Point3D;

public class Robot {

    private int src;
    private Point3D pos;
    private int id;
    private int _dest;
    private double value;
    private int speed;

    //constructors
    public Robot(int src, Point3D p, int id, int de, double v, int speed)
    {
        this.src=src;
        this.pos=new Point3D(p);
        this.id=id;
        this._dest =de;
        this.value=v;
        this.speed= speed;

    }

    public Robot(Robot r)
    {
        this.src=r.src;
        this.pos=new Point3D(r.pos);
        this.id=r.id;
        this._dest =r._dest;
        this.value=r.value;
        this.speed=r.speed;
    }

    //Default constructor
    public Robot(){}

    
    
    public void initRobot(String string) {

        try {
            JSONObject obj1 = new JSONObject(string);
            JSONObject obj2 = obj1.getJSONObject("Robot");
            this.id = obj2.getInt("id");
            this._dest = obj2.getInt("dest");
            this.src = obj2.getInt("src");
            this.speed = obj2.getInt("speed");
            String pos = obj2.getString("pos");
            String[] loc = pos.split(",");
            this.pos = new Point3D(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void set_dest(int d)
    {
        this._dest =d;
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

    public int get_dest()
    {
        return this._dest;
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