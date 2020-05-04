//package col106.assignment4.HashMap;
import java.util.Vector;
import java.util.ArrayList;

class Pair<V>{
	String key;
	V value;
	int hash;
	Pair(String key, V value, int hash){
		this.key = key;
		this.value = value;
		this.hash = hash;
	}
}
public class HashMap<V> {//implements HashMapInterface<V>{
	Pair[] Map;
	/*public static void main(String[] args) {
		HashMap<Integer> M = new HashMap<Integer>(10);
		M.put("orange",10); //6
		M.put("jhhh",34); //9
		M.put("mansi",29); //8
		//M.put("randi",27);
		//M.put("aeiufmi",233);
		//M.put("qoiqrhqq",256);
		System.out.println(M.remove("jhhh"));
		Vector<String> S = M.getKeysInOrder();
		for(int i = 0 ;i < S.size();i++){
			System.out.println(S.elementAt(i));
		}
	}*/
	public HashMap(int size) {
		this.Map = new Pair[size];
	}

	public V put(String key, V value){
		int hash = find_hash(key);
		if(this.Map[hash] == null){
			this.Map[hash] = new Pair(key,value,hash);
			return null;
		}
		V previous = null;
		int count = this.Map.length, temp_hash = hash;
		while(count != 0){
			
			if(this.Map[temp_hash] == null){
				this.Map[temp_hash] = new Pair(key,value,hash);
				break;
			}
			else if(this.Map[temp_hash].key.equals(key)){
				previous = (V)this.Map[temp_hash].value;
				this.Map[temp_hash].value = value;
				break;
			}
			temp_hash += 1;
			if(temp_hash > this.Map.length-1)
				temp_hash = 0;
			count --;
		}
		return previous;
	}
	
	int find_hash(String key){

		int count = key.length()-2;
		long hash = (long)key.charAt(key.length()-1);
		while(count != -1){
			hash = ((long)key.charAt(count)+ 41*hash) % this.Map.length;
			count--;
		}
		return (int)(hash % this.Map.length);
	}

	public V get(String key){
		int hash = find_hash(key);
		int count = this.Map.length, temp_hash = hash;
		while(count != 0){
			if(this.Map[temp_hash] == null)
				break;
			if(this.Map[temp_hash].key.equals(key))
				return (V)this.Map[temp_hash].value;
			temp_hash += 1;
			if(temp_hash > this.Map.length-1)
				temp_hash = 0;
			count --;
		}
		return null;
	}

	public boolean remove(String key){
		int hash = find_hash(key);
		int count = this.Map.length, temp_hash = hash;
		while(count != 0){
			if(this.Map[temp_hash] == null)
				break;
			if(this.Map[temp_hash].key.equals(key)){
				int old = temp_hash;
				this.Map[temp_hash] = null;
				temp_hash += 1;
				if(temp_hash > this.Map.length - 1)
					temp_hash = 0;
				while(this.Map[temp_hash] != null){
					if(this.Map[temp_hash].hash == hash){
						this.Map[old] = this.Map[temp_hash];
						this.Map[temp_hash] = null;
						old = temp_hash;
					}
					temp_hash += 1;
					if(temp_hash > this.Map.length - 1)
						temp_hash = 0;
				}
				return true;
			}
			temp_hash += 1;
			if(temp_hash > this.Map.length-1)
				temp_hash = 0;
			count --;
		}
		return false;
	}

	public boolean contains(String key){
		int hash = find_hash(key);
		int count = this.Map.length, temp_hash = hash;
		while(count != 0){
			if(this.Map[temp_hash] == null)
				break;
			if(this.Map[temp_hash].key.equals(key))
				return true;
			temp_hash += 1;
			if(temp_hash > this.Map.length-1)
				temp_hash = 0;
			count --;
		}
		return false;
	}

	public Vector<String> getKeysInOrder(){
		Vector<String> ans = new Vector<String>();
		Boolean check = false;
		for(int i = 0; i< this.Map.length; i++){
			if(this.Map[i] != null){
				check = true;
				ans.add(this.Map[i].key);
			}
		}
		if(!check){
			ans.clear();
		}
		return ans;
	}
}
