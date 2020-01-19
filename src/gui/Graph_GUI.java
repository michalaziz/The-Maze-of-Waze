package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import elements.Fruit;
import elements.Node;
import elements.Robot;
import elements.edge_data;
import elements.node_data;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.graph;
import utils.Point3D;
import utils.StdDraw;

public class Graph_GUI extends JFrame implements Serializable {

	private DGraph graph ;
	private Graph_Algo gAlgo;

	public Graph_Algo get_gAlgo()
	{
		return this.gAlgo;
	}
	public DGraph get_graph()
	{
		return graph;
	}
	public Graph_GUI(graph g )
	{
		this.graph = (DGraph)g;	
		gAlgo=new Graph_Algo();
		gAlgo.init(this.graph);
		StdDraw.setGui(this);
	}

	public Graph_GUI()
	{
		gAlgo=new Graph_Algo();
		graph=new DGraph();
		StdDraw.setGui(this);
	}

	public void initGUI() 
	{	

		this.setScale();
		this.drawPoints();
		this.drawEdges();
	}
	
	
	public void init(String name)
	{
		this.gAlgo.init(name);
		this.graph= (DGraph) gAlgo.graph_A;
		initGUI();
	}

	public void setScale() {
		double x_min = Double.MAX_VALUE;
		Double x_max = Double.MIN_VALUE;
		Double y_min = Double.MAX_VALUE;
		Double y_max = Double.MIN_VALUE;

		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().x() < x_min) {
				x_min = (double) currentNode.getLocation().x();
			}
			if(currentNode.getLocation().x() > x_max) {
				x_max = (double) currentNode.getLocation().x();
			}
			if(currentNode.getLocation().y() < y_min) {
				y_min = (double) currentNode.getLocation().y();
			}
			if(currentNode.getLocation().y() > y_max) {
				y_max = (double) currentNode.getLocation().y();
			}
		}

		StdDraw.setCanvasSize(Math.abs((int)(x_min+x_max))+1250,
				Math.abs((int)(y_min+y_max))+550);
		StdDraw.setXscale(x_min-0.0001,x_max+0.0001);
		StdDraw.setYscale(y_min-0.001,y_max+0.001);

	}

	public void drawPoints()
	{

		Iterator<node_data> iter=  this.graph.getV().iterator();
		while(iter.hasNext())
		{
			node_data temp= iter.next();
			StdDraw.setPenRadius(0.02);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.point(temp.getLocation().x(),temp.getLocation().y());
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius(0.001);
			StdDraw.text(temp.getLocation().x()-0.00028, temp.getLocation().y(),""+temp.getKey());
		}
	}

	public void drawEdges()
	{
		Iterator<node_data> iterN=  this.graph.getV().iterator();
		while(iterN.hasNext())
		{
			node_data tempV=iterN.next();
			Collection<edge_data> edges = this.graph.getE(tempV.getKey());
			if(edges==null)
				break;
			Iterator<edge_data> iterE=edges.iterator();
			while(iterE.hasNext())
			{
				StdDraw.setPenRadius(0.003);
				StdDraw.setPenColor(StdDraw.GRAY);
				edge_data tempE=iterE.next();
				StdDraw.line(graph.getNode(tempE.getSrc()).getLocation().x(),
						graph.getNode(tempE.getSrc()).getLocation().y(),
						graph.getNode(tempE.getDest()).getLocation().x(),
						graph.getNode(tempE.getDest()).getLocation().y());
				StdDraw.setPenRadius(0.015);
				StdDraw.setPenColor(StdDraw.GREEN);
				StdDraw.point(((graph.getNode(tempE.getSrc()).getLocation().x()+
						graph.getNode(tempE.getDest()).getLocation().x()*7)/8),
						((graph.getNode(tempE.getSrc()).getLocation().y())+
								graph.getNode(tempE.getDest()).getLocation().y()*7)/8);
				//				StdDraw.setPenColor(StdDraw.BOOK_BLUE);
				//				StdDraw.text(((graph.getNode(tempE.getSrc()).getLocation().x()+
				//						graph.getNode(tempE.getDest()).getLocation().x())/2),
				//						((graph.getNode(tempE.getSrc()).getLocation().y())+
				//								graph.getNode(tempE.getDest()).getLocation().y())/2,
				//						""+tempE.getWeight());
			}
		}
	}

}