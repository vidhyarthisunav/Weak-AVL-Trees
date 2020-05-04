//package col106.assignment4.WeakAVLMap;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

class Node<K,V>{
	V value;
	K key;
	int rank;
	Node Right = null;
	Node Left = null;
	Node(K key, V value){
		this.key = key;
		this.value = value;
		this.rank = 0;
	}
}
public class WeakAVLMap<K extends Comparable,V> {//implements WeakAVLMapInterface<K,V>{
	
	Node head;
	int rotate_count;
	/*public static void main(String[] args){
		WeakAVLMap<Integer,Integer> M = new WeakAVLMap<Integer,Integer>();
		M.put(2,41);
		M.put(69,72);
		M.put(83,55);
		M.remove(83);
		M.put(5,100);
		Vector<Integer> T = M.BFS();
		/*M.put(30886,92777);
		M.put(47793,38335);
		M.put(60492,16649);
		M.put(2362,90027);
		M.put(20059,97763);
		M.put(89172,55736);
		M.remove(89172);
		M.put(33069,98167);
		M.remove(47793);
		M.remove(2362);
		M.put(30886,59956);
		M.remove(30886);
		M.remove(33069);
		M.put(99932,95060);
		M.put(10012,36226);
		M.put(26652,60756);
		M.put(9441,53865);
		System.out.println(M.rotateCount());
		M.put(26652,79497);
		M.put(55306,64683);
		//System.out.println(M.getHeight());
		Vector<Integer> T = M.BFS();
		for(int i = 0; i < T.size();i++)
			System.out.println(T.elementAt(i));
	}*/
	public WeakAVLMap(){
		this.head = null;
		rotate_count = 0;
	}
	public V put(K key, V value){

		if(this.head == null){
			this.head = new Node(key, value);	
			this.head.rank = 1;
			return null;
		}

		V ans = Normal_BST(key, value);

		if(ans != null){
			return ans;
		}
		Stack<Node> S = new Stack<Node>();
		S.add(this.head);
		while(S.peek().key != key){

			Node temp1 = S.peek();
			if(key.compareTo(temp1.key) > 0){
				S.add(temp1.Right);
			}
			else{
				S.add(temp1.Left);
			}
		}

		while(!S.isEmpty()){

			Node current = S.pop();
			Node parent;
			try {
				parent = S.peek();
			} catch (Exception e) {
				current.rank += 1;
				break;
			}
		
			current.rank += 1;
			
			// if the diffrence bw any node and its parent that is being traversed in bottom up approach becomes 1
			// i.e. it was either a 2,1 node or 2,2 node - symmetric handled similarly
			if(parent.rank - current.rank == 1)
				break;

			// if the parent was a 1,1 node and now it is a 0,1 node - appropriate rotations - symmetric handled similarly
			if(parent.rank == current.rank){

				K current_key = (K)current.key;
				K parent_key = (K)parent.key;

				int parent_Left_rank, parent_Right_rank;

				if(parent.Left != null)
					parent_Left_rank = parent.Left.rank;
				else
					parent_Left_rank = 0;

				if(parent.Right != null)
					parent_Right_rank = parent.Right.rank;
				else
					parent_Right_rank = 0;

				if(current_key.compareTo(parent_key) > 0 && parent.rank - parent_Left_rank == 2){
					if(key.compareTo(current.key) > 0){
						rotate_count += 1;
						S.pop();
						Left_rotate(S, parent, key);
						break;
					}
					else{
						rotate_count += 2;
						Right_rotate(S, current, key);
						Left_rotate(S, parent, key);
						break;
					}
				}
				if(current_key.compareTo(parent_key) < 0 && parent.rank - parent_Right_rank == 2){
					if(key.compareTo(current.key) < 0){
						rotate_count += 1;
						S.pop();
						Right_rotate(S, parent, key);
						break;
					}
					else{
						rotate_count += 2;
						Left_rotate(S, current, key);
						Right_rotate(S, parent, key);
						break;
					}
				}
			}
		}
		return ans;
	}
	
	V Normal_BST(K key, V value){
		Node temp = this.head;
		outerloop:
		while(true){
			if(key.compareTo(temp.key) > 0){
				if(temp.Right == null){
					temp.Right = new Node(key, value);
					break outerloop;
				}
				else
					temp = temp.Right;
			}
			else if(key.compareTo(temp.key) < 0){
				if(temp.Left == null){
					temp.Left = new Node(key, value);
					break outerloop;
				}
				else
					temp = temp.Left;
			}
			else if(key.compareTo(temp.key) == 0){
				V temp_value = (V)temp.value;
				temp.value = value;
				return temp_value;
			}
		}
		return null;
	}
	void Left_rotate(Stack<Node> S, Node current, K key){
		Node x = current.Right;
		Node T2 = x.Left;

		current.rank = current.rank - 1;
		x.Left = current;
		current.Right = T2;

		x.Left.rank = 1 + max(getRank(x.Left.Right), getRank(x.Left.Left));
		x.rank = 1 + max(getRank(x.Right), getRank(x.Left));
					
		try {
			Node parent = S.pop();
			K key1 = (K)x.key;
			if(key1.compareTo(parent.key) > 0){
				parent.Right = x;
				}
			else{
				parent.Left = x;
			}
		} catch (Exception e) {
			this.head = x;
		}
	}
	
	void Right_rotate(Stack<Node> S, Node current, K key){

		Node x = current.Left;
		Node T2 = x.Right;

		x.Right = current;
		current.Left = T2;

		x.Right.rank = 1 + max(getRank(x.Right.Right), getRank(x.Right.Left));
		x.rank = 1 + max(getRank(x.Right), getRank(x.Left));
		try {
			Node parent = S.pop();
			K key1 = (K)x.key;
			if(key1.compareTo(parent.key) > 0){
				parent.Right = x;
			}
			else{
				parent.Left = x;
			}
		} catch (Exception e) {
			this.head = x;
		}	
	}

	public V remove(K key){
		Node temp = this.head;
		while(true){
			if(temp == null)
				return null;
			else if(key.compareTo(temp.key) > 0)
				temp = temp.Right;
			else if(key.compareTo(temp.key) < 0)
				temp = temp.Left;
			else
				 break;
		}
		V value = (V)temp.value;
		this.head = Delete(this.head, key);
    	return value;
	}
	Node Delete(Node root, K key){
		
		if(root == null)
			return root;
		else if(key.compareTo(root.key) < 0)
			root.Left = Delete(root.Left, key);

		else if(key.compareTo(root.key) > 0){
			root.Right = Delete(root.Right, key);}

		else{

			if(root.Left == null && root.Right == null){
				root = null;
			}
			else if (root.Left == null){
				root = root.Right;
			}
			else if (root.Right == null){
				root = root.Left;
			}
			else {
				Node temp = FindMin(root.Right);
				root.key = temp.key;
				root.value = temp.value;
				root.Right = Delete(root.Right, (K)temp.key);
				root.rank = getRank(root);
			}
		}
		if(root == null){
			return root;
		}
		
		int R_rank = 0, L_rank = 0;
        if(root.Right != null){
            R_rank = root.Right.rank;}

        if(root.Left != null)
            L_rank = root.Left.rank;	
            
		int R_diff = root.rank - R_rank, L_diff = root.rank - L_rank;
		
		// if parent of deleted node was a 2,1 or 1,2 node but now it is a 2,2 leaf node
        if(R_diff == 2 && L_diff == 2 && root.Left == null && root.Right == null){
			root.rank -= 1;
		}
		// if the parent of the deleted node was a 2,2 node and now it is a 3,2 node - symmetric also handled similarly
		if(L_diff == 3 && R_diff == 2){
			root.rank -= 1;
		}
		//if parent of deleted node was a 2,1 node and now it has become a 3,1 node (appropriate rotations) 
																							//- symmetric also handled similarly
		if(L_diff == 3 && R_diff == 1){
			int RR_rank = 0, RL_rank = 0;
			if(root.Right.Left != null)
				RL_rank = root.Right.Left.rank;
			if(root.Right.Right != null)
				RR_rank = root.Right.Right.rank;
			if(root.Right.rank - RL_rank == 1 && root.Right.rank - RR_rank == 1){
				rotate_count += 1;
				Node temp = Left_rotate1(root, key);
				temp.rank -= 1;
				temp.Left.rank += 1;
				return temp;
			}
			else if(root.Right.rank - RR_rank == 1){
				rotate_count += 1;
				Node temp = Left_rotate1(root, key);
				temp.rank -= 1;
				temp.Left.rank += 1;
				return temp;
			}
			else if(root.Right.rank - RL_rank == 1){
				rotate_count += 2;
				root.Right = Right_rotate1(root.Right, key);
				Node temp = Left_rotate1(root, key);
				temp.rank -= 2;
				temp.Right.rank += 1;
				temp.Left.rank += 2;
				return temp;
			}
		}
	
		if(L_diff == 2 && R_diff == 3){
			root.rank -= 1;
		}
		if(L_diff == 1 && R_diff == 3){
			int LR_rank = 0, LL_rank = 0;
			if(root.Left.Left != null)
				LL_rank = root.Left.Left.rank;
			if(root.Left.Right != null)
				LR_rank = root.Left.Right.rank;
			if(root.Left.rank - LL_rank == 1 && root.Left.rank - LR_rank == 1){
				rotate_count += 1;
				Node temp = Right_rotate1(root, key);
				temp.rank -= 1;
				temp.Right.rank += 1;
				return temp;
			}
			else if(root.Left.rank - LL_rank == 1){
				rotate_count += 1;
				Node temp = Right_rotate1(root, key);
				temp.rank -= 1;
				temp.Right.rank += 1;
				return temp;
			}
			else if(root.Left.rank - LR_rank == 1){
				rotate_count += 2;
				root.Left = Left_rotate1(root.Left, key);
				Node temp = Right_rotate1(root, key);
				temp.rank -= 2;
				temp.Left.rank += 1;
				temp.Right.rank += 2;
				return temp;

			}
		}
		return root;
	}

	Node FindMin(Node N){

		Node current = N;
		
		if(current.Left == null){
			return current;
		}

		current = current.Left;
		while(current.Left != null){
			current = current.Left;
		}
		return current;
	}
	Node Left_rotate1(Node current, K key){
		Node x = current.Right;
		Node T2 = x.Left;

		x.Left = current;
		current.Right = T2;

		
		//x.Left.rank = 1 + max(getRank(x.Left.Right), getRank(x.Left.Left));
		//x.rank = 1 + max(getRank(x.Right), getRank(x.Left));
					
		return x;
	}
	Node Right_rotate1(Node current, K key){

		Node x = current.Left;
		Node T2 = x.Right;

		x.Right = current;
		current.Left = T2;

		x.rank -= 1;
		x.Right.rank += 1;
		//x.Right.rank = 1 + max(getRank(x.Right.Right), getRank(x.Right.Left));
		//x.rank = 1 + max(getRank(x.Right), getRank(x.Left));
		
		return x;
	}

	int getRank(Node N){
		if(N == null)
			return 0;
		
		return N.rank;
	}
	public V get(K key){

		Node temp = this.head;
		while(true){
			if(key.compareTo(temp.key) > 0){
				if(temp.Right == null)
					return null;
				temp = temp.Right;
			}
			else if(key.compareTo(temp.key) < 0){
				if(temp.Left == null)
					return null;
				temp = temp.Left;
			}
			else
				break;
		}
		return (V)temp.value;
	}

	public Vector<V> searchRange(K key1, K key2){

		Vector<Node> traverse = new Vector<Node>();
		Inorder(this.head, traverse);
		Vector<V> ans = new Vector<V>();
		Boolean check = true;
		for(int i = 0; i < traverse.size(); i++)
			if( 0 <= key2.compareTo((K)traverse.elementAt(i).key)  && key1.compareTo((K)traverse.elementAt(i).key) <= 0){
				ans.add((V)traverse.elementAt(i).value);
				check = false;
			}
		if(check)
			ans.clear();
		return ans;
	}
	void Inorder(Node N, Vector<Node> traverse){
		if(N == null)
			return;
		Inorder(N.Left, traverse);
		traverse.add(N);
		Inorder(N.Right, traverse);
	}

	public int rotateCount(){
		return this.rotate_count;
	}

	public int getHeight(){
		try {
			return find_height(this.head);
		} catch (Exception e) {
			return 0;
		}
	}
	
	int find_height(Node N){
		if(N.Right == null && N.Left == null)
			return 1;
		if(N.Right == null)
			return 1+find_height(N.Left);
		if(N.Left == null)
			return 1+find_height(N.Right);
		return 1+max(find_height(N.Left),find_height(N.Right));
	}

	int max(int A, int B){
		if(A > B)
			return A;
		return B;
	}

	public Vector<K> BFS(){
		
		Vector ans = new Vector<K>();
		if(this.head == null){
			ans.clear();
			return ans;
		}		
		Queue<Node> Q = new LinkedList<Node>();
		Q.add(this.head);
		while(!Q.isEmpty()){
			Node temp = Q.peek();
			//System.out.println(temp.key+" "+temp.rank);
			ans.add(temp.key);
			if(temp.Left != null){
				Q.add(temp.Left);
			}
			if(temp.Right != null){
				Q.add(temp.Right);
			}
		Q.remove();
		}
		return ans;
	}
}