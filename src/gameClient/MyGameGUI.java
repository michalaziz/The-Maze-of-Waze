package gameClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;

public class MyGameGUI implements Runnable {

	public static void initEdges()
	{
		try {
			
		int scenario_num = 2;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		String g = game.getGraph();
		DGraph d_g = new DGraph();
		JSONObject line1= new JSONObject(g);
		JSONArray nodes = line1.getJSONArray("Nodes");
		System.out.println();
		
		
		
		
		
		
		
		}catch(Exception e)
		{
			
		}
		
	}
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[])
	{
		initEdges();
	}

}
