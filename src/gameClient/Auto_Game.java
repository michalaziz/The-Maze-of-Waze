package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;
import elements.Edge;
import elements.Fruit;
import elements.Robot;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Auto_Game extends Thread {
    public MyGameGUI my_game_gui;


          //constructor//
    public Auto_Game(MyGameGUI g) {
        this.my_game_gui = g;
    }


    /**
     * This function start automatic game by the scenario num that the user choose.
     * @param scenario_num is the number of the game that the user choose.
     */
    public void startGame(int scenario_num)  {
        this.my_game_gui.game2 = Game_Server.getServer(scenario_num);
        String graphGame = this.my_game_gui.game2.getGraph();
        DGraph dg = new DGraph();
        dg.init(graphGame); // initialize from Json points and edges
        System.out.println(this.my_game_gui.game2.toString());
        this.my_game_gui.fruits_arr = this.my_game_gui.initFruits(); // initialize from Json fruits
       this.my_game_gui.robots_arr = this.my_game_gui.initRobots(); // initialize from Json robots
        StdDraw.clear();
        this.my_game_gui.paint(); // set scale, draw points,draw edges,draw fruits
        addRobotAuto(this.my_game_gui.fruits_arr);
        this.my_game_gui.drawRobots();
        StdDraw.show();
        this.my_game_gui.game2.startGame();

        while (my_game_gui.game2.isRunning())
        {
            moveRobots(this.my_game_gui.game2,this.my_game_gui.d_g);
            StdDraw.clear();
            StdDraw.enableDoubleBuffering();
            StdDraw.show();

        }

    }

    public void run() {

    }

    /**
     * This function add robots to the graph.
     * This function placed the robot next to the first fruits that found in the graph.
     * @param f is the list fruits of the scenario tha choose by the user
     */
    public void addRobotAuto(List<Fruit> f) {
        f = this.my_game_gui.fruits_arr;
        int robNum = this.my_game_gui.robors_size();
        this.my_game_gui.initRobots();
        for (int j = 0; j < robNum; j++) {
            edge_data e = isFruitOnEdge(f.get(0));
            if(e!=null)
            {
                if((e.getSrc()<e.getDest())  && (f.get(0).getType()==-1))// type banana
                {
                    this.my_game_gui.game2.addRobot(e.getDest()); // place the robot in dest caz banana is eaten from high to low(dest is the maximum)
                    node_data n= this.my_game_gui.d_g.getNode(e.getDest());
                    this.my_game_gui.robots_arr.add(new Robot(e.getDest(),j,n.getLocation()));
                }
                else if((e.getSrc()>e.getDest()) && (f.get(0).getType()==-1)) //type banana
                {
                    this.my_game_gui.game2.addRobot(e.getSrc()); // place the robot in src
                    node_data n= this.my_game_gui.d_g.getNode(e.getSrc());
                    this.my_game_gui.robots_arr.add(new Robot(e.getSrc(),j,n.getLocation()));
                }
                else if ((e.getSrc()<e.getDest())  && (f.get(0).getType()== 1))// type apple
                {
                    this.my_game_gui.game2.addRobot(e.getSrc()); // place the robot in src
                    node_data n= this.my_game_gui.d_g.getNode(e.getSrc());
                    this.my_game_gui.robots_arr.add(new Robot(e.getSrc(),j,n.getLocation()));
                }
                else if ((e.getSrc()>e.getDest()) && (f.get(0).getType()==1)) // type apple
                {
                    this.my_game_gui.game2.addRobot(e.getDest()); // place the robot in dest caz apple is eaten from low to high(dest is the minimum)
                    node_data n= this.my_game_gui.d_g.getNode(e.getDest());
                    this.my_game_gui.robots_arr.add(new Robot(e.getDest(),j,n.getLocation()));
                }
                f.remove(0);
            }
        }
    }

    /**
     * This function return edge that there is fruit on her , is there is no fruit -return null
     * @param f is a fruit from the list fruits of the game
     * @return edge that there is fruit on her - banana or apple
     */
    public edge_data isFruitOnEdge(Fruit f) {
        Collection<node_data> nodesGame = this.my_game_gui.d_g.getV();
        Iterator<node_data> iterNode = nodesGame.iterator();
        while (iterNode.hasNext()) {
            node_data n = iterNode.next();
            Collection<edge_data> edgeN = this.my_game_gui.d_g.getE(n.getKey());
            Iterator<edge_data> iterE = edgeN.iterator();
            while (iterE.hasNext()) {
                edge_data edgeF = iterE.next();
                Point3D pSrc = this.my_game_gui.d_g.getNode(edgeF.getSrc()).getLocation();
                Point3D pDest = this.my_game_gui.d_g.getNode(edgeF.getDest()).getLocation();
                double dis1 = (pSrc.distance2D(pDest)); // Calculate distance between 2 points
                double dis2 = Math.abs(f.getPos().distance2D(pSrc)); //Calculate distance between fruit pos and src location
                double dist3 = Math.abs(f.getPos().distance2D(pDest)); //Calculate distance between fruit pos and dest location
                double dist4 = Math.abs(dis2 + dist3);
                double dist5 = Math.abs(dis1 - dist4);
                if ((dist5 <= 0.0000001))// the fruit is on the edge
                {
                    if (((edgeF.getSrc() < edgeF.getDest()) && (f.getType() == 1)) ||  ((edgeF.getSrc() > edgeF.getDest()) && (f.getType() == -1))
                    || (edgeF.getSrc() < edgeF.getDest() && (f.getType()==-1)) || ((edgeF.getSrc() > edgeF.getDest()) && (f.getType() == 1))) {
                        return edgeF;
                    }
                }
            }
        }
        return null; // there is no fruits
    }



    /**
     * Moves each of the robots along the edges .
     * in case the robot is on a node, the next destination is chosen by the the function "nextNode".
     * @param game is the scenario number that the user choose
     * @param gg is the graph of this scenario
     */
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
                        desti = nextNode(this.my_game_gui.getRobots_arr().get(i), gg, this.my_game_gui.getFruits_arr());
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
     * the function gets Dgraph ,robot and list of fruits of the node that the robot placed on it
     * @param g   is the Dgraph of the game
     * @param roby is the robot we wont tp find for him the best path
     * @return key of the node that the robot will go
     */
    public int nextNode(Robot roby, DGraph g, List<Fruit> gmF) {

        Graph_Algo alg = new Graph_Algo(g);
        edge_data e;
        double dis = Integer.MAX_VALUE;
        int walkto = -1;
        double robyDis = 0;
        int next = -1;
        Iterator<Fruit> itF = gmF.iterator();
        //Calculate the distance between the robot and the fruit
        while (itF.hasNext()) {
            Fruit f = itF.next();
            if (f.getVisit() == false) {
                e = this.isFruitOnEdge(f); // return the edge if it exist, else-return null
                // check if the fruit is banana or apple
                if (f.getType() == 1) ///////if apple///////
                {
                    if (e.getSrc() < e.getDest()) // The direction of eating the fruit is correct
                    {
                        robyDis = alg.shortestPathDist(roby.getSrc(), e.getDest());
                        next = e.getSrc();
                    } else if (e.getDest() < e.getSrc()) // The direction of eating the fruit is opposite
                    {
                        robyDis = alg.shortestPathDist(roby.getSrc(), e.getDest());
                        next = e.getDest();// The robot will be moved from dest to src. its weighted directed graph
                    }
                } else if (f.getType() == -1) ////////if banana////////
                {
                    if (e.getSrc() > e.getDest()) // The direction of eating the fruit is correct
                    {
                        robyDis = alg.shortestPathDist(roby.getSrc(), e.getSrc());
                        next = e.getDest();
                    } else if (e.getDest() > e.getSrc()) {
                        robyDis = alg.shortestPathDist(roby.getSrc(), e.getDest());
                        next = e.getSrc();
                    }
                }

                if (robyDis < dis) {
                    dis = robyDis;
                    walkto = next;
                }
                f.setVisit(true);
            }
            roby.setToNextNode(alg.shortestPath(roby.getSrc(), walkto)); // set the shortest path to roby
        }
        return roby.getToNextNode().get(1).getKey();
    }



}