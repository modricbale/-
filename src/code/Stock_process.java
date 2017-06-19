package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.tools.Tool;

public class Stock_process {
	public static void main(String args[]){
		String start = "2007-10-23";
		String next = start;
//		String end = Tools.get_next_year(start);	
		String end = "2007-11-11";	
		TreeMap<String, TreeMap<String,String>> hs = Connect_stock.connect("scrapy_news",start,end);
//		getcd("*ST中企 概念板块",hs,start,next,end);
		Set set = hs.keySet();
		Iterator<String> iterator = set.iterator();
		ArrayList<String[]> contents = new ArrayList<String[]>();
		ArrayList<String> judge = new ArrayList<String>();
		String[] headers ={"Source","Target","Value"};
		contents.add(headers);
		while(iterator.hasNext()){
			String stock_name = iterator.next();
			ArrayList<String[]> arr = getcd(stock_name, hs, start, next, end);
			for(int j=arr.size()-1;j>=0;j--){
				String tmp1 = arr.get(j)[0];
				String tmp2 = arr.get(j)[1];
				String tmp3 = arr.get(j)[2];				
				if(judge.contains(tmp2.trim()+tmp1.trim())){
					//System.out.println("1");
					arr.remove(j);
				}
				else{
					judge.add(tmp1.trim()+tmp2.trim());
				}
			}
			contents.addAll(arr);
		}
		Tools.write(contents,"info");
	}
	
	
	
	//获取超过阈值的股票集合
	public static ArrayList<String[]> getcd(String stock_name,TreeMap<String, TreeMap<String,String>> hs,String start,String next,String end){		
		ArrayList<String> date_arr = new ArrayList<String>();
		String cd="";
		date_arr.add(start);
		for(int i=0;;i++){
			next = Tools.get_next_day(next);		
			
			if(next.equals(end)){
				break;
			}
			date_arr.add(next);
		}
		//System.out.println(date_arr);
		//找出需要对比的股票
		TreeMap<String, String> tr1 = hs.get(stock_name);
		Set s = hs.keySet();
		Iterator it = s.iterator();
		ArrayList<String[]>arr = new ArrayList<String[]>();
		while(it.hasNext()){
			String name = (String) it.next();
			TreeMap<String, String> tr2 = hs.get(name);
			ArrayList<Double> r1 = Tools.getR(tr1,date_arr);
			ArrayList<Double> r2 = Tools.getR(tr2,date_arr);
			Double cd_tmp = Tools.getCd(r1, r2);
			cd = String.format("%.8f", cd_tmp);
			//System.out.println(name+": "+cd);
			System.out.println(cd);
			if(Double.parseDouble(cd)>0.94){
			String[] arr_tmp = {stock_name,name,cd};
			arr.add(arr_tmp);
			}
		}
		return arr;
//		TreeMap<String, String> tr1 = hs.get("*ST中企 概念板块");
//		TreeMap<String, String> tr2 = hs.get("新五丰 A 农林牧渔");
//		ArrayList<Double> r1 = Tools.getR(tr1,date_arr);
//		ArrayList<Double> r2 = Tools.getR(tr2,date_arr);
//		Double cd_tmp = Tools.getCd(r1, r2);
//		String cd = String.format("%.8f", cd_tmp);
//		System.out.println("haha: "+cd);
		
	}
	
}



class Connect_stock {
	static public TreeMap<String, TreeMap<String,String>> connect(String base,String start,String end){
		
		TreeMap<String, TreeMap<String,String>> hs = new TreeMap<String, TreeMap<String, String>>();
		String driver = "com.mysql.jdbc.Driver";
		//localhost指本机，也可以用本地ip地址代替，3306为MySQL数据库的默认端口号
		String url = "jdbc:mysql://localhost:3306/"+base;
		//填入数据库的用户名跟密码
		String username = "root";
		String password = "root";
		
		String sql = "select close,date,stock_name,first_category from "+base +" where date between "+"'"+start+"'"+" and "+"'"+end+"'" ;//编写要执行的sql语句，此处为从表中查询所有用户的信息
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
			String stock_name = rs.getString("stock_name")+" "+rs.getString("first_category");
			String date = rs.getString("date");
			String close = rs.getString("close");		
			if(hs.get(stock_name)==null){			
				TreeMap<String, String> hs_index = new TreeMap<String, String>();
				hs_index.put(date, close);
				hs.put(stock_name, hs_index);
			}
			else{
				TreeMap<String, String> hs_index = hs.get(stock_name);
				hs_index.put(date, close);
			}
//		System.out.println(""+base+rs.getString(2));//通过列的标号来获得数据
//		System.out.println("useradd: "+rs.getString("closep"));//通过列名来获得数据
//		System.out.println("userage: "+rs.getInt("userage"));
		}
	
	//	System.out.println(base+" "+data_base.get(4).size());
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
	
static public TreeMap<String, TreeMap<String,String>> connect2(String base,String start,String end){
		
		TreeMap<String, TreeMap<String,String>> hs = new TreeMap<String, TreeMap<String, String>>();
		String driver = "com.mysql.jdbc.Driver";
		//localhost指本机，也可以用本地ip地址代替，3306为MySQL数据库的默认端口号
		String url = "jdbc:mysql://localhost:3306/"+base;
		//填入数据库的用户名跟密码
		String username = "root";
		String password = "root";
		
		String sql = "select close,date,stock_name,first_category from "+base +" where date between "+"'"+start+"'"+" and "+"'"+end+"'" ;//编写要执行的sql语句，此处为从表中查询所有用户的信息
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
			String stock_name = rs.getString("stock_name");
			String date = rs.getString("date");
			String close = rs.getString("close");		
			if(hs.get(stock_name)==null){			
				TreeMap<String, String> hs_index = new TreeMap<String, String>();
				hs_index.put(date, close);
				hs.put(stock_name, hs_index);
			}
			else{
				TreeMap<String, String> hs_index = hs.get(stock_name);
				hs_index.put(date, close);
			}
//		System.out.println(""+base+rs.getString(2));//通过列的标号来获得数据
//		System.out.println("useradd: "+rs.getString("closep"));//通过列名来获得数据
//		System.out.println("userage: "+rs.getInt("userage"));
		}
	
	//	System.out.println(base+" "+data_base.get(4).size());
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
