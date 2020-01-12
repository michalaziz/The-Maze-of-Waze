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
    private String pic="";

    //constructors
    public Fruit(double v, int t, Point3D p, String pic)
    {
        this.value=v;
        this.type=t;
        this.pos=new Point3D(p);
        this.pic=pic;
    }

    public Fruit(Fruit f)
    {
        this.value= f.value;
        this.type=f.type;
        this.pos=new Point3D(f.pos);
        this.pic=f.pic;
    }

    //Default constructor
    public Fruit ()
    {
        this.value=0;
        this.type=0;
        this.pos=null;
        this.pic=pic;
    }

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

    public void setPic(String pic){
        this.pic=pic;
    }

    public double getVal() { return this.value; }

    public int getType() { return this.type; }

    public Point3D getPos() { return this.pos; }

    public String getPic() { return this.pic; }

         }