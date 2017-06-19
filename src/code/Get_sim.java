package code;
import java.util.ArrayList;  
import java.util.List;  
public class Get_sim {
	
	  
	  
	  
	    List<Double> vector1 = new ArrayList<Double>();  
	    List<Double> vector2 = new ArrayList<Double>();  
	  
	    public Get_sim(String string1, String string2) {  
//	    	System.out.println(string1.length());
//	    	System.out.println(string2.length());
	        //把输入字符串中多个空格变为一个  
	        String[] vector1String = string1.trim().replaceAll("\\s+", " ").split(" ");  
	        String[] vector2String = string2.trim().replaceAll("\\s+", " ").split(" ");  
	  
	        for (String string : vector1String) {  
	            vector1.add(Double.parseDouble(string));  
	        }  
	        for (String string : vector2String) {  
	            vector2.add(Double.parseDouble(string));  
	        }  
	    }  
	  
	    // 求余弦相似度  
	    public double sim() {  
	        double result = 0;  
	        result = pointMulti(vector1, vector2) / sqrtMulti(vector1, vector2);  
	  
	        return result;  
	    }  
	  
	    private double sqrtMulti(List<Double> vector1, List<Double> vector2) {  
	        double result = 0;  
	        result = squares(vector1) * squares(vector2);  
	        result = Math.sqrt(result);  
	        return result;  
	    }  
	  
	    // 求平方和  
	    private double squares(List<Double> vector) {  
	        double result = 0;  
	        for (Double integer : vector) {  
	            result += integer * integer;  
	        }  
	        return result;  
	    }  
	  
	    // 点乘法  
	    private double pointMulti(List<Double> vector1, List<Double> vector2) {  
	        double result = 0;  
	        for (int i = 0; i < vector1.size(); i++) {  
	            result += vector1.get(i) * vector2.get(i);  
	        }  
	        return result;  
	    }  
	      
	    public static void main(String[] args) {  
	  
	        String string = "00112111100000000111000";  
	        String string2 = "00114111100000000111000";  
	        Get_sim computerDecition = new Get_sim(string,  
	                string2);  
	        System.out.println(computerDecition.sim());  
	    }  
	}  

