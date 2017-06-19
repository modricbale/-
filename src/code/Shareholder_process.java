package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Shareholder_process {
	static int i=0;
	public static void main(String[] args){
//		TreeMap<String, TreeMap<String,ArrayList<Shareholder>>> hs = connect("shareholder", "shareholder2", "2009-01-01", "2018-01-01");
//		//按季度写入csv
//		Tools.write_csv(hs);
		
		
		TreeMap<String, TreeMap<String,ArrayList<Shareholder>>> hs_test = Tools.connect("shareholder", "shareholder2", "2015-06-30", "2015-10-01");
		//按董事会共现得到股票相关度并写入CSV
		Tools.get_similar_stock(hs_test,"2015-06-30", "2015-10-01");
		
		
		
	}
}
