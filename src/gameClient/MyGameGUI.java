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
	public Robot r= new Robot();


	public MyGameGUI() throws JSONException
	{
		a_g=new Auto_Game();
		a_g.setMyGameGUI(this);
		selectGame();
	}



	public void selectGame() throws JSONException
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

	public void manualGame() throws JSONException
	{
		this.game = Game_Server.getServer(scenario_num);
		String graphGame = game.getGraph();
		DGraph d = new DGraph();
		d.init(graphGame);
		this.d_g = d;
		System.out.println(game.toString());
		fruits_arr = initFruits();
		paint();

		addRobotManual();

		List<String> robListManual = game.getRobots();
		robots_arr=r.initRobots(robListManual);

		drawRobots();
		StdDraw.show();
		//		
		game.startGame();
		//
		//		this.start();
		while(game.isRunning())
		{
			//            updateFruit();
			//            updateRobots();
			moveRobotManual();
			StdDraw.clear();
			StdDraw.enableDoubleBuffering();
			updatGraph();
			StdDraw.show();

		}


		//this.start();
	}


	public void updatGraph() throws JSONException
	{
		String fruitList = game.getFruits().toString();
		String robotList = game.getRobots().toString();

		fruits_arr.clear();
		JSONArray f = new JSONArray(fruitList);
		for(int i=0; i<f.length(); i++) {
			JSONObject current = f.getJSONObject(i);
			JSONObject current2 = current.getJSONObject("Fruit");
			int type = current2.getInt("type");
			double value = current2.getDouble("value");
			Object pos = current2.get("pos");
			Point3D p = new Point3D(pos.toString());
			Fruit fu = new Fruit(value, type, p);
			fruits_arr.add(fu);
		}


		robots_arr.clear();
		JSONArray r = new JSONArray(robotList);
		for(int j=0; j<r.length(); j++) {
			JSONObject line = r.getJSONObject(j);
			JSONObject robotline = line.getJSONObject("Robot");
			int id = robotline.getInt("id");
			double value1 = robotline.getDouble("value");
			int src = robotline.getInt("src");
			int dest = robotline.getInt("dest");
			int speed = robotline.getInt("speed");
			Object pos1 = robotline.get("pos");
			Point3D point = new Point3D(pos1.toString());
			Robot ro = new Robot(src, dest, point, id, value1, speed);
			robots_arr.add(ro);
			game.addRobot(ro.getSrc());
			StdDraw.picture(ro.getPos().x(), ro.getPos().y(),"rob.png",0.0004,0.0004);
		}

		drawEdges();
		drawPoints();
		drawFruits();
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
			//			node_data n=d_g.getNode(keyRobotsList[i]);
			//			robots_arr.add(new Robot(i,keyRobotsList[i],n.getLocation()));
			game.addRobot(keyRobotsList[i]);
		}

	}



	int RobotId=0;

	public void moveRobotManual()
	{

		Robot closest =null;
		List<String> log = game.move();
		double x=0,y=0;
		if(log!=null)
		{
			long time= game.timeToEnd();
			for(int i=0; i<log.size(); i++)
			{
				String rj=log.get(i);
				try {
					JSONObject line = new JSONObject(rj);
					JSONObject tomove = line.getJSONObject("Robot");
					int robID = tomove.getInt("id");
					int src = tomove.getInt("src");
					int dest = tomove.getInt("dest");
					if(StdDraw.isMousePressed())
					{
						x=StdDraw.mouseX();
						y=StdDraw.mouseY();
						closest=findClosestRobot(x,y);
						while(closest==null)
						{
							x=StdDraw.mouseX();
							y=StdDraw.mouseY();
							closest=findClosestRobot(x,y);
						}
						RobotId=closest.getID();
					}
					
					if(StdDraw.isMousePressed())
					{
						x=StdDraw.mouseX();
						y=StdDraw.mouseY();
						node_data nDest=findClosestNode(x, y);
						//            			edge_data p = d_g.getEdge(src, nDest.getKey());
						if(dest==-1)
							game.chooseNextEdge(RobotId, nDest.getKey());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateFruit()
	{
		List<String> arrf= game.getFruits();
		if(arrf!=null)
			for(int i=0; i<fruits_arr.size(); i++)
				fruits_arr.get(i).initFruit(arrf.get(i));
	}

	public void updateRobots(){
		List<String> robots = game.getRobots();
		if(robots!=null){
			System.out.println(this.robots_arr.size());
			for (int i = 0; i <this.robots_arr.size() ; i++) {
				this.robots_arr.get(i).initRobot(robots.get(i));
			}
		}
	}

	//
	//		node_data nDest=new Node();
	//		if(StdDraw.isMousePressed() && flagSrc==false)
	//		{
	//			flagSrc=true;
	//			x=StdDraw.mouseX();
	//			y=StdDraw.mouseY();
	//			Point3D pSrc =new Point3D(x,y);
	//			closest=findClosestRobot(pSrc);
	//
	//		}
	//
	//
	//		if(StdDraw.isMousePressed() && closest!=null)//find the next to move
	//		{
	//			x=StdDraw.mouseX();
	//			y=StdDraw.mouseY();
	//			Point3D pDest =new Point3D(x,y);
	//			nDest=findClosestNode(pDest);	
	//			Iterator<edge_data> iter=d_g.getE(robots_arr.get(robot_index).getSrc()).iterator();
	//
	//			while(iter.hasNext())
	//			{
	//				if(iter.next().getDest()==nDest.getKey())
	//				{
	//					robots_arr.get(robot_index).setPos(nDest.getLocation());
	//					robots_arr.get(robot_index).setSrc(nDest.getKey());
	//					StdDraw.clear();
	//					drawPoints();
	//					drawEdges();
	//					drawFruits();
	//					drawRobots();
	//					StdDraw.show();       
	//				}
	//			}
	//		}
	//	}


	public Robot findClosestRobot(double x, double y)
	{
		Point3D pos=new Point3D(x,y);
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

	public node_data findClosestNode(double x, double y)
	{
		Point3D pos=new Point3D(x,y);
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


	public static void main(String args[]) throws JSONException {
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