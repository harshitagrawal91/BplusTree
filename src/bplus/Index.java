/* Index Node class for creating index nodes*/
package bplus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author harsh
 */
public class Index extends Node {

	// m nodes
	protected ArrayList<Node> children; // m+1 children

	public Index(int key, Node firstChild, Node secondChild) {
		isLeaf = false;
		keys = new ArrayList<Integer>();
		keys.add(key);
		children = new ArrayList<Node>();
		children.add(firstChild);
		children.add(secondChild);
	}

	public Index(List<Integer> newKeys, List<Node> newChildren) {
		isLeaf = false;
		keys = new ArrayList<Integer>(newKeys);
		children = new ArrayList<Node>(newChildren);
	}

	/**
	 * insert the entry into this node at the specified index so that it still
	 * remains sorted
	 * 
	 * @param e
	 * @param index
	 */
	public void insertSorted(Entry<Integer, Node> e, int index) {
		int key = e.getKey();
		Node child = e.getValue();
		if (index >= keys.size()) {
			keys.add(key);
			children.add(child);
		} else {
			keys.add(index, key);
			children.add(index+1, child);
		}
	}

}
