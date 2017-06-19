package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Dbscan_stock {
	private double radius;
    private int minPts;

    public Dbscan_stock(double radius,int minPts) {
        this.radius = radius;
        this.minPts = minPts;
    }
    
    //生成dbscan节点对象名称的集合
    public static ArrayList<Dbscan_stock_entity> gen(HashMap<String, Integer> field){
    	ArrayList<Dbscan_stock_entity> arrayList = new ArrayList<Dbscan_stock_entity>();
    	Set key = field.keySet();
    	Iterator<String> iterator = key.iterator();
    	while(iterator.hasNext()){
    		String name = iterator.next();
    		Dbscan_stock_entity d = new Dbscan_stock_entity(name);
    		arrayList.add(d);
    	}
    	return arrayList;
    }
    
    public ArrayList<Dbscan_stock_entity> process(ArrayList<Dbscan_stock_entity> points,HashMap<String, Integer> field,Double[][] arr2) {
        int size = points.size();
        int idx = 0;
        int cluster = 1;
        while (idx<size) {
        	Dbscan_stock_entity p = points.get(idx++);
            //choose an unvisited point
            if (!p.getVisit()) {
                p.setVisit(true);//set visited
                ArrayList<Dbscan_stock_entity> adjacentPoints = getAdjacentPoints(p, points,field,arr2);
                //System.out.println("size:"+adjacentPoints.size());
                //set the point which adjacent points less than minPts noised
                if (adjacentPoints != null && adjacentPoints.size() < minPts) {
                    p.setNoised(true);
                } 
                else {
                    p.setCluster(cluster);
                    for (int i = 0; i < adjacentPoints.size(); i++) {
                    	Dbscan_stock_entity adjacentPoint = adjacentPoints.get(i);
                        //only check unvisited point, cause only unvisited have the chance to add new adjacent points
                        if (!adjacentPoint.getVisit()) {
                        	//System.out.println("novisit");
                            adjacentPoint.setVisit(true);
                            ArrayList<Dbscan_stock_entity> adjacentAdjacentPoints = getAdjacentPoints(adjacentPoint, points,field,arr2);
                            //System.out.println("momo:"+adjacentAdjacentPoints.size());
                            //add point which adjacent points not less than minPts noised
                            if (adjacentAdjacentPoints != null && adjacentAdjacentPoints.size() >= minPts) {
                            	//System.out.println("pppppp");
                                adjacentPoints.addAll(adjacentAdjacentPoints);
                            }
                        }
                        //add point which doest not belong to any cluster
                        if (adjacentPoint.getCluster() == 0) {
                        	//System.out.println("lll");
                            adjacentPoint.setCluster(cluster);
                            //set point which marked noised before non-noised
                            if (adjacentPoint.getNoised()) {
                                adjacentPoint.setNoised(false);
                            }
                        }
                    }
                    cluster++;
                }
            }
        }
        return points;
    }
  
  private ArrayList<Dbscan_stock_entity> getAdjacentPoints(Dbscan_stock_entity centerPoint,
		  ArrayList<Dbscan_stock_entity> points,HashMap<String, Integer> field,Double[][] arr2) {
		    int index_center = field.get(centerPoint.getName());
	        ArrayList<Dbscan_stock_entity> adjacentPoints = new ArrayList<Dbscan_stock_entity>();
	        for (Dbscan_stock_entity p:points) {
        	int index_p = field.get(p.getName());
            //include centerPoint itself
//        	System.out.println(index_center+"  "+index_p);
//        	System.out.println(p.getName()+" "+centerPoint.getName());
            double distance = arr2[index_center][index_p];
            //System.out.println(distance);
            if (distance>=radius) {
                adjacentPoints.add(p);
            }
        }
        return adjacentPoints;
    }
}
