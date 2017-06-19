package code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

public class Dp_process {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String start = "2015-06-30";//起始日期
		String end = "2015-09-30";//结束日期
		int num =6;//声明聚类个数
		
		//获取所有指定日期内的股东信息
		TreeMap<String, TreeMap<String,ArrayList<Shareholder>>> hs_test = Tools.connect("shareho"
				+ "lder", "shareholder2", start,end);
		//获取所有指定日期内股票信息
		TreeMap<String, TreeMap<String,String>> hs_stock = Connect_stock.connect2("scrapy_news",start, end);
		ArrayList<Object> obj = Tools.get_similar_stock_generate_Arr2(
				hs_test,hs_stock,start,end);
		LinkedHashMap<String, Integer> field = Tools.sortMap((HashMap<String, Integer>)obj.get(0));
		ArrayList<Dp_stock_entity> dp_stock_entities = Dp_stock.get_ett(field);
		Double[][] arr2 = (Double[][])obj.get(1);
		ArrayList<String[]> contents = (ArrayList<String[]>)obj.get(2);
		Dp_stock dps = new Dp_stock(arr2);
		double dc = Dp_stock.sort();//生成dc
		LinkedHashMap<String, Double> p_arr = Dp_stock.getp(dc,field);//生成pi集合		
		ArrayList<Integer> q_arr = Dp_stock.getq(p_arr, field);
		double d_max = Dp_stock.getd_max(contents, -1);
		double[] cta = (double[]) Dp_stock.getc_n(contents, q_arr).get(0);
		int[] n = (int[]) Dp_stock.getc_n(contents, q_arr).get(1);
		int[] c = Dp_stock.process_c(Dp_stock.get_c(num, q_arr), n, q_arr);
		
		
		for(int i=0;i<dp_stock_entities.size();i++){
			Dp_stock_entity dp_stock_entity = dp_stock_entities.get(i);
			dp_stock_entity.setCluster(c[i]);
		}
		
		
		ArrayList<String> name_list = new ArrayList<String>();
		Iterator<String> iterator = field.keySet().iterator();
		while(iterator.hasNext()){
			name_list.add(iterator.next());
		}
		for(int i=0;i<dp_stock_entities.size();i++){
			Dp_stock_entity dp_stock_entity = dp_stock_entities.get(i);
			dp_stock_entity.setP(p_arr.get(name_list.get(i)));
		}
		
		for(int i=0;i<dp_stock_entities.size();i++){
			Dp_stock_entity dp_stock_entity = dp_stock_entities.get(i);
			dp_stock_entity.setCta(cta[i]);
		}
		double[] density = Dp_stock.halo(num, c, dc, q_arr, field, dp_stock_entities);
		Dp_stock.process_halo(dp_stock_entities, field, p_arr, density, c);
//		System.out.println(p_arr);
//		System.out.println(q_arr);
//		System.out.println(field);
		MyCompartor mc = new MyCompartor();
		Collections.sort(dp_stock_entities, mc);
//		for(int i=0;i<dp_stock_entities.size();i++){
//			System.out.println(dp_stock_entities.get(i));
//		}
		Gson gson = new Gson();
		String result = gson.toJson(dp_stock_entities);
		try {
			Tools.write(result, "F:\\apache-tomcat-8.0.39\\webapps\\d3\\stock\\", "dp.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
