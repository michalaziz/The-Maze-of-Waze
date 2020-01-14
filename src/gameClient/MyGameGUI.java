package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.*;
import elements.Robot;
import gui.Graph_GUI;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;
import utils.StdDraw;


public class MyGameGUI  {

	public DGraph d_g ;
	public Graph_Algo algoGame;//???
	public static Graph_GUI gui = new Graph_GUI();
	public String typeGame;//???
	public int scenario_num=2;
	public game_service game;//???
	List<Fruit> fruits_arr=new ArrayList<Fruit>();
	List<Robot> robots_arr=new ArrayList<Robot>();
	Thread gameThread;

	public MyGameGUI(){
		
		String[] options = {"Manual", "Automtic"};
		int o=JOptionPane.showOptionDialog(null, "Select a game type", "game type"
				,JOptionPane.DEFAULT_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		switch(o)
		{
		case 0:
			manual();
			break;
		case 1:
			automatic();
			break;
		}
		game = Game_Server.getServer(scenario_num);
		initNodes();
		initEdges();
		fruits_arr=initFruits();
		robots_arr=initRobots();
		paint();

	}

	public void paint() {

		StdDraw.clear();
		gui.initGUI();
		drawRobots();
		drawFruits();

	}


	public void initNodes()
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



	public void initEdges()
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


	public List<Robot> initRobots( ){
		List<String> rob= this.game.getRobots();
		List<Robot>ans=new ArrayList<>();
		Iterator<String> iter=rob.iterator();
		while (iter.hasNext())
		{
			Robot r= new Robot();
			r.initRobot(iter.next());
			ans.add(r);
		}
		return ans;
	}

	public List<Fruit> initFruits( ) {

		List<String> fr= this.game.getFruits();
		List<Fruit> res= new ArrayList<Fruit>();
		Iterator<String> iter= fr.iterator();
		while (iter.hasNext())
		{
			Fruit f= new Fruit();
			f.initFruit(iter.next());
			res.add(f);
		}
		return res;
	}


	public void drawFruits() {
		Iterator<Fruit> iter = this.fruits_arr.iterator();
		while (iter.hasNext()) {
			Fruit current = iter.next();
			if (current.getType() == 1) {
				StdDraw.picture(current.getPos().x(), current.getPos().y(), "apple.png", 0.00048, 0.00048);
			} else {
				StdDraw.picture(current.getPos().x(), current.getPos().y(), "banana.png", 0.00048, 0.00048);
			}
		}
	}

	public void drawRobots()
	{
		Iterator<Robot> robotIterator=this.robots_arr.iterator();
		while(robotIterator.hasNext())
		{
			Robot current = robotIterator.next();
			double x = current.getPos().x();
			double y = current.getPos().y();
			StdDraw.picture(x,y,"rob.png",0.0004,0.0004);
		}

	}
	//    public void addRobot(Robot r )// by click
	//    {
	//
	//
	//    }




	public void moveRobots(game_service game, DGraph gg) {
		List<String> log = game.move();
		if (log != null) {
			long time = game.timeToEnd();
			for (int i = 0; i < log.size(); i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject robotM = line.getJSONObject("Robot");
					int key = robotM.getInt("id");
					int src = robotM.getInt("src");
					int desti = robotM.getInt("dest");

					if (desti == -1) { // the robot on node
						desti = nextNode(gg, src);
						game.chooseNextEdge(key, desti);
						System.out.println("Turn to node: " + desti + "  time to end:" + (time / 1000));
						System.out.println(robotM);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}



	/**
	 * the function gets Dgraph and key of the node that the robot placed on it
	 * @param g is the Dgraph
	 * @param src key of the node that the robot placed on it
	 * @return  the edge that the robot trip on her
	 */
	public int nextNode(DGraph g, int src) {
		int ans = -1;
		Collection<edge_data> edges = g.getE(src);
		Iterator<edge_data> itr = edges.iterator();
		int size = edges.size();
		int random = (int)(Math.random()*size); //
		int i=0;
		while(i<random) // running until the the number of edge random is less than i
		{
			itr.next();
			i++;
		}
		ans = itr.next().getDest();  //its the edge that the robot trip on her
		return ans;
	}

	public void automatic()
	{
		System.out.println("auto");
	}
	
	public void manual()
	{
		System.out.println("manual");
	}




	public static void main(String args[])
	{

		MyGameGUI game= new MyGameGUI();

	}

}