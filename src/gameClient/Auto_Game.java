package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.Fruit;
import elements.Robot;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import utils.Point3D;
import utils.StdDraw;
import gameClient.MyGameGUI;;

public class Auto_Game {

	public MyGameGUI my_game_gui;

	
	
	public void setMyGameGUI(MyGameGUI g)
	{
		this.my_game_gui=g;
	}

	public MyGameGUI getMyGameGUI()
	{
		return this.my_game_gui;
	}

	public void startGame(int scenario_num)
	{
		my_game_gui.game = Game_Server.getServer(scenario_num);
		String graphGame = my_game_gui.game.getGraph();
		DGraph d = new DGraph();
		d.init(graphGame);
		my_game_gui.d_g = d;
		System.out.println(my_game_gui.game.toString());
		my_game_gui.fruits_arr = my_game_gui.initFruits();
		my_game_gui.robots_arr = my_game_gui.initRobots();// היא לא באמת עושה משהו initRobots אל תשתמשי בפונקציה         
		StdDraw.clear();
		my_game_gui.paint();
		addRobotAuto(my_game_gui.fruits_arr);
		my_game_gui.drawRobots();
		my_game_gui.game.startGame();
		my_game_gui.start();

	}

	public void addRobotAuto(List<Fruit> f){

		f= my_game_gui.fruits_arr;
		int i=0;
		int size= f.size();
		Collection<node_data> nodesGame= my_game_gui.d_g.getV();
		Iterator<node_data> iterNode= nodesGame.iterator();
		while (iterNode.hasNext())
		{
			node_data n= iterNode.next();
			Collection<edge_data> edgeN= my_game_gui.d_g.getE(n.getKey());
			Iterator<edge_data> iterE= edgeN.iterator();
			while (iterE.hasNext())
			{
				Fruit fruity= f.get(i);
				edge_data edgeF= iterE.next();
				Point3D pSrc= my_game_gui.d_g.getNode(edgeF.getSrc()).getLocation();
				Point3D pDest= my_game_gui.d_g.getNode(edgeF.getDest()).getLocation();
				double dis1= Math.abs(pSrc.distance2D(pDest)); // Calculate distance between 2 points
				double dis2=Math.abs(pSrc.distance2D(fruity.getPos())); //Calculate distance between fruit pos and src location
				double dist3= Math.abs(pDest.distance2D(fruity.getPos())); //Calculate distance between fruit pos and dest location
				if(dis1==dis2+dist3)// the fruit is on the edge
				{
					int small= edgeF.getSrc();
					int big= edgeF.getDest();
					if(small<big) // on this edge there is apple- (low-------->high)
					{
						my_game_gui.game.addRobot(n.getKey());
					}
					else{ my_game_gui.game.addRobot(edgeF.getDest());}// on this edge there is banana- (high-------->low)
					i++;
					if(i==size-1) break; // we found all places of the fruits
				}
			}
		}
	}

}
