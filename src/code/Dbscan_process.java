package code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Dbscan_process {
	public static void main(String[]args){
	//得到二维数组矩阵数据供dbscan使用
		Double radius = 0.89;//控制相关系数
		int minPts = 2;//控制个数
		String start = "2015-06-30";//起始日期
		String end = "2015-09-30";//结束日期
		//获取所有指定日期内的股东信息
		TreeMap<String, TreeMap<String,ArrayList<Shareholder>>> hs_test = Tools.connect("shareho"
				+ "lder", "shareholder2", start,end);
		//获取所有指定日期内股票信息
		TreeMap<String, TreeMap<String,String>> hs_stock = Connect_stock.connect2("scrapy_news",start, end);
		ArrayList<Object> obj = Tools.get_similar_stock_generate_Arr2(
				hs_test,hs_stock,start,end);
		HashMap<String, Integer> field = (HashMap<String, Integer>)obj.get(0);
	//	System.out.println(field);
		Double[][] arr2 = (Double[][])obj.get(1);
		System.out.println("fff "+arr2[0][0]+" "+arr2[1][1]+
				" "+arr2[2][2]+" "+arr2[3][3]+" "+arr2[4][4]+" "+arr2[5][5]);
		ArrayList<String[]> contents = (ArrayList<String[]>)obj.get(2);
		Dbscan_stock db = new Dbscan_stock(radius, minPts);
		ArrayList<Dbscan_stock_entity> arr = db.gen(field);		
		ArrayList<Dbscan_stock_entity> result = db.process(arr, field, arr2);
		String str_d3 = Tools.get_similar_stock_generate_json(hs_test,hs_stock, contents,result,start,end,0.86);		
		try {
			Tools.write(str_d3, "F:\\apache-tomcat-8.0.39\\webapps\\d3\\stock\\","stock.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for(Dbscan_stock_entity d:result){
//			System.out.println(d.getCluster());
//		}
	}
	
	
	
}
