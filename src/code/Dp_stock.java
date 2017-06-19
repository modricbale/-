package code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Dp_stock {
	public Dp_stock(Double[][]arr2){
		this.size = arr2[0].length;
		this.arr2= arr2;
	}
	public static int size;
	public static Double arr2[][];
	
	
	//根据二维矩阵获取d(i)(j)的数组排序集合,并返回d(c)
	public static double sort(){
		double[] arr = new double[size*(size-1)/2];
		System.out.println(arr.length);
		int M = size*(size-1)/2+1;
		
		//t为控制d（c）的参数
		double t = 0.97;
		int index = (int) Math.rint(M*t);
		System.out.println(index);
		System.out.println(Math.rint(M*t));
		for(int i=0;i<size;i++){
			for(int j=i+1;j<size;j++){
				int k = i*size+j-(i+1)*(i+2)/2;
				arr[k] = arr2[i][j];
				
			}
		}
	
		Tools.insertSort(arr);

		
		return arr[index];
	}
	
	//生成p(i)集合
	public static LinkedHashMap<String, Double> getp(double dc,HashMap<String, Integer> field){
		LinkedHashMap<String, Double> p_arr = new LinkedHashMap<String, Double>();
		Iterator iterator = field.entrySet().iterator();
		for(int i=0;i<size;i++){
			Double index = 0.0;
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			//由股东共现引起的数据大量为0决定了数据的特殊性，从而使得高斯距离的效果并不好，所以采取cutoff kernel来计算距离，效果不错
//			for(int j=0;j<size;j++){
//				double up =  Math.pow((arr2[i][j]/dc),2);
//
//				double tmp = Math.pow(Math.E, up);
//				index+=tmp;
//			}
			for(int j=0;j<size;j++){
				double up = arr2[i][j]-dc;
				double tmp = 0.0;
				if(up>=0){
					tmp = 1.0;
				}
				else{
					tmp = 0.0;
				}
				index+=tmp;
			}
			p_arr.put(key, index);
		}
		return p_arr;
	}
	
	//生成p(i)集合下标序q(i)
	public static ArrayList<Integer> getq(HashMap<String, Double> p_arr,HashMap<String, Integer> field){
		ArrayList<Integer> q_arr = new ArrayList<Integer>();
		p_arr = Tools.sortMap2(p_arr);
//		System.out.println(p_arr);
//		System.out.println("fie "+field);
		Iterator iterator = p_arr.keySet().iterator();
		while(iterator.hasNext()){
			String name = (String) iterator.next();
		//	System.out.println(name);
			int flag = field.get(name);
			q_arr.add(flag);
		}
		return q_arr;
	}
	
	//获取d(max)
		public static double getd_max(ArrayList<String[]> contents,int flag){
			double d_max = 1.0;
			
			for(int i=1;i<contents.size();i++){
				double d=0.0;
				String[] tmp = contents.get(i);
				if(!tmp[2].equals("")){
					 d = Double.parseDouble(tmp[2]);
				}
				else{
					 d = 0.0;
				}
				if(flag<0){
					if(d_max>d){
						d_max = d;
						}
					}
				if(flag>0){
					if(d_max<d){
						d_max = d;
					}
				}
				
			}
			return d_max;
		}
	
	//生成cta(i)和n(i)集合
	public static ArrayList<Object> getc_n(ArrayList<String[]> contents,ArrayList<Integer> q_arr){
		ArrayList<Object> arr = new ArrayList<Object>();
		size = arr2[0].length;
		double[] cta = new double[size];
		int[] n = new int[size];
		for(int i=1;i<size;i++){
			int tmp = q_arr.get(i);
			cta[tmp] = Dp_stock.getd_max(contents,-1);
			for(int j=0;j<i;j++){
				int tmp1 = q_arr.get(j);
				if(arr2[tmp][tmp1]>cta[tmp]){
					cta[tmp] = arr2[tmp][tmp1];
					n[tmp] = tmp1;
				}
			}
		}
	
		cta[q_arr.get(0)] = Tools.getMax(cta);
		arr.add(cta);
		arr.add(n);
		return arr;
	}
	
	//初始化c（i）集合
	public static int[] get_c(int num,ArrayList<Integer> q_arr){
		int[] c = new int[size];
		int cluster = 0;
		System.out.println(q_arr);
		for(int i=0;i<num;i++){
			int index = q_arr.get(i);
			
			
			c[index] = cluster;
			cluster++;
		}
		return c;
	}
	
	//处理非聚类中心点
	public static int[] process_c(int[] c,int[] n,ArrayList<Integer> q_arr){
		for(int i=0;i<c.length;i++){
			int index = q_arr.get(i);
			if(c[index]==0){
				c[index] = c[n[index]];
			}
		}
		return c;
	}
	
	//为每一个cluster赋halo和core
	public static double[] halo(int num,int[] c,Double dc,ArrayList<Integer> q_arr,HashMap<String, Integer> field,ArrayList<Dp_stock_entity> dp_stock_entities){
		double[] density = new double[num];
		HashMap<String, Double> p_arr = Dp_stock.getp(dc,field);
		ArrayList<String> name_list = new ArrayList<String>();
		Iterator<String> iterator = field.keySet().iterator();
		while(iterator.hasNext()){
			name_list.add(iterator.next());
		}
		for(int i=0;i<size-1;i++){
			for(int j=i+1;j<size;j++){
				if(c[i]!=c[j]&&arr2[i][j]<dc){					
					double p_aver = 0.5*(p_arr.get(name_list.get(i))+p_arr.get(name_list.get(j)));
					
					if(p_aver>density[dp_stock_entities.get(i).getCluster()]){
						density[dp_stock_entities.get(i).getCluster()] = p_aver;
					}
					if(p_aver>density[dp_stock_entities.get(j).getCluster()]){
						density[dp_stock_entities.get(j).getCluster()] = p_aver;
					}
				}
			}
		}
		return density;
	}
	
	//生成dp_stock_entity实体对象数列
	public static ArrayList<Dp_stock_entity> get_ett(HashMap<String, Integer> field){
		ArrayList<Dp_stock_entity> arrayList = new ArrayList<Dp_stock_entity>();
		Iterator iterator = field.keySet().iterator();
		while(iterator.hasNext()){
			String name = (String) iterator.next();
			Dp_stock_entity dp_stock_entity = new Dp_stock_entity();
			dp_stock_entity.setName(name);
			arrayList.add(dp_stock_entity);
		}
		return arrayList;
		
	}
	
	//标示halo
	public static void process_halo(ArrayList<Dp_stock_entity> arr,HashMap<String, Integer> field,HashMap<String, Double> p_arr,double[] density,int[] c){
		Iterator iterator = field.keySet().iterator();
		for(int i=0;i<size;i++){
			if(p_arr.get(iterator.next())<density[c[i]]){
				arr.get(i).setH(1);
			}			
		}
	}
	
//	//给实体类赋属性值
//	public static void put(ArrayList<Dp_stock_entity> arr,Object[] arr2,String flag){
//		for(int i=0;i<arr.size();i++){
//			Dp_stock_entity dp_stock_entity = arr.get(i);
//			if(flag.equals("cluster")){
//			dp_stock_entity.setCluster((Integer)arr2[i]);
//			}
//			else if(flag.equals("p")){
//				dp_stock_entity.setP((Double)arr2[i]);
//				}
//			else if(flag.equals("cluster")){
//				dp_stock_entity.setCta((Double)arr2[i]);
//				}
//		}
//	}
	
	
}

//用于list的排序器
class MyCompartor implements Comparator
{
//     @Override
//     public int compare(Object o1, Object o2)
//    {
//
//           Dp_stock_entity sdto1= (Dp_stock_entity )o1;
//           Integer tmp1 = (int) (sdto1.getP()*sdto1.getCta());
//           
//           Dp_stock_entity sdto2= (Dp_stock_entity )o2;
//           Integer tmp2 = (int) (sdto2.getP()*sdto2.getCta());
//           return tmp1.compareTo(tmp2);
//
//    }
     @Override
     public int compare(Object o1, Object o2)
    {

           Dp_stock_entity sdto1= (Dp_stock_entity )o1;
           Double tmp1 =  (sdto1.getP()*sdto1.getCta());
           
           Dp_stock_entity sdto2= (Dp_stock_entity )o2;
           Double tmp2 =  (sdto2.getP()*sdto2.getCta());
           return tmp1.compareTo(tmp2);

    }
}

