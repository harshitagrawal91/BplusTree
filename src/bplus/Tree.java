package bplus;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map.Entry;

/**
 *
 * @author harsh
 * UFID:9041-1685
 */
public class Tree {

    public static Node rootNode;
    public static int DEGREE = 3;
    HandlerClass helper = new HandlerClass();

    //Delete a pair with specefic key in B+Tree
    
    public void delete(int key) {
        int index = helper.deleteHandler(null, rootNode, key);
        if (index != -1) {
            rootNode.keys.remove(index);
            if (rootNode.keys.size() == 0) {
                rootNode = ((Index) rootNode).children.get(0);
            }
        }

        if (rootNode.keys.size() == 0) {
            rootNode = null;
        }
    }

    // function to get value for the specified search key
    
    public Double search(int key) {
        Leaf leaf = helper.searchHandler(rootNode, key);
        return (leaf.getValue(key));

    }
    
// function to get all the values for the specified range of keys

    public StringBuilder rangeSearch(Integer key1, Integer key2) {
        StringBuilder outputString = new StringBuilder();
        if (key1 == null || key2 == null || rootNode == null) {
            return outputString.append("");
        }
        // get the leaf node which is having the first key
        Leaf leaf = (Leaf) helper.searchHandler(rootNode, key1);

        int j = 0;
        while (key1.compareTo(leaf.keys.get(j)) > 0 && leaf.nextNode != null) {
            if (j == leaf.keys.size() - 1) {
                leaf = leaf.nextNode;
                j = 0;
            } else {
                j++;
            }
        }
        for (int i = 0; i < leaf.keys.size(); i++) {
            if (key1.compareTo(leaf.keys.get(i)) <= 0) {
                while (i < leaf.keys.size() && key2.compareTo(leaf.keys.get(i)) >= 0) {
                    //getting all the values in range and creating string of values
                    outputString.append(leaf.values.get(i) + ",");
                    i++;
                    if (i == leaf.keys.size() && key2.compareTo(leaf.keys.get(i - 1)) >= 0 && leaf.nextNode != null) {
                        i = 0;
                        //going to next leaf node if the key2 is not in current node
                        leaf = leaf.nextNode;
                    }
                }
            }
        }
        return outputString;
    }

    // function to Insert a given pair in a B+ tree
    
    public void insert(int key, Double value) {
        if (rootNode == null) {
            rootNode = new Leaf(key, value);
        }

        Entry<Integer, Node> overflowed = helper.insertHandler(rootNode, key, value);
        // handling condition if overflow occur at root level
        if (overflowed != null) {
            rootNode = new Index(overflowed.getKey(), rootNode, overflowed.getValue());
        }

    }

}
