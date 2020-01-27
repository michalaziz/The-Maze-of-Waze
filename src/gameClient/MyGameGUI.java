package gameClient;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import algorithms.Graph_Algo;
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


public class MyGameGUI extends Thread{

	public static DGraph d_g;
	public static Graph_Algo algoGame;
	public static Graph_GUI gui;
	public String typeGame;
	public int scenario_num;
	public static game_service game2;
	public Auto_Game a_g = new Auto_Game(this);
	List<Fruit> fruits_arr;
	List<Robot> robots_arr=new ArrayList<Robot>();
	public Robot r= new Robot();
	Fruit f=new Fruit();
	private static KML_Logger kml;
	Thread t;
	public static int id;
	public Thread trd= new Thread();

	public MyGameGUI() throws JSONException, InterruptedException
	{
		selectGame();
	}


	// getters and setters
	public Fruit getF() { return f; }
	public void setF(Fruit f) { this.f = f; }
	public List<Fruit> getFruits_arr() { return this.fruits_arr; }
	public void setFruits_arr(List<Fruit> fruits_arr) { this.fruits_arr = fruits_arr; }
	public List<Robot> getRobots_arr() { return robots_arr; }
	public void setRobots_arr(List<Robot> robots_arr) { this.robots_arr = robots_arr; }

	public static DGraph getGraph() {
		return d_g;
	}

	public static void setGraph(DGraph graph) {
		MyGameGUI.d_g = graph;
	}

	public static game_service getGame() {
		return game2;
	}

	public static void setGame(game_service game) {
		MyGameGUI.game2 = game;
	}



	/**
	 * this function let the user choose scenario number and game type manual or automatic
	 * and initializing it.
	 * @throws JSONException
	 * @throws InterruptedException 
	 */
	public void selectGame() throws JSONException, InterruptedException
	{
    	String st1 = JOptionPane.showInputDialog(null, " Enter ID number :");
        id = Integer.parseInt(st1);
        Game_Server.login(id);
		String st = JOptionPane.showInputDialog(null, "Choose scenario number between 0-23 :");
		scenario_num = Integer.parseInt(st);
		if (scenario_num > 23 || scenario_num < 0)
			JOptionPane.showMessageDialog(null, "The number that you entered isn't a Scenario number ");
		else {
			String[] options = {"Automtic", "Manual"};
			int option = JOptionPane.showOptionDialog(null, "Select a game type", "game type"
					, JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			if(option==1)//manual
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

	/**
	 * this function manage the manual game and running it
	 * it draw the graph place the rbot manual and than start the game
	 * @throws JSONException
	 */
	public void manualGame() throws JSONException
	{
		game2 = Game_Server.getServer(scenario_num);
		String graphGame = game2.getGraph();
		DGraph d = new DGraph();
		d.init(graphGame);
		d_g = d;
		System.out.println(game2.toString());
		fruits_arr = initFruits();
		setScale();
		StdDraw.picture(getXmin()+0.012, getYmin()+0.0038, "mall.png", 0.03,0.01);
		paint();

		addRobotManual();

		List<String> robListManual = game2.getRobots();
		robots_arr=r.initRobots(robListManual);

		drawRobots();
		StdDraw.show();
		game2.startGame();
		while(game2.isRunning())
		{
			moveRobotManual();
			drawGrade(game2);
			StdDraw.clear();
			StdDraw.picture(getXmin()+0.012, getYmin()+0.0038, "mall.png", 0.03,0.01);
			StdDraw.enableDoubleBuffering();
			updatGraph();
			StdDraw.show();

		}

	}


	/**
	 * this function update the graph after there was some changes like robot moved
	 * or fruit eat.
	 * than draw the point edges and fruits robots again
	 * @throws JSONException
	 */
	public void updatGraph() throws JSONException
	{
		StdDraw.picture(getXmin()+0.012, getYmin()+0.0038, "mall.png", 0.03,0.01);
		String fruitList = game2.getFruits().toString();
		String robotList = game2.getRobots().toString();

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
			game2.addRobot(ro.getSrc());
			StdDraw.picture(ro.getPos().x(), ro.getPos().y(),"rob.png",0.0005,0.0005);
		}

		drawPoints();
		drawEdges();
		drawFruits();
		drawGrade(game2);
	}

	/**
	 * this function draw the game
	 * set the scale, draw the nodes, draw the edges, and draw the fruits
	 */
	public void paint() {
		//setScale();
		drawPoints();
		drawEdges();
		drawFruits();
		StdDraw.show();
	}


	/**
	 * this function place the robot y manual on the start of the game
	 * by taking the key node of the place you want to place the robot
	 */
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
			game2.addRobot(keyRobotsList[i]);
		}

	}


	/**
	 * this function move the robots by manual by clicking the mouse
	 * click on the robots you want to move and click on the node you want to move to
	 */
	int RobotId=0;
	public void moveRobotManual()
	{

		Robot closest =null;
		List<String> log = game2.move();
		double x=0,y=0;
		if(log!=null)
		{
			long time= game2.timeToEnd();
			for(int i=0; i<log.size(); i++)
			{
				String rj=log.get(i);
				try {
					JSONObject line = new JSONObject(rj);
					JSONObject tomove = line.getJSONObject("Robot");
					int robID = tomove.getInt("id");
					int src = tomove.getInt("src");
					int dest = tomove.getInt("dest");
					if(StdDraw.isMousePressed())//which robot to move
					{
						x=StdDraw.mouseX();
						y=StdDraw.mouseY();
						closest=findClosestRobot(x,y);
						while(closest==null)//while robot not found click again
						{
							x=StdDraw.mouseX();
							y=StdDraw.mouseY();
							closest=findClosestRobot(x,y);
						}
						RobotId=closest.getID();
					}

					if(StdDraw.isMousePressed())//where to move the robot
					{
						x=StdDraw.mouseX();
						y=StdDraw.mouseY();
						node_data nDest=findClosestNode(x, y);
						if(dest==-1)
							game2.chooseNextEdge(RobotId, nDest.getKey());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateFruit()
	{
		List<String> arrf= game2.getFruits();
		if(arrf!=null)
			for(int i=0; i<fruits_arr.size(); i++)
				fruits_arr.get(i).initFruit(arrf.get(i));
	}

	public void updateRobots(){
		List<String> robots = game2.getRobots();
		if(robots!=null){
			System.out.println(this.robots_arr.size());
			for (int i = 0; i <this.robots_arr.size() ; i++) {
				this.robots_arr.get(i).initRobot(robots.get(i));
			}
		}
	}




	/**
	 * this function return the closest robot to the given point x,y
	 * @param x
	 * @param y
	 * @return the closest robot to x,y
	 */
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
	/**
	 * this function find the closest node on the graph to the given x,y
	 * @param x
	 * @param y
	 * @return the closest node to the point x,y
	 */

	public node_data findClosestNode(double x, double y)
	{
		Point3D pos=new Point3D(x,y);
		Double min = Double.MAX_VALUE;
		node_data closest =null;
		Iterator<elements.node_data> iter= d_g.getV().iterator();
		while(iter.hasNext())
		{
			node_data next = (node_data) iter.next();
			double distance = next.getLocation().distance3D(pos);
			if(distance<min)
			{
				min = distance;
				closest=next;
			}
		}
		return closest;
	}


	/**
	 * @return the size of the robot in this scenario number.
	 */
	public int robors_size()
	{
		int rs=0;
		try {
			String x = game2.toString();
			JSONObject l = new JSONObject(x);
			JSONObject ttt = l.getJSONObject("GameServer");
			rs= ttt.getInt("robots");

		}catch(Exception e) {e.printStackTrace();}
		return rs;
	}


	/**
	 * This function initializes the fruits found in this scenario number by using the function initFruit
	 * which initializes fruit from a string by using Json.
	 * @return List of objects that are the fruits of the game
	 */
	public List<Fruit> initFruits() {

		List<String> fr = game2.getFruits();
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
	 * This function initializes the Robots found in this scenario number by using the function initRobot
	 * which initializes Robot from a string by using Json.
	 * @return List of objects that are the Robots of the game.
	 */
	public List<Robot> initRobots() {
		List<String> rob = game2.getRobots();
		List<Robot> ans = new ArrayList<Robot>();
		Iterator<String> iter = rob.iterator();
		while (iter.hasNext()) {
			Robot r = new Robot();
			r.initRobot(iter.next());
			ans.add(r);
		}
		return ans;
	}



	///////////////////////////////////////////////////draw_functions/////////////////////////////////////////////////////////////////


	public double getXmin() {
		double x_min = Double.MAX_VALUE;
		Iterator<elements.node_data> iter = d_g.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = (node_data) iter.next();
			if(currentNode.getLocation().x() < x_min) {
				x_min = currentNode.getLocation().x();
			}
		}
		return x_min;
	}



	public double getYmin() {
		double y_min = Double.MAX_VALUE;
		Iterator<elements.node_data> iter =d_g.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = (node_data) iter.next();
			if(currentNode.getLocation().y() < y_min) {
				y_min =  currentNode.getLocation().y();
			}
		}
		return y_min;
	}

	public void drawGrade(game_service game1)
	{
		String results = game1.toString();
		long t = game1.timeToEnd();
		try {
			int scoreInt=0;
			JSONObject score = new JSONObject(results);
			JSONObject ttt = score.getJSONObject("GameServer");
			scoreInt = ttt.getInt("grade");

			String countDown = "Time Left: " + t/1000+"." + t%1000;
			String scoreStr = "Your Score: " + scoreInt;
			StdDraw.setPenColor(Color.RED);
			StdDraw.text(getXmin()+0.00180, getYmin()-0.0003, countDown);
			StdDraw.text(getXmin()+0.0060, getYmin()-0.0003, scoreStr);
		}
		catch (Exception e) {
			System.out.println("cant print score");
		}
	}


	public void drawRobots() {
		Iterator<Robot> robotIterator = this.robots_arr.iterator();
		while (robotIterator.hasNext()) {
			Robot current = robotIterator.next();
			double x = current.getPos().x();
			double y = current.getPos().y();
			StdDraw.picture(x, y, "rob.png", 0.0005, 0.0005);
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
		Iterator<elements.node_data> iterN = this.d_g.getV().iterator();
		while (iterN.hasNext()) {
			node_data tempV = (node_data) iterN.next();
			Collection<elements.edge_data> edges = this.d_g.getE(tempV.getKey());
			if (edges == null)
				break;
			Iterator<elements.edge_data> iterE = edges.iterator();
			while (iterE.hasNext()) {
				StdDraw.setPenRadius(0.003);
				StdDraw.setPenColor(StdDraw.BLACK);
				edge_data tempE = (edge_data) iterE.next();
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


		this.game2 = Game_Server.getServer(scenario_num);
		String graphGame = game2.getGraph();
		DGraph d = new DGraph();
		d.init(graphGame);
		this.d_g = d;
		Iterator<elements.node_data> iter = this.d_g.getV().iterator();
		while (iter.hasNext()) {
			node_data currentNode = (node_data) iter.next();
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
		Iterator<elements.node_data> iter = this.d_g.getV().iterator();
		while (iter.hasNext()) {
			node_data temp = (node_data) iter.next();
			StdDraw.setPenRadius(0.02);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.point(temp.getLocation().x(), temp.getLocation().y());
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius(0.001);
			StdDraw.text(temp.getLocation().x() - 0.00028, temp.getLocation().y(), "" + temp.getKey());
		}
	}


	public KML_Logger getKml() {
		return MyGameGUI.kml;
	}

	public void setKml(KML_Logger kml) {
		MyGameGUI.kml = kml;
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}


	public static void main(String args[]) throws JSONException, InterruptedException {
		MyGameGUI gam = new MyGameGUI();


	}

}