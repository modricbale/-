package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import utils.SystemParas;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class NlpirTest {


	
	
	public static ArrayList<String> txt2String(File file){
        ArrayList<String>arrayList = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                arrayList.add(System.lineSeparator()+s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return arrayList;
    }
	
	
	
	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"F:\\汉语分词20140928\\lib\\win64\\NLPIR", CLibrary.class);
		
		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);
				
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
		public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
		public String NLPIR_GetLastErrorMsg();
		public void NLPIR_Exit();
	}

	public static String transString(String aidString, String ori_encoding,
			String new_encoding) {
		try {
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<String> result(String sInput){

		String argu = "../../";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;
		//处理停用词后的最后词组数列
		ArrayList<String>split_arraylist = new ArrayList<String>();
		//加载停用词表
		ArrayList<String> stopword = txt2String(new File("StopWordTable.txt"));
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;

		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is "+nativeBytes);
			return null;
		}

		//String sInput = "据悉，质检总局已将最新有关情况再次通报美方，要求美方加强对输华玉米的产地来源、运输及仓储等环节的管控措施，有效避免输华玉米被未经我国农业部安全评估并批准的转基因品系污染。";
		
		//String nativeBytes = null;
		try {
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput.replaceAll(" ", ""), 1);
			//System.out.println("分词结果为： " + nativeBytes);			
			String[] split_result = nativeBytes.split(" ");
			
			for(int i=0;i<split_result.length;i++){
				split_arraylist.add(split_result[i]);
			}
//			System.out.println("bef:"+split_arraylist);
//			System.out.println("bef:"+split_arraylist.size());
			for(int i=split_arraylist.size()-1;i>=0;i--){
					int index = split_arraylist.get(i).indexOf("/");
					if (index==-1)
						continue;
					//System.out.println(split_arraylist.get(i)+"  : "+index);
					String s1 = split_arraylist.get(i).substring(0, index);
					//System.out.println("once");
					for(String s_tmp:stopword){						
					if(s_tmp.trim().equals(s1.trim())){
						//System.out.print("s:"+s_tmp);
						//System.out.println(i);
						split_arraylist.remove(i);	
						break;
					}
				}
			}
			//System.out.println("aft:"+split_arraylist.size());
//			for(int i=0;i<split_arraylist.size();i++){
//				System.out.println(split_arraylist.get(i));
//			}
			
//			CLibrary.Instance.NLPIR_AddUserWord("要求美方加强对输 n");
//			CLibrary.Instance.NLPIR_AddUserWord("华玉米的产地来源 n");
//			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
//			//System.out.println("增加用户词典后分词结果为： " + nativeBytes);
//			
//			CLibrary.Instance.NLPIR_DelUsrWord("要求美方加强对输");
//			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
//			//System.out.println("删除用户词典后分词结果为： " + nativeBytes);
//			
//			
//			int nCountKey = 0;
//			String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput, 10,false);
//
//			//System.out.print("关键词提取结果是：" + nativeByte);
//
//			nativeByte = CLibrary.Instance.NLPIR_GetFileKeyWords("C:\\Users\\lenovo\\Desktop\\demo1.txt", 10,false);
//
//			//System.out.print("关键词提取结果是：" + nativeByte);
//
//			CLibrary.Instance.NLPIR_Exit();
//			
			
			
			

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
		return split_arraylist;
	}
	
	public static void main(String[] args) throws Exception {
		String sInput = "动力方面：2017款宝马X6 V8的功率和动力特性很大程度上得益于双涡轮增压技术，"
				+ "在8缸发动机上首次采用了两个涡轮以最大效率同时提升发动机功率和扭矩。一个涡轮增压器一次为4个气缸供给压缩空气，确保格外流畅的运转与对油门踏板的直接响应。"
				+ "与采用双涡轮增压技术的直六发动机一样，传统涡轮增压发动机典型的“涡轮迟滞”，即涡轮增压器形成功率和增压压力所需的时间滞后，通过这一先进技术几乎被完全消除。";
		ArrayList<String> arrayList = result(sInput.replaceAll(" ", ""));
	
		for(int i=0;i<arrayList.size();i++){
		System.out.println(arrayList.get(i));
		}
	}
}
