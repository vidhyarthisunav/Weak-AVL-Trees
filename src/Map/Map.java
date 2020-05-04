package col106.assignment4.Map;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;

public class Map<V> {
	
	/*public static void main(String[] args) {
		Map<Integer> M = new Map<Integer>();
		M.eval("map_input","map_output");
	}*/
	public Map() {}

	public void eval(String inputFileName, String outputFileName){
		BufferedReader reader;
		String key;
		int value;
		long W_insert = 0, H_insert = 0 ,H_delete = 0 ,W_delete = 0;
		long time_1, time_2;
		try {
			reader = new BufferedReader(new FileReader(inputFileName));
			String line = reader.readLine();
			HashMap<Integer> H = new HashMap<Integer>(Integer.parseInt(line.trim()));
			WeakAVLMap<String,Integer> W = new WeakAVLMap<String,Integer>();
			while(line!=null){
				String[] S = line.split(" ");
				if(S[0].equals("I")){
					key = S[1];
					value = Integer.parseInt(S[2]);
					time_1 = System.currentTimeMillis();
					W.put(key,value);
					time_2 = System.currentTimeMillis();
					W_insert += time_2-time_1;
					time_1 = System.currentTimeMillis();
					H.put(key,value);
					time_2 = System.currentTimeMillis();
					H_insert += time_2-time_1;
				}
				if(S[0].equals("D")){
					key = S[1];
					time_1 = System.currentTimeMillis();
					W.remove(key);
					time_2 = System.currentTimeMillis();
					W_delete += time_2-time_1;
					time_1 = System.currentTimeMillis();
					H.remove(key);
					time_2 = System.currentTimeMillis();
					H_delete += time_2-time_1;
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		try{    
			FileWriter fw=new FileWriter(outputFileName);  
			fw.write("Operations WAVL HashMap"+'\n'+"Insertions "+W_insert+" "+H_insert+'\n'+"Deletions "+W_delete+" "+H_delete);  
			fw.close();    
		   }catch(Exception e){   
		   e.printStackTrace();
	  }    
	}
}
