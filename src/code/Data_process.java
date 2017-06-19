package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


class Connect{
	static public HashMap<String,ArrayList<News>> top_five(String str,String type,String str2,ArrayList<News> arr){
		HashMap<String,ArrayList<News>> hashMap = new HashMap<String,ArrayList<News>>();
		ArrayList<News> arrayList = new ArrayList<News>();
		String matrix1 = "";
		String matrix2 = "";
		for(int j=0;j<arr.size();j++){
			if(type.equals("title")){
				String title = arr.get(j).getTitle().trim();	
				matrix1 = Similarity_with_word.sim(str, title)[0];
				matrix2 = Similarity_with_word.sim(str, title)[1];
			}
			if(type.equals("news_content")){
				String news_content = arr.get(j).getNews_content().trim();	
				matrix1 = Similarity_with_word.sim(str, news_content)[0];
				matrix2 = Similarity_with_word.sim(str, news_content)[1];
			}
			
			Get_sim get_sim = new Get_sim(matrix1, matrix2);			
			double sim = get_sim.sim();
//			System.out.println(sim);
			if(sim>0.2){	
				System.out.println(sim);
				arrayList.add(arr.get(j));
			}
		}
		System.out.println(str2+": "+arrayList.size());
		hashMap.put(str2,arrayList);
		return hashMap;
	}
	
	static public ArrayList<ArrayList<String>> connect(String base){
		String driver = "com.mysql.jdbc.Driver";
		//localhost指本机，也可以用本地ip地址代替，3306为MySQL数据库的默认端口号，“user”为要连接的数据库名
		String url = "jdbc:mysql://localhost:3306/"+base;
		//填入数据库的用户名跟密码
		String username = "root";
		String password = "root";
		ArrayList<ArrayList<String>> data_base = new ArrayList<ArrayList<String>>();
		ArrayList<String> title = new ArrayList<String>();
		ArrayList<String> news_time = new ArrayList<String>();
		ArrayList<String> news_source = new ArrayList<String>();
		ArrayList<String> author = new ArrayList<String>();
		ArrayList<String> news_content = new ArrayList<String>();
		String sql = "select * from "+base;//编写要执行的sql语句，此处为从user表中查询所有用户的信息
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
		
		data_base.add(title);
		data_base.add(news_time);
		data_base.add(news_source);
		data_base.add(author);
		data_base.add(news_content);
		while(rs.next())//对结果集进行遍历输出
		{
			data_base.get(0).add(rs.getString("title"));
			data_base.get(1).add(rs.getString("news_time"));
			data_base.get(2).add(rs.getString("news_source"));
			data_base.get(3).add(rs.getString("author"));
			data_base.get(4).add(rs.getString("news_content"));
			
			
//		System.out.println(""+base+rs.getString(2));//通过列的标号来获得数据
//		System.out.println("useradd: "+rs.getString("news_content"));//通过列名来获得数据
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
		return data_base;
	}
}

class Final_connect implements Callable<ArrayList<ArrayList<String>>>{
	String name;

	public Final_connect(String name) {
	    super();
	    this.name = name;
	}
	
	static Object obj = new Object();
	
	@Override
	public ArrayList<ArrayList<String>> call() throws Exception {
		// TODO Auto-generated method stub
		//String thread_name = Thread.currentThread().getName();
		String web_name = name;
		//System.out.println(web_name);
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		synchronized (obj) {
			if(web_name.equals("eeo")){
				result = Connect.connect("eeo");
			}
			if(web_name.equals("cnstock")){
				result = Connect.connect("cnstock");
				}
			if(web_name.equals("cb")){
				result = Connect.connect("cb");
				}
			if(web_name.equals("ce")){
				result = Connect.connect("ce");
				}
			if(web_name.equals("cs")){
				result = Connect.connect("cs");
				}
		}
		return result;
	}	
}



public class Data_process {
	
	public static void main(String[] args)
	{
		String[] web_name = {"eeo","cnstock","cb","ce","cs"};
		int taskSize = 50000;
		ExecutorService pool = Executors.newFixedThreadPool(taskSize);
		HashMap<String, Future> hashMap = new HashMap<String,Future>();
		//List<Future> list = new ArrayList<Future>();
		for (int i = 0; i < 5; i++) {
			 Callable c = new Final_connect(web_name[i]);
			// 执行任务并获取Future对象
			 Future f = pool.submit(c);		
			 //list.add(f);
			 hashMap.put(web_name[i], f);
		}
		pool.shutdown();
		 // 获取所有并发任务的运行结果
		ArrayList<News> eeo = new ArrayList<News>();
		ArrayList<News> cnstock = new ArrayList<News>();
		ArrayList<News> cb = new ArrayList<News>();
		ArrayList<News> ce = new ArrayList<News>();
		ArrayList<News> cs = new ArrayList<News>();
		try {
			
			for(int k=0;k<((ArrayList<ArrayList<String>>)hashMap.get("eeo").get()).get(0).size();k++){
				News news = new News();
				news.setTitle(((ArrayList<ArrayList<String>>)hashMap.get("eeo").get()).get(0).get(k));			
				news.setNews_time(((ArrayList<ArrayList<String>>)hashMap.get("eeo").get()).get(1).get(k));
				news.setNews_source(((ArrayList<ArrayList<String>>)hashMap.get("eeo").get()).get(2).get(k));
				news.setAuthor(((ArrayList<ArrayList<String>>)hashMap.get("eeo").get()).get(3).get(k));
				news.setNews_content(((ArrayList<ArrayList<String>>)hashMap.get("eeo").get()).get(4).get(k));
				
				eeo.add(news);
			}
			
			for(int k=0;k<((ArrayList<ArrayList<String>>)hashMap.get("cnstock").get()).get(0).size();k++){
				News news = new News();
				news.setTitle(((ArrayList<ArrayList<String>>)hashMap.get("cnstock").get()).get(0).get(k));			
				news.setNews_time(((ArrayList<ArrayList<String>>)hashMap.get("cnstock").get()).get(1).get(k));
				news.setNews_source(((ArrayList<ArrayList<String>>)hashMap.get("cnstock").get()).get(2).get(k));
				news.setAuthor(((ArrayList<ArrayList<String>>)hashMap.get("cnstock").get()).get(3).get(k));
				news.setNews_content(((ArrayList<ArrayList<String>>)hashMap.get("cnstock").get()).get(4).get(k));
				cnstock.add(news);
			}
			
			for(int k=0;k<((ArrayList<ArrayList<String>>)hashMap.get("cb").get()).get(0).size();k++){
				News news = new News();
				news.setTitle(((ArrayList<ArrayList<String>>)hashMap.get("cb").get()).get(0).get(k));			
				news.setNews_time(((ArrayList<ArrayList<String>>)hashMap.get("cb").get()).get(1).get(k));
				news.setNews_source(((ArrayList<ArrayList<String>>)hashMap.get("cb").get()).get(2).get(k));
				news.setAuthor(((ArrayList<ArrayList<String>>)hashMap.get("cb").get()).get(3).get(k));
				news.setNews_content(((ArrayList<ArrayList<String>>)hashMap.get("cb").get()).get(4).get(k));
				cb.add(news);
			}
			
			for(int k=0;k<((ArrayList<ArrayList<String>>)hashMap.get("ce").get()).get(0).size();k++){
				News news = new News();
				news.setTitle(((ArrayList<ArrayList<String>>)hashMap.get("ce").get()).get(0).get(k));			
				news.setNews_time(((ArrayList<ArrayList<String>>)hashMap.get("ce").get()).get(1).get(k));
				news.setNews_source(((ArrayList<ArrayList<String>>)hashMap.get("ce").get()).get(2).get(k));
				news.setAuthor(((ArrayList<ArrayList<String>>)hashMap.get("ce").get()).get(3).get(k));
				news.setNews_content(((ArrayList<ArrayList<String>>)hashMap.get("ce").get()).get(4).get(k));
				ce.add(news);
			}
			
			for(int k=0;k<((ArrayList<ArrayList<String>>)hashMap.get("cs").get()).get(0).size();k++){
				News news = new News();
				news.setTitle(((ArrayList<ArrayList<String>>)hashMap.get("cs").get()).get(0).get(k));			
				news.setNews_time(((ArrayList<ArrayList<String>>)hashMap.get("cs").get()).get(1).get(k));
				news.setNews_source(((ArrayList<ArrayList<String>>)hashMap.get("cs").get()).get(2).get(k));
				news.setAuthor(((ArrayList<ArrayList<String>>)hashMap.get("cs").get()).get(3).get(k));
				news.setNews_content(((ArrayList<ArrayList<String>>)hashMap.get("cs").get()).get(4).get(k));
				cs.add(news);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String s1 = eeo.get(4).getNews_content().trim();
		String s2 = eeo.get(5).getNews_content().trim();
		
		String matrix1 = Similarity_with_word.sim(s1, s2)[0];
		String matrix2 = Similarity_with_word.sim(s1, s2)[1];
		
		Get_sim get_sim = new Get_sim(matrix1, matrix2);
//		System.out.println(matrix1);
//		System.out.println(matrix2);		
		double sim = get_sim.sim();
		//System.out.println("sim: "+sim);
		
		//计算相似度前五
		News news_tmp = eeo.get(0);
		String title = news_tmp.getTitle().trim();
		String news_content = news_tmp.getNews_content().trim();
		//寻找标题相似的新闻集合
//		HashMap<String, ArrayList<News>> hs_eeo_title = Connect.top_five(title,  "title","eeo_title", eeo);
//		HashMap<String, ArrayList<News>> hs_cnstock_title = Connect.top_five(title,  "title","cnstock_title", cnstock);
//		HashMap<String, ArrayList<News>> hs_cb_title = Connect.top_five(title, "title","cb_title", cb);
//		HashMap<String, ArrayList<News>> hs_ce_title = Connect.top_five(title, "title","ce_title", ce);
//		HashMap<String, ArrayList<News>> hs_cs_title = Connect.top_five(title, "title","cs_title", cs);
		//寻找文本相似的集合
		HashMap<String, ArrayList<News>> hs_eeo = Connect.top_five(news_content, "news_content","eeo_content", eeo);
//		HashMap<String, ArrayList<News>> hs_cnstock = Connect.top_five(news_content, "news_content", "cnstock_content", cnstock);
//		HashMap<String, ArrayList<News>> hs_cb = Connect.top_five(news_content, "news_content","cb_content", cb);
//		HashMap<String, ArrayList<News>> hs_ce = Connect.top_five(news_content, "news_content", "ce_content", ce);
//		HashMap<String, ArrayList<News>> hs_cs = Connect.top_five(news_content, "news_content", "cs_content", cs);
		
		}
	}














//
//package code;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//class Connect{
//	static public ArrayList<ArrayList<String>> connect(String base){
//		String driver = "com.mysql.jdbc.Driver";
//		//localhost指本机，也可以用本地ip地址代替，3306为MySQL数据库的默认端口号，“user”为要连接的数据库名
//		String url = "jdbc:mysql://localhost:3306/"+base;
//		//填入数据库的用户名跟密码
//		String username = "root";
//		String password = "root";
//		ArrayList<ArrayList<String>> data_base = new ArrayList<ArrayList<String>>();
//		ArrayList<String> title = new ArrayList<String>();
//		ArrayList<String> news_time = new ArrayList<String>();
//		ArrayList<String> news_source = new ArrayList<String>();
//		ArrayList<String> author = new ArrayList<String>();
//		ArrayList<String> news_content = new ArrayList<String>();
//		String sql = "select * from "+base;//编写要执行的sql语句，此处为从user表中查询所有用户的信息
//		try
//		{
//		Class.forName(driver);//加载驱动程序，此处运用隐式注册驱动程序的方法
//		}
//		catch(ClassNotFoundException e)
//		{
//		e.printStackTrace();
//		}
//		try
//		{
//		Connection con = DriverManager.getConnection(url,username,password);//创建连接对象
//		Statement st = con.createStatement();//创建sql执行对象
//		ResultSet rs = st.executeQuery(sql);//执行sql语句并返回结果集
//		
//		data_base.add(title);
//		data_base.add(news_time);
//		data_base.add(news_source);
//		data_base.add(author);
//		data_base.add(news_content);
//		while(rs.next())//对结果集进行遍历输出
//		{
//			data_base.get(0).add(rs.getString("title"));
//			data_base.get(1).add(rs.getString("news_time"));
//			data_base.get(2).add(rs.getString("news_source"));
//			data_base.get(3).add(rs.getString("author"));
//			data_base.get(4).add(rs.getString("news_content"));
//			
//			
////		System.out.println(""+base+rs.getString(2));//通过列的标号来获得数据
////		System.out.println("useradd: "+rs.getString("news_content"));//通过列名来获得数据
////		System.out.println("userage: "+rs.getInt("userage"));
//		
//		}
//		System.out.println(base+" "+data_base.get(4).size());
//		//关闭相关的对象
//		if(rs != null)
//		{
//		try
//		{
//		rs.close();
//		}
//		catch(SQLException e)
//		{
//		e.printStackTrace();
//		}
//		}
//		if(st != null)
//		{
//		try
//		{
//		st.close();
//		}
//		catch(SQLException e)
//		{
//		e.printStackTrace();
//		}
//		}
//		if(con !=null)
//		{
//		try
//		{
//		con.close();
//		}
//		catch(SQLException e)
//		{
//		e.printStackTrace();
//		}
//		}
//		}
//		catch(SQLException e)
//		{
//		e.printStackTrace();
//		}
//		return data_base;
//	}
//}
//
//class Final_connect implements Runnable{
//	static Object obj = new Object();
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		String thread_name = Thread.currentThread().getName();
//		synchronized (obj) {
//			if(thread_name.equals("eeo")){
//				Connect.connect("eeo");
//			
//			}
//			if(thread_name.equals("cnstock")){
//				Connect.connect("cnstock");
//				}
//			if(thread_name.equals("cb")){
//				Connect.connect("cb");
//				}
//			if(thread_name.equals("ce")){
//				Connect.connect("ce");
//				}
//			if(thread_name.equals("cs")){
//				Connect.connect("cs");
//				}
//		}
//		
//	}	
//}
//
//
//
//public class Data_process {
//	
//	public static void main(String[] args)
//	{
//		Final_connect final_connect = new Final_connect();
//		Thread t1 = new Thread(final_connect,"eeo");
//		Thread t2 = new Thread(final_connect,"cnstock");
//		Thread t3 = new Thread(final_connect,"cb");
//		Thread t4 = new Thread(final_connect,"ce");
//		Thread t5 = new Thread(final_connect,"cs");
//		t1.start();
//		t2.start();
//		t3.start();
//		t4.start();
//		t5.start();
//		}
//	}
