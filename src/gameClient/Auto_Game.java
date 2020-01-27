package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;
import elements.Fruit;
import elements.Robot;
import elements.edge_data;
import elements.node_data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

public class Auto_Game extends Thread {
    public MyGameGUI myGG;


    //constructor//
    public Auto_Game(MyGameGUI g) {
        this.myGG = g;
    }


    /**
     * this function update the graph the changes of moving robots and eating fruits
     * @throws JSONException
     * @throws InterruptedException 
     */
    public void updateGraphAuto() throws JSONException, InterruptedException
    {
    	

        StdDraw.picture(this.myGG.getXmin()+0.012, this.myGG.getYmin()+0.0038, "mall.png", 0.03,0.01);
        this.myGG.drawPoints();
        this.myGG.drawEdges();
        this.myGG.drawFruits();
        this.myGG.drawGrade(this.myGG.game2);
        String fruitList = this.myGG.game2.getFruits().toString();
        String robotList = this.myGG.game2.getRobots().toString();
        this.myGG.fruits_arr.clear();
        JSONArray f = new JSONArray(fruitList);
        for(int i=0; i<f.length(); i++) {
            JSONObject current = f.getJSONObject(i);
            JSONObject current2 = current.getJSONObject("Fruit");
            int type = current2.getInt("type");
            double value = current2.getDouble("value");
            Object pos = current2.get("pos");
            Point3D p = new Point3D(pos.toString());
            Fruit fu = new Fruit(value, type, p);
            this.myGG.fruits_arr.add(fu);
        }


        
        this.myGG.robots_arr.clear();
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
            this.myGG.robots_arr.add(ro);
            this.myGG.game2.addRobot(ro.getSrc());
            this.myGG.getKml().placeMark("data\\rob.png",ro.getPos().toString());
            StdDraw.picture(ro.getPos().x(), ro.getPos().y(),"rob.png",0.0005,0.0005);
        }
    }
    
    



    /**
     * This function start automatic game by the scenario num that the user choose.
     * @param scenario_num is the number of the game that the user choose.
     * @throws JSONException
     * @throws InterruptedException 
     */
    public void startGame(int scenario_num) throws JSONException, InterruptedException  {
    	//sleep(100);
        this.myGG.setKml(new KML_Logger(scenario_num));
        this.myGG.game2 = Game_Server.getServer(scenario_num);
        String graphGame = this.myGG.game2.getGraph();
        DGraph d = new DGraph();
        d.init(graphGame);
        myGG.d_g = d;
        System.out.println(this.myGG.game2.toString());
        this.myGG.fruits_arr = this.myGG.initFruits();
        this.myGG.setScale();
        StdDraw.picture(this.myGG.getXmin()+0.012, this.myGG.getYmin()+0.0038, "mall.png", 0.03,0.01);
        this.myGG.paint();
        addRobotAuto();
        this.myGG.drawRobots();
        
        this.myGG.game2.startGame();
        while(this.myGG.game2.isRunning())
        {
            moveAuto(this.myGG.game2,this.myGG.d_g);
            this.myGG.drawGrade(this.myGG.game2);
            StdDraw.clear();
            StdDraw.enableDoubleBuffering();
            sleep(65);
            updateGraphAuto();
            StdDraw.show();
        }
        
        this.myGG.getKml().kmlPause();
        this.myGG.game2.stopGame();
        String temp=this.myGG.game2.toString();
        JSONObject o= (JSONObject) new JSONObject(temp).get("GameServer");
        int gr= o.getInt("grade");
        int mov= o.getInt("moves");
        JOptionPane.showMessageDialog(null,"GAME OVER\n"+"moves:"+mov+"\n grade:"+gr);
    }
//        String temp=this.myGG.game2.toString();
//        JSONObject o= (JSONObject) new JSONObject(temp).get("GameServer");
//        int gr= o.getInt("grade");
//        int mov= o.getInt("moves");
//        JOptionPane.showMessageDialog(null,"GAME OVER\n"+"moves:"+mov+"\n grade:"+gr);
        
    


    /**
     * This function add robots to the graph.
     * This function placed the robot next to the first fruits that found in the graph.
     * @param f is the list fruits of the scenario tha choose by the user
     * @throws InterruptedException 
     */
    public void addRobotAuto() throws InterruptedException {
        List<Fruit> f = this.myGG.fruits_arr;
        int robNum = this.myGG.robors_size();
        this.myGG.initRobots();
        for (int j = 0; j < robNum; j++) {
            edge_data e = isFruitOnEdge(f.get(j));
            if(e!=null)
            {
                if((e.getSrc()<e.getDest())  && (f.get(j).getType()==-1))// type banana
                {
                    this.myGG.game2.addRobot(e.getDest()); // place the robot in dest caz banana is eaten from high to low(dest is the maximum)
                    node_data n= this.myGG.d_g.getNode(e.getDest());
                    this.myGG.robots_arr.add(new Robot(j,e.getDest(),n.getLocation()));
                }
                else if((e.getSrc()>e.getDest()) && (f.get(j).getType()==-1)) //type banana
                {
                    this.myGG.game2.addRobot(e.getSrc()); // place the robot in src
                    node_data n= this.myGG.d_g.getNode(e.getSrc());
                    this.myGG.robots_arr.add(new Robot(j,e.getSrc(),n.getLocation()));
                }
                else if ((e.getSrc()<e.getDest())  && (f.get(j).getType()== 1))// type apple
                {
                    this.myGG.game2.addRobot(e.getSrc()); // place the robot in src
                    node_data n= this.myGG.d_g.getNode(e.getSrc());
                    this.myGG.robots_arr.add(new Robot(j,e.getSrc(),n.getLocation()));
                }
                else if ((e.getSrc()>e.getDest()) && (f.get(j).getType()==1)) // type apple
                {
                    this.myGG.game2.addRobot(e.getDest()); // place the robot in dest caz apple is eaten from low to high(dest is the minimum)
                    node_data n= this.myGG.d_g.getNode(e.getDest());
                    this.myGG.robots_arr.add(new Robot(j,e.getDest(),n.getLocation()));
                }
            }
        }
    }

    /**
     * This function return edge that there is fruit on her , is there is no fruit -return null
     * @param f is a fruit from the list fruits of the game
     * @return edge that there is fruit on her - banana or apple
     */
    public edge_data isFruitOnEdge(Fruit f) {
        Collection<node_data> nodesGame = this.myGG.d_g.getV();
        Iterator<node_data> iterNode = nodesGame.iterator();
        while (iterNode.hasNext()) {
            node_data n = iterNode.next();
            Collection<edge_data> edgeN = this.myGG.d_g.getE(n.getKey());
            Iterator<edge_data> iterE = edgeN.iterator();
            while (iterE.hasNext()) {
                edge_data edgeF = iterE.next();
                Point3D pSrc = this.myGG.d_g.getNode(edgeF.getSrc()).getLocation();
                Point3D pDest = this.myGG.d_g.getNode(edgeF.getDest()).getLocation();
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
     * @throws InterruptedException 
     */
    public void moveAuto(game_service game, DGraph d) throws InterruptedException
    {
        List<node_data> p = new ArrayList<node_data>();
        int dest=-1;
        Double min = Double.MAX_VALUE;
        Graph_Algo algo = new Graph_Algo();
        algo.init(d);
        Robot r=null;
        for(int i=0; i<this.myGG.robots_arr.size(); i++)
            if(this.myGG.robots_arr.get(i).getDest() == -1){
                {
                    r= this.myGG.robots_arr.get(i);
                    for(int j=0; j<this.myGG.fruits_arr.size(); j++)
                    {
                        Fruit fr = this.myGG.fruits_arr.get(j);
                        edge_data edge=getFruitEdge(fr);
                        double dis=algo.shortestPathDist(r.getSrc(),edge.getSrc());
                        if(dis<min)
                        {
                            min=dis;
                            dest=edge.getSrc();
                            p=algo.shortestPath(r.getSrc(), dest);
                            p.add(d.getNode(edge.getDest()));
                            p.remove(0);
                        }
                    }
                    dest = p.get(0).getKey();
                    
                    game.chooseNextEdge(r.getID(), dest);
                    
                }
            }
        game.move();
    }

    /**
     * this function gets fruit and returns the edge that the fruit on
     * @param f
     * @return
     */
    private edge_data getFruitEdge(Fruit f) {
        Iterator<node_data> iterV = myGG.d_g.getV().iterator();
        while(iterV.hasNext()) {
            node_data nextV=iterV.next();
            if (myGG.d_g.getE(nextV.getKey()) != null) {
                Iterator<edge_data>iterE = myGG.d_g.getE(nextV.getKey()).iterator();
                while (iterE.hasNext()) {
                    edge_data nextE = iterE.next();
                    node_data dest = myGG.d_g.getNode(nextE.getDest());
                    node_data src = myGG.d_g.getNode(nextE.getSrc());
                    double srcToFruit = distance(src.getLocation(), f.getPos());
                    double fruitToDest = distance(f.getPos(), dest.getLocation());
                    double dis = distance(src.getLocation(), dest.getLocation());
                    if (srcToFruit + fruitToDest <= dis + 0.000001) {
                        if (f.getType() == -1 && src.getKey() > dest.getKey())
                            return nextE;
                        if (f.getType() == 1 && src.getKey() < dest.getKey())
                            return nextE;
                    }
                }
            }
        }
        return null;
    }

    /**
     * calculate distance between points
     * @param src
     * @param dest
     * @return
     */
    private static double distance(Point3D src, Point3D dest) {
        double result = 0;
        double x1 = src.x();
        double x2 = dest.x();
        double y1 = src.y();
        double y2 = dest.y();
        result = Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2);
        result = Math.sqrt(result);
        return result;
    }
}