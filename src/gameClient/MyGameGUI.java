package gameClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import elements.Edge;
import elements.Fruit;
import elements.Node;
import elements.Robot;
import gui.Graph_GUI;
import utils.Point3D;

public class MyGameGUI  {

	public static DGraph d_g = new DGraph();
	public static Graph_GUI gui = new Graph_GUI();


	static int  scenario_num = 20;
	static game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games


	public static void initNodes()
	{
		try {
			String g = game.getGraph();
			JSONObject line= new JSONObject(g);
			JSONArray nodes = line.getJSONArray("Nodes");
			for(int i=0; i<nodes.length(); i++)
			{
				JSONObject current= nodes.getJSONObject(i);
				int key= current.getInt("id");
				Object pos = current.get("pos");
				Point3D p=new Point3D(pos.toString());
				Node n = new Node(key,p);
				gui.get_graph().addNode(n);
				
			}


		}catch(Exception e){e.printStackTrace();}

	}

	public static void initEdges()
	{
		try {
			String g = game.getGraph();
			JSONObject line= new JSONObject(g);
			JSONArray edges = line.getJSONArray("Edges");
			for(int i=0; i<edges.length(); i++)
			{
				JSONObject current= edges.getJSONObject(i);
				int src= current.getInt("src");
				double weigth = current.getDouble("w");
				int dest = current.getInt("dest");
				gui.get_graph().connect(src, dest, weigth);
			}

		}catch(Exception e){e.printStackTrace();}

	}

	public static void initFruit()
	{

		try {
			String fruitList = game.getFruits().toString();
			JSONArray flist = new JSONArray(fruitList);
			for(int i=0; i<flist.length(); i++)
			{
				JSONObject current = flist.getJSONObject(i);
				JSONObject fr= current.getJSONObject("Fruit");
				double val= fr.getDouble("value");
				int t= fr.getInt("type");
				Object pos = fr.get("pos");
				Point3D p=new Point3D(pos.toString());
				Fruit fruit = new Fruit(val, t, p,"");
				gui.get_graph().addFruit(fruit);
			}

		}catch(Exception e){e.printStackTrace();}
	}


	public static void initRobot()
	{
		try {
		String robotList= game.getRobots().toString();
		JSONArray rlist = new JSONArray(robotList);
		for(int i=0; i<rlist.length(); i++)
		{
			JSONObject current = rlist.getJSONObject(i);
			int src = current.getInt("src");
			Object pos = current.get("pos");
			Point3D p=new Point3D(pos.toString());
			int id = current.getInt("id");
			int dest = current.getInt("dest");
			double value = current.getDouble("value");
			int speed = current.getInt("speed");
			Robot robot = new Robot(src, p, id, dest, value, speed);
			gui.get_graph().addRobot(robot);
		}
		}catch(Exception e) {e.printStackTrace();}
	}
	
	public static void drawGraph()
	{
		
		initNodes();
		initEdges();
		initFruit();
		initRobot();
		gui.initGUI();
	}


	public static void main(String args[])
	{
		drawGraph();
	}

}
