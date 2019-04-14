package bplus;

import java.util.ArrayList;
/**
 *
 * @author harsh
 */
public class Node {
	protected boolean isLeaf;
	protected ArrayList<Integer> keys;

	public boolean isOverflowed() {
		return keys.size() > Tree.DEGREE-1;
	}

	public boolean isUnderflowed() {
		return keys.size() < Math.ceil(Tree.DEGREE/2f)-1;
	}

}
