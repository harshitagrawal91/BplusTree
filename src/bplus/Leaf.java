package bplus;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author harsh
 */
public class Leaf extends Node {
	protected ArrayList<Double> values;
	protected Leaf nextNode;

	public Leaf(int firstKey, Double firstValue) {
		isLeaf = true;
		keys = new ArrayList<Integer>();
		values = new ArrayList<Double>();
		keys.add(firstKey);
		values.add(firstValue);

	}

	public Leaf(List<Integer> newKeys, List<Double> newValues) {
		isLeaf = true;
		keys = new ArrayList<Integer>(newKeys);
		values = new ArrayList<Double>(newValues);

	}

	/**
	 * insert key/value into this node so that it still remains sorted
	 * 
	 * @param key
	 * @param value
	 */
	public void insertInSortedOrder(int key, Double value) {
		if (key < keys.get(0)) {
			keys.add(0, key);
			values.add(0, value);
		} else if (key > keys.get(keys.size() - 1)) {
			keys.add(key);
			values.add(value);
		} else {
			ListIterator<Integer> iterator = keys.listIterator();
			while (iterator.hasNext()) {
				if (iterator.next() > key) {
					int position = iterator.previousIndex();
					keys.add(position, key);
					values.add(position, value);
					break;
				}
			}

		}
	}
        public Double getValue(int key) {
	  for (int ittratorKey: keys) {
      if (key==ittratorKey) {
        return values.get(keys.indexOf(ittratorKey));
      }
	  }
	  return null;
	}

}
