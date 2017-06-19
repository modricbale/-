package com.sicd.xjtu.process;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;   
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

import code.Tools; 

public class Readxml {
	
	public static void main(String[] args){
		read_xml();
	}
	
	protected static String convertRGBToHex(int r, int g, int b) {    
	    String rFString, rSString, gFString, gSString, bFString, bSString, result;
	    int red, green, blue;
	    int rred, rgreen, rblue;
	    red = r / 16;
	    rred = r % 16;
	    if (red == 10) rFString = "A";
	    else if (red == 11) rFString = "B";
	    else if (red == 12) rFString = "C";
	    else if (red == 13) rFString = "D";
	    else if (red == 14) rFString = "E";
	    else if (red == 15) rFString = "F";
	    else rFString = String.valueOf(red);

	    if (rred == 10) rSString = "A";
	    else if (rred == 11) rSString = "B";
	    else if (rred == 12) rSString = "C";
	    else if (rred == 13) rSString = "D";
	    else if (rred == 14) rSString = "E";
	    else if (rred == 15) rSString = "F";
	    else rSString = String.valueOf(rred);

	    rFString = rFString + rSString;

	    green = g / 16;
	    rgreen = g % 16;

	    if (green == 10) gFString = "A";
	    else if (green == 11) gFString = "B";
	    else if (green == 12) gFString = "C";
	    else if (green == 13) gFString = "D";
	    else if (green == 14) gFString = "E";
	    else if (green == 15) gFString = "F";
	    else gFString = String.valueOf(green);

	    if (rgreen == 10) gSString = "A";
	    else if (rgreen == 11) gSString = "B";
	    else if (rgreen == 12) gSString = "C";
	    else if (rgreen == 13) gSString = "D";
	    else if (rgreen == 14) gSString = "E";
	    else if (rgreen == 15) gSString = "F";
	    else gSString = String.valueOf(rgreen);

	    gFString = gFString + gSString;

	    blue = b / 16;
	    rblue = b % 16;

	    if (blue == 10) bFString = "A";
	    else if (blue == 11) bFString = "B";
	    else if (blue == 12) bFString = "C";
	    else if (blue == 13) bFString = "D";
	    else if (blue == 14) bFString = "E";
	    else if (blue == 15) bFString = "F";
	    else bFString = String.valueOf(blue);

	    if (rblue == 10) bSString = "A";
	    else if (rblue == 11) bSString = "B";
	    else if (rblue == 12) bSString = "C";
	    else if (rblue == 13) bSString = "D";
	    else if (rblue == 14) bSString = "E";
	    else if (rblue == 15) bSString = "F";
	    else bSString = String.valueOf(rblue);
	    bFString = bFString + bSString;
	    result = "#" + rFString + gFString + bFString;
	    return result;

	}
	
	public static <K,V>void read_xml(){
		ArrayList<Node<K, V>> arr_node = new ArrayList<Node<K,V>>();
		ArrayList<Link<K, V>> arr_link = new ArrayList<Link<K,V>>();
		TreeMap<String, List> tm_d3 = new TreeMap<String, List>();
		File f = new File("Untitled.gexf");   
	     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
	     DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			 try {
				Document doc = builder.parse(f);
				//读取节点信息
			     NodeList nl = doc.getElementsByTagName("node");  
			     for (int i = 0; i < nl.getLength(); i++) {     
			    	 Node<K, V> node = new Node<K, V>();
			    	 Element node_xml = (Element)nl.item(i);		    	 
			    	 String id = node_xml.getAttribute("id");
			    	 String label = id;
			    	 Map<K, V> attributes = new HashMap<K, V>();	    	
			    	 NodeList nodeList = node_xml.getChildNodes();
			    	 org.w3c.dom.Node size_node = nodeList.item(3);
			    	 org.w3c.dom.Node position_node = nodeList.item(5);
			    	 org.w3c.dom.Node color_node = nodeList.item(7);
			    	 double x = Double.parseDouble(position_node.getAttributes().item(0).getNodeValue());
			    	 double y = Double.parseDouble(position_node.getAttributes().item(1).getNodeValue());
			    	 double size = Double.parseDouble(size_node.getAttributes().item(0).getNodeValue()); 
			    	 int r =Integer.parseInt(color_node.getAttributes().item(0).getNodeValue()); 
			    	 int g =Integer.parseInt(color_node.getAttributes().item(1).getNodeValue()); 
			    	 int b =Integer.parseInt(color_node.getAttributes().item(2).getNodeValue()); 
			    	 String color_hex = convertRGBToHex(r, g, b);
				     node.setColor(color_hex);
				     node.setAttributes(attributes);
				     node.setId(id);
				     node.setLabel(label);
				     node.setSize(size/2);
				     node.setX(x);
				     node.setY(y);
				     arr_node.add(node);
				    
			    	}   
				    
				
				 NodeList nl2 = doc.getElementsByTagName("edge");  
				 
			     for (int i = 0; i < nl2.getLength(); i++) {     
			    	//读取边信息
			    	 Link<K, V> link = new Link<K, V>();
			    	 Element link_xml = (Element)nl2.item(i);
			    	 String sourceID = link_xml.getAttribute("source");
			    	 String targetID = link_xml.getAttribute("target");
			    	 NodeList nodeList = link_xml.getChildNodes();
			    	 org.w3c.dom.Node tmp = nodeList.item(1);
			    	 NodeList nodeList2 = tmp.getChildNodes();
			    	 org.w3c.dom.Node cd_node = nodeList2.item(1);	
			    	 String x = cd_node.getAttributes().item(1).getNodeValue();
			    	 if(!x.equals("")&&Double.parseDouble(x)>0.85){
			    	 double cd = Double.parseDouble(x);
				    	 System.out.println("qwe "+cd); 
				    	 Map<K, V> attributes2 = new HashMap<K, V>();	    	 
				    	 link.setSize(1);
				    	 link.setAttributes(attributes2);
				    	 link.setSourceID(sourceID);
				    	 link.setTargetID(targetID);
				    	 arr_link.add(link);
			    	 }
			    	}   
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	    
		 
		tm_d3.put("nodes", arr_node);
		System.out.println(arr_node.size());
		System.out.println(arr_link.size());
		tm_d3.put("edges", arr_link);
		Gson gson = new Gson(); 
		 //生成最终json数据
		String str_d3 = gson.toJson(tm_d3);
		try {
			Tools.write(str_d3, "F:\\apache-tomcat-8.0.39\\webapps\\d3\\echarts\\","shareholder.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
