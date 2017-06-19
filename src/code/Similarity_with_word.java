package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.omg.CORBA.INTERNAL;

public class Similarity_with_word {
	
	
    
	
	//数组去重
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
	
	public static String[] sim(String s1,String s2){
		int sim = 0;
		ArrayList<String> arrayList_s1 = NlpirTest.result(s1);
		ArrayList<String> arrayList_s2 = NlpirTest.result(s2);
//		System.out.println(arrayList_s1);
//		System.out.println(arrayList_s2);
		Double count1 = (double) arrayList_s1.size();
		Double count2 = (double) arrayList_s2.size();
		
		LinkedHashMap<String, Integer> h1 = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Integer> h2 = new LinkedHashMap<String, Integer>();
		
		for(String s:arrayList_s1){
			if(h1.keySet().contains(s)){
				h1.replace(s, h1.get(s), h1.get(s)+1);
			}
			else{
				h1.put(s, 1);	
			}				
		}
		for(String s:arrayList_s2){
			if(h2.keySet().contains(s)){
				
				h2.replace(s, h2.get(s), h2.get(s)+1);
			}
			else{
				h2.put(s, 1);	
			}				
		}
		
		//公用词数组
		ArrayList<String> common = new ArrayList<String>();
		
		
		for(int i=0;i<arrayList_s1.size();i++){
			if(arrayList_s2.contains(arrayList_s1.get(i))){
				common.add(arrayList_s1.get(i));
			}
		}
		int common_size = removeDuplicate(common).size();
		for(int i=0;i<common.size();i++){
			int tmp1 = h1.get(common.get(i));
			h1.remove(common.get(i));
			h1.put(common.get(i), tmp1);
		}
		
		for(int i=0;i<common.size();i++){
			int tmp2 = h2.get(common.get(i));
			h2.remove(common.get(i));
			h2.put(common.get(i), tmp2);
		}
//		System.out.println(h1);
//		System.out.println(h2);
		String matrix1="",matrix2 = "";
		
		//形成词向量
		Iterator<String> I1 = h1.keySet().iterator();
		Iterator<String> I2 = h2.keySet().iterator();
		int matrix1_size = 0;
		int matrix2_size = 0;
		while(I1.hasNext()){
			String tmp = I1.next();
			matrix1_size++;
			//System.out.println("here"+h1.get(tmp));	
			//matrix1+=(h1.get(tmp));
			matrix1+=(h1.get(tmp)/count1)+" ";
		}
		
		
		while(I2.hasNext()){
			String tmp = I2.next();
			matrix2_size++;
			//System.out.println("here"+h2.get(tmp)/count2);	
			//matrix2+=(h2.get(tmp));
			matrix2+=(h2.get(tmp)/count2)+" ";
		}
//		System.out.println("1: "+matrix1);
//		System.out.println("2: "+matrix2);
//		System.out.println(common);
		int total_size = matrix1_size+matrix2_size-common_size;
		String[] matrix_1 = matrix1.split(" "); 
		matrix1 ="";
		for(int i=0;i<matrix1_size-common_size;i++){
			
			matrix1+=matrix_1[i]+" ";
			
		}
		//System.out.println("aaaaa"+matrix1);
		matrix1+=add0(total_size-matrix1_size);
		//System.out.println("bbbbb"+matrix1);
		for(int i=matrix1_size-common_size;i<matrix1_size;i++){
			matrix1+=matrix_1[i]+" ";
		}
		//System.out.println("ccccc"+matrix1);
//		matrix1 = matrix1.substring(0, matrix1_size-common_size)+add0(total_size-matrix1_size)+
//		matrix1.substring(matrix1_size-common_size,matrix1_size);
		
		matrix2 = add0(total_size-matrix2_size)+matrix2;
		
//		System.out.println("fuck:"+(total_size)+" "+matrix2_size+" "+common_size+" "+matrix1_size);
//		System.out.println("0:"+add0(total_size-matrix1_size));
//		System.out.println("aaa:"+matrix1.substring(0, matrix1_size-common_size));
//		System.out.println("ssss"+matrix1.substring(matrix1_size-common_size,matrix1_size));
//		System.out.println("common:+ "+common);
//		System.out.println("a: "+matrix1);
//		System.out.println("b: "+matrix2);
		
		String[] arr = {matrix1,matrix2};
		return arr;
	}
	
	public static String add0(int i){
		String str = "";
		for(int k=0;k<i;k++){
			str+="0 ";
		}
		return str;
	}
	
	public static void main(String[]args){
//		String s1 = "据悉，据悉据悉据悉据悉据悉质检质检质检质检质检总局总局总局总局已将最新有关情况再次通报美方，要求美方加强对输华玉米的产地来源、运输及仓储等环节的管控措施，有效避免输华玉米被未经我国农业部安全评估并批准的转基因品系污染。";
//		String s2 = "据悉，据悉据悉据悉据悉据悉质检质检质检质检质检总局总局总局总局动力方面：2017款宝马X6 V8的功率和动力特性很大程度上得益于双涡轮增压技术，"
//				+ "在8缸发动机上首次采用了两个涡轮以最大效率同时提升发动机功率和扭矩。一个涡轮增压器一次为4个气缸供给压缩空气，确保格外流畅的运转与对油门踏板的直接响应。"
//				+ "与采用双涡轮增压技术的直六发动机一样，传统涡轮增压发动机典型的“涡轮迟滞”，即涡轮增压器形成功率和增压压力所需的时间滞后，通过这一先进技术几乎被完全消除。";
		String s1 = "我是你的爸爸爸爸爸爸和妈妈";
		String s2 = "我是你的爸爸爸爸爸爸和妈妈";
		
		String matrix1 = sim(s1, s2)[0];
		String matrix2 = sim(s1, s2)[1];
		
		Get_sim get_sim = new Get_sim(matrix1, matrix2);
		double sim = get_sim.sim();
		System.out.println(sim); 

	}







}
