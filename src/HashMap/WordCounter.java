package col106.assignment4.HashMap;

public class WordCounter {
	HashMap<Integer> H;
	/*public static void main(String[] args) {
		WordCounter W = new WordCounter();
		System.out.println(W.count("susunav", "su"));
	}*/
	public WordCounter(){}
	public int count(String str, String word){
		this.H = new HashMap<Integer>(str.length()-word.length()+1);
		int counter = 0;
		while(counter < str.length()-word.length()+1){
			try {
				int check = this.H.get(str.substring(counter, counter+word.length()));
				this.H.put(str.substring(counter, counter+word.length()),check+1);
			} catch (Exception e) {
				this.H.put(str.substring(counter, counter+word.length()),1);
			}
			counter++;
		}
		try {
			return this.H.get(word);
		} catch (Exception e) {
			return 0;
		}
	}
}
