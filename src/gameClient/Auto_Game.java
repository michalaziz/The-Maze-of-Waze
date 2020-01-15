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
import org.json.JSONObject;
import utils.StdDraw;
import gameClient.MyGameGUI;;

public class Auto_Game {

	public MyGameGUI my_game_gui=new MyGameGUI();
	
	public void setMyGameGUI(MyGameGUI g)
	{
		this.my_game_gui=g;
	}
	
	public void startGame(int scenario_num)
	{
		game_service game = Game_Server.getServer(scenario_num);
		
	}
}
