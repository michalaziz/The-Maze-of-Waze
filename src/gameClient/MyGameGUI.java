package gameClient;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.*;
import elements.Robot;
import gui.Graph_GUI;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameClient.Auto_Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;
import utils.StdDraw;


public class MyGameGUI extends Thread{

	public DGraph d_g;
	public Graph_Algo algoGame;
	public static Graph_GUI gui;
	public String typeGame;
	public int scenario_num;
	public game_service game;
	public Auto_Game a_g=new Auto_Game();
	List<Fruit> fruits_arr;
	List<Robot> robots_arr;


	public MyGameGUI()
	{
		a_g=new Auto_Game();
		a_g.setMyGameGUI(this);
		selectGame();
	}



	public void selectGame()
	{

		String st = JOptionPane.showInputDialog(null, "Choose scenario number between 0-23 :");
		scenario_num = Integer.parseInt(st);
		if (scenario_num > 23 || scenario_num < 0)
			JOptionPane.showMessageDialog(null, "The number that you entered isn't a Scenario number ");
		else {
			String[] options = {"Manual", "Automtic"};
			int option = JOptionPane.showOptionDialog(null, "Select a game type", "game type"
					, JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			if(option==0)//manual
			{
				manualGame();
			}
			else//automatic
			{
				a_g.startGame(scenario_num);
				//autoGame();
			}
		}
	}

	static int ii=0;
	public void manualGame()
	{
		this.game = Game_Server.getServer(scenario_num);
		String graphGame = game.getGraph();
		DGraph d = new DGraph();
		d.init(graphGame);
		this.d_g = d;
		System.out.println(game.toString());
		fruits_arr = initFruits();
		robots_arr = initRobots();
		paint();
		addRobotManual();
		drawRobots();
		game.startGame();

		while(game.isRunning())
		{

			moveRobotManual();	
//			StdDraw.clear();
//			drawPoints();
//			drawEdges();
//			drawFruits();
//			drawRobots();
//			StdDraw.enableDoubleBuffering();
//			StdDraw.show();
		}


		//this.start();
	}

	public void paint() {
		setScale();
		drawPoints();
		drawEdges();
		drawFruits();
		StdDraw.show();
	}


	public void addRobotManual()
	{
		int rs=robors_size();
		int [] keyRobotsList=new int[rs];//Preparing array to the keys of the robots
		for(int i=0; i<rs; i++)//ask for robots location as the number of the robots in the scenario
		{
			String st = JOptionPane.showInputDialog(null, "choose key node to place robot number "+i);
			keyRobotsList[i]=Integer.parseInt(st);//put the key in the array
		}
		//for each key creating new robot and adding it to the robots_arr
		for(int i=0; i<keyRobotsList.length; i++)
		{
			node_data n=d_g.getNode(keyRobotsList[i]);
			robots_arr.add(new Robot(i,keyRobotsList[i],n.getLocation()));
			game.addRobot(keyRobotsList[i]);
		}

	}

	public void test ()
	{
		//StdDraw.enableDoubleBuffering();
		if(StdDraw.isMousePressed())
		{
			System.out.println("yes "+ ii);
			double x= StdDraw.mouseX();
			double y = StdDraw.mouseY();
			StdDraw.setPenRadius(0.02);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.point(x, y);
		}
	}


	public void moveRobotManual()
	{
		Robot closest=null;
		boolean isOk=false;

		double x=-100000,y=-100000;
		boolean flagSrc=false;

		node_data nDest=new Node();
		if(StdDraw.isMousePressed() && flagSrc==false)
		{
			System.out.println("enterde src");
			flagSrc=true;
			//StdDraw.enableDoubleBuffering();
			x=StdDraw.mouseX();
			y=StdDraw.mouseY();
			Point3D pSrc =new Point3D(x,y);
			closest = new Robot(findClosestRobot(pSrc));
			System.out.println(closest.getID());

		}


		if(closest!=null)//find the next to move
		{

			System.out.println("desttt");
			
			x=StdDraw.mouseX();
			y=StdDraw.mouseY();
			Point3D pDest =new Point3D(x,y);
			nDest=findClosestNode(pDest);	

			Iterator<edge_data> iter=d_g.getE(closest.getSrc()).iterator();

			while(iter.hasNext())
			{
				if(iter.next().getDest()==nDest.getKey())
				{
					//game.chooseNextEdge(closest.getID(),nDest.getKey());
					int id= closest.getID();
					int src= closest.getSrc();
					Robot r= new Robot(id, src, pDest);
					this.robots_arr.clear();
					System.out.println(robots_arr.size());
					this.robots_arr.add(r);
					StdDraw.clear();
					drawPoints();
					drawEdges();
					drawFruits();
					drawRobots();
					//StdDraw.enableDoubleBuffering();
					StdDraw.show();
					
					//closest.setPos(pDest);
					//StdDraw.enableDoubleBuffering();

					//StdDraw.picture(nDest.getLocation().x(), nDest.getLocation().y(), "rob.png", 0.0004, 0.0004);     
				}
			}
		}



		//		System.out.println(x);
		//		System.out.println(y);
		//		System.out.println(closest.getSrc());
		//		System.out.println(robots_arr.size());
	}
	//		node_data nDest=new Node();
	//		if(flagSrc==true)
	//		{
	//			StdDraw.enableDoubleBuffering();
	//			x=StdDraw.mouseX();
	//			y=StdDraw.mouseY();
	//			Point3D pDest =new Point3D(x,y);
	//			nDest=findClosestNode(pDest);	
	//
	//			Iterator<edge_data> iter=d_g.getE(closest.getSrc()).iterator();
	//
	//			while(iter.hasNext()&&isOk==false)
	//			{
	//				if(iter.next().getDest()==nDest.getKey())
	//				{
	//					game.chooseNextEdge(closest.getID(),nDest.getKey());
	//					isOk=true;
	//				}
	//			}
	//		}
	//		else
	//			JOptionPane.showMessageDialog(null, "try again dest");
	//		if(isOk==true)
	//		{
	//			StdDraw.picture(nDest.getLocation().x(), nDest.getLocation().y(), "rob.png", 0.0004, 0.0004);
	//			StdDraw.show();
	//		}





	public Robot findClosestRobot(Point3D pos)
	{
		Double min = Double.MAX_VALUE;
		Robot closest =null;

		for(int i=0; i<robots_arr.size(); i++)
		{
			double distance = robots_arr.get(i).getPos().distance3D(pos);
			if(distance<min)
			{
				min = distance;
				closest = robots_arr.get(i);
			}
		}

		return closest;
	}

	public node_data findClosestNode(Point3D pos)
	{
		Double min = Double.MAX_VALUE;
		node_data closest =null;
		Iterator<node_data> iter= d_g.getV().iterator();
		while(iter.hasNext())
		{
			node_data next = iter.next();
			double distance = next.getLocation().distance3D(pos);
			if(distance<min)
			{
				min = distance;
				closest=next;
			}
		}		
		return closest;
	}


	public int robors_size()
	{
		int rs=0;
		try {
			String x = game.toString();
			JSONObject l = new JSONObject(x);
			JSONObject ttt = l.getJSONObject("GameServer");
			rs= ttt.getInt("robots");

		}catch(Exception e) {e.printStackTrace();}
		return rs;
	}

	public void initRobots(String str)
	{
		try {
			JSONObject rlist = new JSONObject(str);
			JSONObject current= rlist.getJSONObject("Robot");
			int src = current.getInt("src");
			Object pos = current.get("pos");
			Point3D p=new Point3D(pos.toString());
			int id = current.getInt("id");
			int dest = current.getInt("dest");
			double value = current.getDouble("value");
			int speed = current.getInt("speed");
			Robot robot = new Robot(id, src, p);
			this.robots_arr.add(robot);
		}catch(Exception e) {e.printStackTrace();}
	}

	public List<Robot> initRobots() {
		List<String> rob = this.game.getRobots();
		List<Robot> ans = new ArrayList<>();
		Iterator<String> iter = rob.iterator();
		while (iter.hasNext()) {
			Robot r = new Robot();
			r.initRobot(iter.next());
			ans.add(r);
		}
		return ans;
	}

	public List<Fruit> initFruits() {

		List<String> fr = this.game.getFruits();
		List<Fruit> res = new ArrayList<Fruit>();
		Iterator<String> iter = fr.iterator();
		while (iter.hasNext()) {
			Fruit f = new Fruit();
			f.initFruit(iter.next());
			res.add(f);
		}
		return res;
	}


	/**
	 * the function gets Dgraph and key of the node that the robot placed on it
	 *
	 * @param g   is the Dgraph
	 * @param src key of the node that the robot placed on it
	 * @return the edge that the robot trip on her
	 */
	public int nextNode(DGraph g, int src) {
		int ans = -1;
		Collection<edge_data> edges = g.getE(src);
		Iterator<edge_data> itr = edges.iterator();
		int size = edges.size();
		int random = (int) (Math.random() * size); //
		int i = 0;
		while (i < random) // running until the the number of edge random is less than i
		{
			itr.next();
			i++;
		}
		ans = itr.next().getDest();  //its the edge that the robot trip on her
		return ans;
	}


	///////////////////////////////////////////////////draw_functions/////////////////////////////////////////////////////////////////
	public void drawRobots() {
		Iterator<Robot> robotIterator = this.robots_arr.iterator();
		while (robotIterator.hasNext()) {
			Robot current = robotIterator.next();
			double x = current.getPos().x();
			double y = current.getPos().y();
			StdDraw.picture(x, y, "rob.png", 0.0004, 0.0004);
		}
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

	public void drawEdges() {
		Iterator<node_data> iterN = this.d_g.getV().iterator();
		while (iterN.hasNext()) {
			node_data tempV = iterN.next();
			Collection<edge_data> edges = this.d_g.getE(tempV.getKey());
			if (edges == null)
				break;
			Iterator<edge_data> iterE = edges.iterator();
			while (iterE.hasNext()) {
				StdDraw.setPenRadius(0.003);
				StdDraw.setPenColor(StdDraw.GRAY);
				edge_data tempE = iterE.next();
				StdDraw.line(this.d_g.getNode(tempE.getSrc()).getLocation().x(),
						this.d_g.getNode(tempE.getSrc()).getLocation().y(),
						this.d_g.getNode(tempE.getDest()).getLocation().x(),
						this.d_g.getNode(tempE.getDest()).getLocation().y());
				StdDraw.setPenRadius(0.015);
				StdDraw.setPenColor(StdDraw.GREEN);
				StdDraw.point(((this.d_g.getNode(tempE.getSrc()).getLocation().x() +
						this.d_g.getNode(tempE.getDest()).getLocation().x() * 7) / 8),
						((this.d_g.getNode(tempE.getSrc()).getLocation().y()) +
								this.d_g.getNode(tempE.getDest()).getLocation().y() * 7) / 8);
			}
		}
	}

	public void setScale() {
		double x_min = Double.MAX_VALUE;
		Double x_max = Double.MIN_VALUE;
		Double y_min = Double.MAX_VALUE;
		Double y_max = Double.MIN_VALUE;

		Iterator<node_data> iter = this.d_g.getV().iterator();
		while (iter.hasNext()) {
			node_data currentNode = iter.next();
			if (currentNode.getLocation().x() < x_min) {
				x_min = (double) currentNode.getLocation().x();
			}
			if (currentNode.getLocation().x() > x_max) {
				x_max = (double) currentNode.getLocation().x();
			}
			if (currentNode.getLocation().y() < y_min) {
				y_min = (double) currentNode.getLocation().y();
			}
			if (currentNode.getLocation().y() > y_max) {
				y_max = (double) currentNode.getLocation().y();
			}
		}


		StdDraw.setCanvasSize(Math.abs((int) (x_min + x_max)) + 1250,
				Math.abs((int) (y_min + y_max)) + 550);
		StdDraw.setXscale(x_min - 0.0001, x_max + 0.0001);
		StdDraw.setYscale(y_min - 0.001, y_max + 0.001);

	}


	public void drawPoints() {
		Iterator<node_data> iter = this.d_g.getV().iterator();
		while (iter.hasNext()) {
			node_data temp = iter.next();
			StdDraw.setPenRadius(0.02);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.point(temp.getLocation().x(), temp.getLocation().y());
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius(0.001);
			StdDraw.text(temp.getLocation().x() - 0.00028, temp.getLocation().y(), "" + temp.getKey());
		}
	}


	public static void main(String args[]) {
		MyGameGUI gam = new MyGameGUI();
	}

	//	@Override
	//	public void run() {
	//
	//		this.game.startGame();
	//		int i=0;
	//		while(game.isRunning())
	//		{
	//			synchronized(this)
	//			{
	//				moveRobots(this.game,this.d_g);
	//				if(i%4==0)
	//					paint();
	//				i++;
	//			}
	//		}
	//		String ans = game.toString();
	//		System.out.println("Game Over: "+ ans);
	//
	//	}
}