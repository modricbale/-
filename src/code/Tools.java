package code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.csvreader.CsvWriter;
import com.google.gson.Gson;

public class Tools {
	
	//找出数组最大值
	public static double getMax(double[] arr){
		double max=arr[0];
		for(int i=1;i<arr.length;i++){
			if(arr[i]>max){
		       max=arr[i];
		          }
		       }
		    return max;
		     }
	
	
	//hashmap按value排序
	public static LinkedHashMap<String, Integer> sortMap(Map<String, Integer> map){
	     List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
	     Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
	         public int compare(Map.Entry<String, Integer> obj1 , Map.Entry<String, Integer> obj2) {
	             return obj1.getValue() - obj2.getValue();
	         }
	     });
	     LinkedHashMap<String, Integer> field = new LinkedHashMap<String,Integer>();
			for(int i=0;i<entries.size();i++){
				field.put(entries.get(i).getKey(), entries.get(i).getValue());
			}
	      return field;
	    }
	
	public static LinkedHashMap<String, Double> sortMap2(Map<String, Double> map){
	     List<Map.Entry<String, Double>> entries = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
	     Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
	         public int compare(Map.Entry<String, Double> obj1 , Map.Entry<String, Double> obj2) {
	             return (int) (obj2.getValue() - obj1.getValue());
	         }
	     });
	     LinkedHashMap<String, Double> field = new LinkedHashMap<String,Double>();
			for(int i=0;i<entries.size();i++){
				field.put(entries.get(i).getKey(), entries.get(i).getValue());
			}
	      return field;
	    }
	
	//调换hashmap键值位置
	public static  HashMap<Integer, String> change(HashMap<String, Integer> hs){
		Iterator iterator = hs.keySet().iterator();
		HashMap<Integer, String> field2 = new HashMap<Integer, String>();
		while(iterator.hasNext()){
			String name = (String) iterator.next();
			Integer index = hs.get(name);
			field2.put(index, name);
		}
		return field2;
	}
	
	//数组插入排序
	public static double[] insertSort(double[] args){
		 for(int i=1;i<args.length;i++){
			 for(int j=i;j>0;j--){
		        if(args[j]<args[j-1]){
		            double temp=args[j-1];
		            args[j-1]=args[j];
		            args[j]=temp;        
		                   }
		        else break;
		                }
		         }
		return args;
		   }
	
	//写入文件
	public static void write(String str,String path,String date) throws IOException{
		File file =new File(path+date);  
		System.out.println(path+date);
        byte bt[] = new byte[1024];  
        bt = str.getBytes();  
        try {  
            FileOutputStream in = new FileOutputStream(file);  
            try {  
                in.write(bt, 0, bt.length);  
                in.close();  
                // boolean success=true;  
                // System.out.println("写入文件成功");  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
	}
	
	//取出二维数组字段
	public static HashMap<String, Integer> get_field(ArrayList<String[]> arr){
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		int index = 0;
		for(int i=1;i<arr.size();i++){
			String name = arr.get(i)[0];
			if(!(hashMap.keySet().contains(name))){
				hashMap.put(name, index);
				index++;
			}
			
		}
		return hashMap;
	}
	
	//为了dbscan得到二维数组数据
	public static Double[][] getarr2(HashMap<String, Integer> hs,ArrayList<String[]> arr){
		int size = hs.keySet().size();
		Double[][] arr2 =new Double[size][size]; 
		for(int j=1;j<arr.size();j++){
			String[] column = arr.get(j);
			String tmp1 = column[0];
			String tmp2 = column[1];		
			String cd = column[2];
			if(cd.equals("NaN")){
				cd="0.0";
			}
			String pose = column[3];
			int index1 = hs.get(tmp1);
			int index2 = hs.get(tmp2);
			if(cd.equals("")){
				arr2[index1][index2] = 0.0;
			}
			else{
				arr2[index1][index2] = Double.parseDouble(cd);
			}
		}
		return arr2;
	}
	
	
	
	
	
	
	//根据股东获取相关股票及相关度生成二维数组保存数据以供dbscan使用
		public static ArrayList<Object> get_similar_stock_generate_Arr2(TreeMap<String, 
				TreeMap<String,ArrayList<Shareholder>>> hs,TreeMap<String, TreeMap<String,String>> hs_stock,String start,String end){
			
			String next = start;
			
			ArrayList<String> date_arr = new ArrayList<String>();
			date_arr.add(start);
			for(int i=0;;i++){
				next = Tools.get_next_day(next);		
				
				if(next.equals(end)){
					break;
				}
				date_arr.add(next);
			}
			//获取所有股票
			 
			 ArrayList<String[]> contents = new ArrayList<String[]>();
			 String[] headers ={"Source","Target","cd","pose"};
			 contents.add(headers);
			 Set set = hs.keySet();
			 Iterator<String> iterator = set.iterator();
			 //存放所有股票股东关系结果
			 TreeMap<String, ArrayList<String>> arr = new TreeMap<String, ArrayList<String>>();
			 //存放股票名称集合
			 ArrayList<String>arr_name = new ArrayList<String>();
			 while(iterator.hasNext()){
				 ArrayList<String> arrayList = new ArrayList<String>();
				 String stock_name = iterator.next();
				 String[] tmp ={stock_name,stock_name,"","",};
				 contents.add(tmp);
				 arr_name.add(stock_name);
				 TreeMap<String,ArrayList<Shareholder>> tm = hs.get(stock_name);
			     Set set2 = tm.keySet();
			     Iterator<String> iterator2 = set2.iterator();
			     while(iterator2.hasNext()){
			    	 String date = iterator2.next();
			    	 ArrayList<Shareholder> aList = tm.get(date);
			    	 for(int i=0;i<aList.size();i++){
			    		 arrayList.add(aList.get(i).getCompany_name());
			    	 }
			     }
			     Tools.removeDuplicate(arrayList);
			     arr.put(stock_name, arrayList);
			 }
			 for(int j=0;j<arr_name.size()-1;j++){
				 String stock_name = arr_name.get(j);
				 ArrayList<String> a1 = arr.get(stock_name);
				 for(int k=j+1;k<arr_name.size();k++){
					int pose=0;
					String cmp_name = arr_name.get(k);
					ArrayList<String> a2 = arr.get(cmp_name);
					for(String s:a1){
						if(a2.contains(s)){
							pose++;
						}
					}	
					//如果有共同股东
					if(pose>0){
						TreeMap<String, String> tr1 = hs_stock.get(stock_name.trim());
						TreeMap<String, String> tr2 = hs_stock.get(cmp_name.trim());				
						if((tr1!=null)&&(tr2!=null)){
						ArrayList<Double> r1 = Tools.getR(tr1,date_arr);
						ArrayList<Double> r2 = Tools.getR(tr2,date_arr);
						Double cd_tmp = Tools.getCd(r1, r2);
						String cd = String.format("%.8f", cd_tmp);
						String[] tmp = {stock_name,cmp_name,cd,String.valueOf(pose)};
						contents.add(tmp);							
						}
					}
					
				 }
			 }
			 HashMap<String, Integer> field = Tools.get_field(contents);
			 Double[][] arr2 = Tools.getarr2(field, contents);
			 int size = arr2.length;
				for(int i=0;i<size;i++){
					for(int j=i;j<size;j++){
						if(arr2[i][j]==null && arr2[j][i]==null){							
							arr2[i][j]=0.0;
							arr2[j][i]=0.0;
						}
						else if(arr2[j][i]==null){							
							arr2[j][i]=arr2[i][j];
						}
						else if(arr2[i][j]==null){
							arr2[i][j]=arr2[j][i];
						}
						
					}
				}
			 ArrayList<Object> arr3 = new ArrayList<Object>();
			 arr3.add(field);
			 arr3.add(arr2);
			 arr3.add(contents);
			 return arr3;
		}
	
	
	
	
	
	
	
	
	
	//根据股东获取相关股票及相关度生成json
	public static String get_similar_stock_generate_json(TreeMap<String, 
			TreeMap<String,ArrayList<Shareholder>>> hs,TreeMap<String, TreeMap<String,String>> hs_stock,ArrayList<String[]> contents,
			ArrayList<Dbscan_stock_entity> result,String start,String end,double therehold){
		
		
		 //生成json
		 ArrayList<Node> arr_node = new ArrayList<Node>();
		 TreeMap<String, List> tm_d3 = new TreeMap<String, List>();
		 //(生成节点集合对象)
		 for(int j=0;j<result.size();j++){
			 Node node = new Node();
			 for(Dbscan_stock_entity d:result){
				 if(d.getName().equals(result.get(j).getName())){
					 node.setCluster(d.getCluster());
					 break;
				 }
			 }
			 node.setId(result.get(j).getName());
			 
			 arr_node.add(node);
		 }
		 tm_d3.put("nodes", arr_node);
		 
		 ArrayList<Links> arr_links = new ArrayList<Links>();
		 //(生成边数据集合)
		 for(int i=1;i<contents.size();i++){
			 String[] tmp = contents.get(i);
			 String source = tmp[0];
			 String target = tmp[1];
			 double label = 0.0;
			 if(!tmp[2].equals("")){
				 label = Double.parseDouble(tmp[2]);
				 if(Double.isNaN(label)){
					 label = 0.0;
				 }
			 }
			 int weight = 0;
			 if(!tmp[2].equals("")){
				 weight = Integer.parseInt(tmp[3]);
			 }
			 if(label>therehold){
			 Links links = new Links();
			 links.setLabel(label);
			 links.setSource(source);
			 links.setTarget(target);
			 links.setWeight(weight);
			 arr_links.add(links);
			 }
			 
		 }
		 
		 tm_d3.put("links", arr_links);
		 Gson gson = new Gson(); 
		 //生成最终json数据
		 String str_d3 = gson.toJson(tm_d3);
		 return str_d3;
	}
	
	

	
	
	//根据股东获取相关股票及相关度并写入csv
	public static void get_similar_stock(TreeMap<String, TreeMap<String,ArrayList<Shareholder>>> hs,String start,String end){
		String next = start;
		ArrayList<String> date_arr = new ArrayList<String>();
		date_arr.add(start);
		for(int i=0;;i++){
			next = Tools.get_next_day(next);		
			
			if(next.equals(end)){
				break;
			}
			date_arr.add(next);
		}
		//获取所有股票
		 TreeMap<String, TreeMap<String,String>> hs_stock = Connect_stock.connect2("scrapy_news",start, end);
		 ArrayList<String[]> contents = new ArrayList<String[]>();
		 String[] headers ={"Source","Target","cd","pose"};
		 contents.add(headers);
		 Set set = hs.keySet();
		 Iterator<String> iterator = set.iterator();
		 //存放所有股票股东关系结果
		 TreeMap<String, ArrayList<String>> arr = new TreeMap<String, ArrayList<String>>();
		 //存放股票名称集合
		 ArrayList<String>arr_name = new ArrayList<String>();
		 while(iterator.hasNext()){
			 ArrayList<String> arrayList = new ArrayList<String>();
			 String stock_name = iterator.next();
			 String[] tmp ={stock_name,stock_name,"","",};
			 contents.add(tmp);
			 arr_name.add(stock_name);
			 TreeMap<String,ArrayList<Shareholder>> tm = hs.get(stock_name);
		     Set set2 = tm.keySet();
		     Iterator<String> iterator2 = set2.iterator();
		     while(iterator2.hasNext()){
		    	 String date = iterator2.next();
		    	 ArrayList<Shareholder> aList = tm.get(date);
		    	 for(int i=0;i<aList.size();i++){
		    		 arrayList.add(aList.get(i).getCompany_name());
		    	 }
		     }
		     Tools.removeDuplicate(arrayList);
		     arr.put(stock_name, arrayList);
		 }
		 for(int j=0;j<arr_name.size()-1;j++){
			 String stock_name = arr_name.get(j);
			 ArrayList<String> a1 = arr.get(stock_name);
			 for(int k=j+1;k<arr_name.size();k++){
				int pose=0;
				String cmp_name = arr_name.get(k);
				ArrayList<String> a2 = arr.get(cmp_name);
				for(String s:a1){
					if(a2.contains(s)){
						pose++;
					}
				}
				
				if(pose>0){
					

					TreeMap<String, String> tr1 = hs_stock.get(stock_name.trim());
					TreeMap<String, String> tr2 = hs_stock.get(cmp_name.trim());
			
					if((tr1!=null)&&(tr2!=null)){
						
					ArrayList<Double> r1 = Tools.getR(tr1,date_arr);
				
					ArrayList<Double> r2 = Tools.getR(tr2,date_arr);
					Double cd_tmp = Tools.getCd(r1, r2);
					String cd = String.format("%.8f", cd_tmp);
					String[] tmp = {stock_name,cmp_name,cd,String.valueOf(pose)};
			
					contents.add(tmp);
					}
				
				}
				
			 }
		 }
		 
		 Tools.write(contents,"result");
		

	    }  
	
	
	//共现股东写入csv
	public static void write_csv(TreeMap<String, TreeMap<String,ArrayList<Shareholder>>> hs){
		
		 for(String start="2013-10-01";!start.contains("2018");start=Tools.get_next_three_month(start)){
			 
		 HashMap<String, ArrayList<Shareholder>> hMap = Tools.get_together(hs, start,Tools.get_next_three_month(start));	
		 //写入csv的内容
		 ArrayList<String[]> contents = new ArrayList<String[]>();
		 String[] headers ={"Source","Target","Company","date"};
		 contents.add(headers);
		 Set set = hMap.keySet();
		 Iterator<String> iterator = set.iterator();
		 while(iterator.hasNext()){		
			 
			 String name = iterator.next();
//			 System.out.println("hahaha:"+name);
//			 System.out.println(hMap.get(name).size());
			 ArrayList<Shareholder>arrayList = hMap.get(name);		 
			 for(int i=0;i<arrayList.size();i++){
				 System.out.println(arrayList.get(i).getCompany_name());
				 for(int j=i+1;j<arrayList.size();j++){
					 String[] str = {arrayList.get(i).getCompany_name(),arrayList.get(j).getCompany_name(),name,arrayList.get(i).getDate()};
					 contents.add(str);
				 }
			 }
			
		 }	
		 
		 Tools.write(contents, start+"起股东共现");
		 }
	}
	
	//获取下一天
	static String get_next_day(String today){
		 Calendar c = Calendar.getInstance();
		    Date date=null; 
		    try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(today);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		    c.setTime(date);
			c.add(Calendar.DATE, +1);
			Date tmp = c.getTime();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String next_day = df.format(tmp);
			return next_day;
	}
	
	//获取后三个月
	static String get_next_three_month(String today){
	 	Calendar c = Calendar.getInstance();
	    Date date=null; 
	    try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(today);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    c.setTime(date);
		c.add(Calendar.MONTH, +3);
		Date tmp = c.getTime();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String next_year = df.format(tmp);
		return next_year;
}
	
	//获取下一年
	static String get_next_year(String today){
		 	Calendar c = Calendar.getInstance();
		    Date date=null; 
		    try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(today);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		    c.setTime(date);
			c.add(Calendar.YEAR, +1);
			Date tmp = c.getTime();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String next_year = df.format(tmp);
			return next_year;
	}
	
	static ArrayList<Double> getR(TreeMap<String, String> hs,ArrayList<String> arr){
		//存放结果
		ArrayList<Double> res = new ArrayList<Double>();
		//arr存放日期
		//生成最终R
		String r = ""; 	
		for(int j=7;j<arr.size()-1;j++){
			String day = arr.get(j);
			String next = arr.get(j+1);
			String tmp1="",tmp2="";Double result=0.0;
			//保证第一天数据不为0
			if(hs.get(day)==null){
				//System.out.println("第一天数据为0");
				for(int i=1;i<7;i++){
				tmp1 = hs.get(arr.get(j-i));
					if(tmp1!=null){
						break;
					}
				}
				if(tmp1==null){
					result=0.0;
					res.add(result);
					continue;
				}
				//System.out.println("tmp: "+tmp1);
			}
			else{
				tmp1 = hs.get(day);
			}
			//填平没有数据的情况
			if(hs.get(next)==null){
				hs.put(next, tmp1);
				tmp2=tmp1;
			}
			else{
				tmp2 = hs.get(next);
			}
			
			result =  Math.log(Double.parseDouble(tmp2))- Math.log(Double.parseDouble(tmp1));
			res.add(result);
		}
		//System.out.println(res);
		return res; 
	}

	static Double getAver(ArrayList<Double> arr){
		int size = arr.size();
		Double total = 0.0;
		for(int j=0;j<arr.size();j++){
			total+=arr.get(j);
		}
		Double aver = (Double)total/size;
		return aver;
	}
	
	static Double getCd(ArrayList<Double> arr1,ArrayList<Double> arr2){
		Double r_aver1 = getAver(arr1);
		Double r_aver2 = getAver(arr2);
		//分子第一部分
		Double p1 = 0.0;
		//分子第二部分
		Double p2 = arr1.size()*r_aver1*r_aver2;
		for(int i=0;i<arr1.size();i++){
			p1+=arr1.get(i)*arr2.get(i);
		}
		//分子
		Double son = p1-p2;
		//分母第一部分
		Double m1_tmp = 0.0;
		for(int i=0;i<arr1.size();i++){
			m1_tmp+=Math.pow((arr1.get(i)-r_aver1),2);
		}
		Double m1=Math.sqrt(m1_tmp);
		//分母第二部分
		Double m2_tmp = 0.0;
		for(int i=0;i<arr2.size();i++){
			m2_tmp+=Math.pow((arr2.get(i)-r_aver2),2);
		}
		Double m2=Math.sqrt(m2_tmp);
		//分母
		Double m = m1*m2;
		Double cd = son/m;
		return cd;
	}
	//写入csv
	public static void write(ArrayList<String[]> arr,String name){
		 CsvWriter wr =new CsvWriter("C://Users//lenovo//Desktop//"+name+".csv",',',Charset.forName("GBK"));                 
		 try {
			for(int i=0;i<arr.size();i++){
				wr.writeRecord(arr.get(i));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 wr.close();
	 }
	//获取股东共现集合
	public static HashMap<String, ArrayList<Shareholder>>
	get_together(TreeMap<String, TreeMap<String,ArrayList<Shareholder>>> hs,String date_start,String date_end){
		HashMap<String, ArrayList<Shareholder>> tMap = new HashMap<String,ArrayList<Shareholder>>();
		Set set = hs.keySet();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()){
			ArrayList<Shareholder> arrayList = null;
			String stock_name = iterator.next();
//			System.out.println(stock_name);
			
			if(tMap.containsKey(stock_name)){
				arrayList = tMap.get(stock_name);
			}
			else{
				arrayList = new ArrayList<Shareholder>();
				tMap.put(stock_name, arrayList);
			}
			TreeMap<String,ArrayList<Shareholder>> tm = hs.get(stock_name);
			//System.out.println(tm.size());
			Set set_index = tm.keySet();
			Iterator<String> iterator2 = set_index.iterator();
			
			while(iterator2.hasNext()){
				String date_index = iterator2.next();
				if(Long.valueOf(date_start.replaceAll("[-\\s:]",""))<Long.valueOf(date_index.replaceAll("[-\\s:]","")) &&
						Long.valueOf(date_index.replaceAll("[-\\s:]",""))<Long.valueOf(date_end.replaceAll("[-\\s:]",""))
						){
					for(Shareholder s:tm.get(date_index)){
					arrayList.add(s);
					}
				}
			}	
			Tools.removeDuplicate(arrayList);
			
		}
		return tMap;
	}
	
	//去重
	public static List removeDuplicate(List list) {  
		   for ( int i = 0 ; i < list.size() - 1 ; i ++ ) {  
		     for ( int j = list.size() - 1 ; j > i; j -- ) {  
		       if (list.get(j).equals(list.get(i))) {  
		         list.remove(j);  
		       }   
		      }   
		    }   
		    return list;  
		}
	
static public TreeMap<String, TreeMap<String,ArrayList<Shareholder>>> connect(String base,String table,String start,String end){
		
		TreeMap<String, TreeMap<String,ArrayList<Shareholder>>> hs = new TreeMap<String, TreeMap<String,ArrayList<Shareholder>>>();
		String driver = "com.mysql.jdbc.Driver";
		//localhost指本机，也可以用本地ip地址代替，3306为MySQL数据库的默认端口号
		String url = "jdbc:mysql://localhost:3306/"+base;
		//填入数据库的用户名跟密码
		String username = "root";
		String password = "root";
		
		String sql = "select date,company_name,hold_num,hold_change,hold_percentage,"
				+ "hold_change_percentage,hold_type,stock_name,stock_id from "+table +" where date between "+"'"+start+"'"+" and "+"'"+end+"'" ;//编写要执行的sql语句，此处为从表中查询所有用户的信息
		System.out.println(sql);
		try
		{
		Class.forName(driver);//加载驱动程序，此处运用隐式注册驱动程序的方法
		}
		catch(ClassNotFoundException e)
		{
		e.printStackTrace();
		}
		try
		{
		Connection con = DriverManager.getConnection(url,username,password);//创建连接对象
		Statement st = con.createStatement();//创建sql执行对象
		ResultSet rs = st.executeQuery(sql);//执行sql语句并返回结果集	
		
		while(rs.next())//对结果集进行遍历输出
		{	
			Shareholder shareholder = new Shareholder();
			String date = rs.getString("date");
			String company_name = rs.getString("company_name");
			String hold_num = rs.getString("hold_num");
			String hold_change = rs.getString("hold_change");
			String hold_percentage = rs.getString("hold_percentage");
			String hold_change_percentage = rs.getString("hold_change_percentage");
			String hold_type = rs.getString("hold_type");
			String stock_name = rs.getString("stock_name");
			String stock_id = rs.getString("stock_id");
			shareholder.setCompany_name(company_name);
			shareholder.setDate(date);
			shareholder.setHold_change(hold_change_percentage);
			shareholder.setHold_change_percentage(hold_change_percentage);
			shareholder.setHold_num(hold_num);
			shareholder.setHold_percentage(hold_percentage);
			shareholder.setHold_type(hold_type);
			shareholder.setStock_id(stock_id);
			shareholder.setStock_name(stock_name);
			if(hs.get(stock_name)==null){			
				TreeMap<String, ArrayList<Shareholder>> hs_index = new TreeMap<String, ArrayList<Shareholder>>();
				ArrayList<Shareholder>arrayList = new ArrayList<Shareholder>();
				arrayList.add(shareholder);
				hs_index.put(date,arrayList);
				hs.put(stock_name, hs_index);
			}
			else{
				TreeMap<String, ArrayList<Shareholder>> hs_index = hs.get(stock_name);
				if(hs_index.containsKey(date)){
					ArrayList<Shareholder> arr = hs_index.get(date);
					arr.add(shareholder);
				}
				else{
					ArrayList<Shareholder>arr = new ArrayList<Shareholder>();
					arr.add(shareholder);
					hs_index.put(date, arr);
				}
			}
//		System.out.println(""+base+rs.getString(2));//通过列的标号来获得数据
//		System.out.println("useradd: "+rs.getString("closep"));//通过列名来获得数据
//		System.out.println("userage: "+rs.getInt("userage"));
		}

		//关闭相关的对象
		if(rs != null)
		{
		try
		{
		rs.close();
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		}
		if(st != null)
		{
		try
		{
		st.close();
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		}
		if(con !=null)
		{
		try
		{
		con.close();
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		}
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return hs;
	}

	
}
