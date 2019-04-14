/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bplus;

import static bplus.Tree.DEGREE;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author harsh
 */
public class HandlerClass {

    // Recursively find the leaf node where we have to insert value 
    public Map.Entry<Integer, Node> insertHandler(Node node, int key, Double value) {
        Map.Entry<Integer, Node> overflow = null;
        if (node.isLeaf) {
            Leaf leaf = (Leaf) node;
            leaf.insertInSortedOrder(key, value);
            if (leaf.isOverflowed()) {
                //handling overflow condition by splitting leaf
                Map.Entry<Integer, Node> rightSplit = splitLeaf(leaf);
                return rightSplit;
            }
            return null;
        } else {
            //handling index nodes
            Index idxNode = (Index) node;
            if (key < node.keys.get(0)) {
                overflow = insertHandler(idxNode.children.get(0), key, value);
            } else if (key >= node.keys.get(idxNode.keys.size() - 1)) {
                overflow = insertHandler(idxNode.children.get(idxNode.children.size() - 1), key, value);
            } else {
                for (int i = 0; i < idxNode.children.size(); i++) {
                    if (idxNode.keys.get(i) > key) {
                        //inserting at middle
                        overflow = insertHandler(idxNode.children.get(i), key, value);
                        break;
                    }
                }
            }
        }
        if (overflow != null) {
            //handling insert for overflowed key
            Index idxNode = (Index) node;
            int splittingKey = overflow.getKey();
            int indexAtParent = idxNode.keys.size();
            if (splittingKey < idxNode.keys.get(0)) {
                indexAtParent = 0;
            } else if (splittingKey > idxNode.keys.get(idxNode.keys.size() - 1)) {
                indexAtParent = idxNode.children.size();
            } else {
                for (int i = 0; i < idxNode.keys.size(); i++) {
                    if (i < idxNode.keys.get(i)) {
                        indexAtParent = i;
                    }
                }
            }

            idxNode.insertSorted(overflow, indexAtParent);
            if (idxNode.isOverflowed()) {
                Map.Entry<Integer, Node> rightSplit = splitIndex(idxNode);
                return rightSplit;
            }
            return null;
        }
        return overflow;

    }
// Recursively serach for the specified key

    public Leaf searchHandler(Node root, int key) {
        if (root == null) {
            return null;
        } else if (root.isLeaf) {
            return (Leaf) root;
        } else {
            Index index = (Index) root;

            // If key is less than the 1st key then traverse to left child
            if (key < index.keys.get(0)) {
                return searchHandler(index.children.get(0), key);
            } else if (key >= (index.keys.get(index.keys.size() - 1))) {
                return searchHandler(index.children.get(index.children.size() - 1), key);
            } else {
                for (int i = 1; i < index.keys.size(); i++) {
                    if (index.keys.get(i).compareTo(key) > 0) {
                        // Traverse to find the leafNode
                        return searchHandler(index.children.get(i), key);
                    }
                }
            }

        }
        return null;
    }
// linking the sibling at leaf node

    public void modifySiblingLink(Leaf left, Leaf right) {
        if (left.nextNode != null) {
            right.nextNode = left.nextNode;
        }
        left.nextNode = right;
    }

    // Split the leaf node in case of overflow and return entry object of right key and right node
    public Entry<Integer, Node> splitLeaf(Leaf leaf) {
        int RIGHT_BUCKET_SIZE = (int) (Math.ceil(Tree.DEGREE / 2f) - 1);

        ArrayList<Integer> rightKeys = new ArrayList<Integer>(RIGHT_BUCKET_SIZE);
        ArrayList<Double> rightValues = new ArrayList<Double>(RIGHT_BUCKET_SIZE);
        //copy the right side keys and values to new list
        rightKeys.addAll(leaf.keys.subList(RIGHT_BUCKET_SIZE, leaf.keys.size()));
        rightValues.addAll(leaf.values.subList(RIGHT_BUCKET_SIZE, leaf.values.size()));

        // delete the keys which are copied to right node
        leaf.keys.subList(RIGHT_BUCKET_SIZE, leaf.keys.size()).clear();
        leaf.values.subList(RIGHT_BUCKET_SIZE, leaf.values.size()).clear();

        Leaf rightLeaf = new Leaf(rightKeys, rightValues);
        modifySiblingLink(leaf, rightLeaf);

        return new AbstractMap.SimpleEntry<Integer, Node>(rightLeaf.keys.get(0), rightLeaf);

    }

    //  Split the Index node in case of overflow and return entry object of right key and right node
    public Entry<Integer, Node> splitIndex(Index index) {
        int BUCKET_SIZE = (int) (Math.ceil(Tree.DEGREE / 2f) - 1);
        ArrayList<Integer> rightKeys = new ArrayList<Integer>(BUCKET_SIZE);
        ArrayList<Node> rightChildren = new ArrayList<Node>(BUCKET_SIZE + 1);
        //copy the right side keys and values to new list
        rightKeys.addAll(index.keys.subList(BUCKET_SIZE, index.keys.size()));
        rightChildren.addAll(index.children.subList(BUCKET_SIZE, index.children.size()));
        Index rightNode = new Index(rightKeys, rightChildren);
        AbstractMap.SimpleEntry<Integer, Node> splitted = new AbstractMap.SimpleEntry<Integer, Node>(index.keys.get(BUCKET_SIZE - 1), rightNode);
        // delete the keys which are copied to right node
        index.keys.subList(BUCKET_SIZE, index.keys.size()).clear();
        index.children.subList(BUCKET_SIZE, index.children.size()).clear();
        return splitted;
    }
// Recursively find the leaf node to delete a given key
    public int deleteHandler(Index parent, Node node, int key) {
        int indexToDelete = -1;
        int indexInParent = -1;
        if (parent != null) {
            for (indexInParent = 0; indexInParent < parent.children.size(); indexInParent++) {
                if (parent.children.get(indexInParent) == node) {
                    break;
                }
            }
        }

        if (node.isLeaf) {
            Leaf leafNode = (Leaf) node;
            for (int i = 0; i < leafNode.keys.size(); i++) {
                //delete respective key and value in leaf node
                if (leafNode.keys.get(i) == key) {
                    leafNode.keys.remove(i);
                    leafNode.values.remove(i);
                    break;
                }
            }

            // check for underflow
            if (leafNode.isUnderflowed() && leafNode != Tree.rootNode) {
                // find index leafnode resides in parent
                if (indexInParent - 1 >= 0) {
                    // node has left child
                    Leaf left = (Leaf) parent.children.get(indexInParent - 1);
                    return leafUnderflowHandler(left, leafNode, parent);
                } else {
                    // node does not have left child
                    Leaf right = (Leaf) parent.children.get(indexInParent + 1);
                    return leafUnderflowHandler(leafNode, right, parent);
                }
            } else {
                // may need to update parents / ancestors if deleted splitting key
                if (leafNode.keys.size() > 0) {
                    updateIndexNodeKey(Tree.rootNode, key, leafNode.keys.get(0));
                }
                return -1; // delete did not cause underflow
            }

        } else {
            // Node is an IndexNode 
            Index idxNode = (Index) node;
            if (key < idxNode.keys.get(0)) {
                // go down first child
                indexToDelete = deleteHandler(idxNode, idxNode.children.get(0), key);
            } else if (key >= idxNode.keys.get(idxNode.keys.size() - 1)) {
                // go down last child
                indexToDelete = deleteHandler(idxNode, idxNode.children.get(idxNode.children.size() - 1), key);
            } else {
                // go down the middle children
                for (int i = 0; i < idxNode.keys.size(); i++) {
                    if (idxNode.keys.get(i) > key) {
                        indexToDelete = deleteHandler(idxNode, idxNode.children.get(i), key);
                    }
                }
            }
        }

        // see if there is an index to delete remaining
        if (indexToDelete != -1) {
            if (node == Tree.rootNode) {
                return indexToDelete;
            }
            node.keys.remove(indexToDelete);

            // if removal caused underflow 
            if (node.isUnderflowed()) {
                // determine if node has left sibling
                Index left = (Index) node;
                Index right = (Index) node;

                // check to see if indexNode has sibling
                if (indexInParent - 1 >= 0) {
                    left = (Index) parent.children.get(indexInParent - 1);
                } else {
                    right = (Index) parent.children.get(indexInParent + 1);
                }
                return indexUnderflowHandler(left, right, parent);
            }
        }

        return -1;
    }

   
     // Handle underflow condition in leaf node
    public int leafUnderflowHandler(Leaf left, Leaf right,
            Index parent) {

        // merge
        if (left.keys.size() + right.keys.size() < DEGREE) {
            left.keys.addAll(right.keys);
            left.values.addAll(right.values);
            left.nextNode = right.nextNode;

            // delete the other node
            int indexInParent = parent.children.indexOf(right);
            parent.children.remove(indexInParent);
            return indexInParent - 1;
        }

        // re-distribute
        int childsIndexInParent;
        if (left.isUnderflowed()) {
            childsIndexInParent = parent.children.indexOf(right);
            // get the minimum key value of right
            left.insertInSortedOrder(right.keys.remove(0), right.values.remove(0));
        } else {
            childsIndexInParent = parent.children.indexOf(right);
            // get maximum key value of left
            right.insertInSortedOrder(left.keys.remove(left.keys.size() - 1), left.values.remove(left.values.size() - 1));
            parent.keys.set(childsIndexInParent - 1, parent.children.get(childsIndexInParent).keys.get(0));
        }
        parent.keys.set(childsIndexInParent - 1, parent.children.get(childsIndexInParent).keys.get(0));

        // update the parent's index key
        return -1;

    }

    
     //Handle underflow condition in index node

    public int indexUnderflowHandler(Index leftIndex,
            Index rightIndex, Index parent) {
        int separatingKey;
        int index;  
        for (index = 0; index < parent.keys.size(); index++) {
            if (parent.children.get(index) == leftIndex && parent.children.get(index + 1) == rightIndex) {
                break;
            }
        }

        separatingKey = parent.keys.get(index);

        // Action : merge
        if (leftIndex.keys.size() + rightIndex.keys.size() < DEGREE) {
            // move separating key down 
            leftIndex.keys.add(separatingKey);
            leftIndex.keys.addAll(rightIndex.keys);

            leftIndex.children.addAll(rightIndex.children);

            // delete the right side
            parent.children.remove(parent.children.indexOf(rightIndex));
            return index;

        }

        // Action: Distribute
        if (leftIndex.isUnderflowed()) {
            // move separating key down to leftIndex
            leftIndex.keys.add(separatingKey);
            // move leftmost key from right up 
            parent.keys.set(index, rightIndex.keys.remove(0));
            // leftmost child of right is now left's
            leftIndex.children.add(rightIndex.children.remove(0));
        } else if (rightIndex.isUnderflowed()) {
            // move separating key down to rightIndex
            rightIndex.keys.add(0, separatingKey);
            // the last child of left index sibling is now right index's
            Node lastChild = leftIndex.children.remove(leftIndex.children.size() - 1);
            rightIndex.children.add(0, lastChild);
            // move rightmost key from leftIndex up
            parent.keys.set(parent.keys.size() - 1, leftIndex.keys.remove(leftIndex.keys.size() - 1));
        }

        return -1;
    }

    private void updateIndexNodeKey(Node theNode, int searchKey, int newKey) {
        if (theNode == null) {
            return;
        }

        if (theNode.isLeaf) {
            return;
        }

        Index idxNode = (Index) theNode;
        for (int i = 0; i < theNode.keys.size(); i++) {
            // not here, don't need to keep going

            if (idxNode.keys.get(i) > searchKey) {
                break;
            }

            if (idxNode.keys.get(i) == searchKey) {
                idxNode.keys.set(i, newKey);
                return;
            }
        }

        // not found, perhaps in another child
        if (searchKey < idxNode.keys.get(0)) {
            updateIndexNodeKey(idxNode.children.get(0), searchKey, newKey);
        } else if (searchKey > idxNode.keys.get(idxNode.keys.size() - 1)) {
            updateIndexNodeKey(idxNode.children.get(idxNode.children.size() - 1), searchKey, newKey);
        } else {
            for (int i = 0; i < theNode.keys.size(); i++) {
                if (idxNode.keys.get(i) > searchKey) {
                    updateIndexNodeKey(idxNode.children.get(i), searchKey, newKey);
                }
            }

        }
    }
}