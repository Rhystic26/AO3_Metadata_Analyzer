package Ao3Analyzer;

import java.io.*;
import java.util.*;
import java.lang.Math;

/*
Notes from Katie:
    - How can we structure the application to take advantage of the scapegoat tree's extremely fast search times?
*/
class IntComparator implements Comparator<Integer>{
    public int compare(Integer n1, Integer n2){
		return n1 - n2;
    }
}

public class ScapegoatTree<Key, Value> implements SymbolTable<Key, Value>{
	
	private Node root;
	public int size;
    private int maxSize;
    private Node scapegoat;
    private double alpha; // Must be between 0.5 and 1
    private int maxDepth;

	public ScapegoatTree(double alpha){
		this.size = 0;
        this.alpha = alpha;
        this.maxSize = 0;
        this.maxDepth = 0;
	}

	private class Node{
        // DONE!
        private Key key;
        private Value val;
        private Node left;
        private Node right;
        private int size;
        private boolean isScapegoat;
		private int depth;

        private Node(Key key, Value val, Node left, Node right, int size, int depth){
            this.key = key;
            this.val = val;
            this.left = left;
            this.right = right;
            this.size = size;
            this.isScapegoat = false;
			this.depth = depth;
        } 
    }

	// super simple
    public boolean isEmpty(){
        return this.size == 0;
    }

	// don't know why we need this but ok
    public int size(){
        return this.size;
    }

    // Puts the key-value pair into the table 
    // The comparator is used to compare keys
    public void put(Key key, Value val, Comparator<Key> comparator){
        // System.out.println("Inserting key " + key);
        int depthCounter = 0;
        root = putHelper(key, val, comparator, root, depthCounter);
    }

    public Node putHelper (Key key, Value val, Comparator<Key> comparator, Node r, int depthCounter){
        // Inserts the key-value pair into a new node if a node in the correct insertion position does not exist
        // if(r !=null) System.out.println("Recursion point: node key " + r.key);
        if(r == null){
            // System.out.println ("Actually inserting key " + key);
            r = new Node(key, val, null, null, 1, depthCounter);
            this.size += 1;
            if(this.size > this.maxSize){
                this.maxSize = this.size;
            }
          
            if(depthCounter > maxDepth){
                this.maxDepth = depthCounter;
            }

            return r;
        }

        // Overwrites existing node value with new value if input key matches the key of an existing node
        if(comparator.compare(key, r.key) == 0){
            r = new Node(key, val, r.left, r.right, 1, depthCounter);
            return r;
        }

        // Recurses down left subtree
        if(comparator.compare(key, r.key) < 0){
            r.left = putHelper(key, val, comparator, r.left, depthCounter+1);
        }
        
        // Recurses down right subtree
        if(comparator.compare(key, r.key) > 0){
            r.right = putHelper(key, val, comparator, r.right, depthCounter+1);
        }

        // Checks to see if tree is alpha-height-balanced
        if(checkHeightBalance(this.root) == false){
            // Checks to see if the current node is eligible to be a scapegoat; if true, rebalances tree
            if(checkWeightBalance(r) == false){
                r.isScapegoat = true;
                rebalance(r, comparator, depthCounter);
            }
        }

        return r;
    }

    // Checks to see if a subtree originating from a given node is a-weight-balanced
    public boolean checkWeightBalance(Node r){
        return(weightBalanceSize(r.left) <= (this.alpha*weightBalanceSize(r)) && weightBalanceSize(r.right) <= (this.alpha*weightBalanceSize(r)));
    }

    // Finds the size of a subtree originating from a given node
    public int weightBalanceSize(Node r){
		return (r == null) ? 0 : weightBalanceSize(r.left) + weightBalanceSize(r.right) + 1;
    }

    // Rebalances tree using scapegoat node selected in put function as root
    public void rebalance(Node r, Comparator<Key> comparator, int depthCounter){
        // Makes a pseudo-hashtable of all key-value pairs in the scapegoat's subtree, sorted by key values
        // System.out.println("Rebalancing");
        ArrayList<Key> unsortedKeys = new ArrayList<Key>();
        ArrayList<Value> unsortedValues = new ArrayList<Value>();
        rebalanceTraversal(unsortedKeys, unsortedValues, r);
        ArrayList<Key> sortedKeys = new ArrayList<Key>(unsortedKeys);
        sortedKeys.sort(comparator);
        ArrayList<Value> sortedValues = new ArrayList<Value>();
        // System.out.println("Sorted rebalance keys: "+sortedKeys.toString());
        for(int i=0; i<unsortedValues.size(); i++){
            sortedValues.add((sortedKeys.indexOf(unsortedKeys.get(i))), unsortedValues.get(i));
        }
        // Deletes all nodes in the subtree (including the scapegoat) and replaces the scapegoat with the median key-value pair from the initial subtree
        r = rebalanceTraversalDelete(r);
        int medianIndex = sortedKeys.size()/2;
        // System.out.println("Median Index: " + medianIndex);
        // System.out.println(sortedKeys.toString());
        // System.out.println("Key at median index: "+sortedKeys.get(medianIndex));
        // r = new Node(sortedKeys.get(medianIndex), sortedValues.get(medianIndex), null, null, 1, depthCounter);
        r = rebalancePut(r, sortedKeys.get(medianIndex), sortedValues.get(medianIndex), comparator, depthCounter);
        // Inserts remaining key-value pairs into subtree in balanced order
        sortedKeys.remove(medianIndex);
        sortedValues.remove(medianIndex);
        // System.out.println("Rebalanced subtree root key "+r.key);
        for(int i=0; i<sortedKeys.size(); i++){
            r = rebalancePut(r, sortedKeys.get(i), sortedValues.get(i), comparator, depthCounter);
        }
    }

    public void rebalanceTraversal(ArrayList<Key> unsortedKeys, ArrayList<Value> unsortedValues, Node currentNode){
        if(currentNode == null){
            return;
        }

        rebalanceTraversal(unsortedKeys, unsortedValues, currentNode.left);
        unsortedKeys.add(currentNode.key);
        unsortedValues.add(currentNode.val);
        rebalanceTraversal(unsortedKeys, unsortedValues, currentNode.right);
    }

    public Node rebalanceTraversalDelete(Node currentNode){
        if(currentNode == null){
            return currentNode;
        }
        // Node l = currentNode.left;
        // Node r = currentNode.right;
        rebalanceTraversalDelete(currentNode.left);
        // currentNode = null;
        rebalanceTraversalDelete(currentNode.right);
       return null;
    }


    public Node rebalancePut(Node r, Key key, Value val, Comparator<Key> comparator, int depthCounter){
        return rebalancePutHelper(r, key, val, comparator, depthCounter);
    }

    public Node rebalancePutHelper(Node r, Key key, Value val, Comparator<Key> comparator, int depthCounter){
        // TODO
        // System.out.println("rebalancePut key " + key);
        if(r == null){
            // System.out.println("rebalancePut inserting key " + key);
            r = new Node(key, val, null, null, 1, depthCounter);
            this.size += 1;
            if(this.size > this.maxSize){
                this.maxSize = this.size;
            }
            if(depthCounter > maxDepth){
                this.maxDepth = depthCounter;
            }

            return r;
        }

        if(comparator.compare(key, r.key) == 0){
            r = new Node(key, val, r.left, r.right, 1, depthCounter);
            return r;
        }

        if(comparator.compare(key, r.key) < 0){
            r.left = rebalancePutHelper(r.left, key, val, comparator, depthCounter+1);
        }
        
        if(comparator.compare(key, r.key) > 0){
            r.right = rebalancePutHelper(r.right, key, val, comparator, depthCounter+1);
        }

        return r;
    }

    public boolean checkHeightBalance(Node root){
        // DONE!
        /*if(root == null){
            return true;
        }

        if(Math.abs(root.left.depth-root.right.depth) <= 1 && checkHeightBalance(root.left) && checkHeightBalance(root.right)){
            return true;
        }

        return false;*/
        double treeSizeLog = Math.abs(Math.log(this.size)/Math.log(1/this.alpha));
        return(maxDepth <= Math.floor(treeSizeLog));
    }

    /* 
	 * Returns the value paired with the key 
     * Returns null if the key is not in the table
	 * The comparator is used to compare keys
	 * Essentially a "Search operation" 
	 */
    public Value get(Key key, Comparator<Key> comparator){
		// it's like the regular BST
        return getHelper(root, key, comparator);
    }

	// me when recursion
	private Value getHelper(Node current, Key key, Comparator comparator) {
		// base case: search miss
		if (current == null) return null;

		// now compare
		int c = comparator.compare(key, current.key);

		// check if search hit, otherwise recurse
		if (c == 0) return current.val;
		// go left
		else if (c < 0) return getHelper(current.left, key, comparator);
		// go right
		else if (c > 0) return getHelper(current.right, key, comparator);
		// if we get out here there is a problem
		throw new RuntimeException("comparator is throwing null??");
	}

    // Returns true or false if the given key is/is not in the table
    public boolean contains(Key key, Comparator<Key> comparator){
		// this can just be get but checking nullness
        return get(key, comparator) != null;
    }

    /* 
	 * Given a key, deletes its corresponding node and value from the table
	 * Prints an error if the key is not in the table 
	 * The comparator is used to compare keys
	 */
    public void delete(Key key, Comparator<Key> comparator){
        if(contains(key, comparator) == false){
            System.out.println("Key not in tree");
            return;
        }
        root = deleteHelper(root, key, comparator);
    }

    public Node deleteHelper(Node r, Key key, Comparator<Key> comparator){
        if(r == null){
            return r;
        }

        if(comparator.compare(key, r.key) < 0){
            r.left = deleteHelper(r.left, key, comparator);
        }
        
        if(comparator.compare(key, r.key) > 0){
            r.right = deleteHelper(r.right, key, comparator);
        }

        if(comparator.compare(key, r.key) == 0){
            if(r.left == null && r.right == null){
                r = null;
                return r;
            }

            if(r.left == null){
                r = r.right;
                return r;
            }

            if(r.right == null){
                r = r.left;
                return r;
            }

            if(r.left != null && r.right != null){
                r.right = deleteHelper(r.right, r.key, comparator);
                return r;
            }
        }
        return r;
    }

    // Returns the node with the smallest key in a given tree
    public Node getMinKeyNodeInRightSubtree(Node root){

        return getMinKeyNodeInRightSubtreeHelper(root.right);
    }

    public Node getMinKeyNodeInRightSubtreeHelper(Node root){
        if(root.left == null){
            return root;
        }
        
        return getMinKeyNodeInRightSubtreeHelper(root.left);
    }

    public Node removeNodeExcludeRoot(Node root, Key key, Comparator<Key> comparator){
        return deleteHelper(root.right, key, comparator);
    }

    public ArrayList<Key> inOrderTraversalKeys(int n){
        ArrayList<Key> keys = new ArrayList<Key>();
        inOrderTraversalKeysHelper(keys, this.root, n);
        return keys;
    }

    public void inOrderTraversalKeysHelper(ArrayList<Key> keys, Node currentNode, int n){
        if(currentNode == null || keys.size() >= n) return;

        inOrderTraversalKeysHelper(keys, currentNode.left, n);
        keys.add(currentNode.key);
        inOrderTraversalKeysHelper(keys, currentNode.right, n);
    }

	public ArrayList<Value> inOrderTraversalValues(int n){
        ArrayList<Value> values = new ArrayList<Value>(n);
        inOrderTraversalValuesHelper(values, this.root, n);
        return values;
    }

	private void inOrderTraversalValuesHelper(ArrayList<Value> values, Node currentNode, int n){
        if(currentNode == null || values.size() >= n) return;

        inOrderTraversalValuesHelper(values, currentNode.left, n);
		if (values.size() >= n) return;
        values.add(currentNode.val);
        inOrderTraversalValuesHelper(values, currentNode.right, n);
    }

    public static void main(String[] args) {
        ScapegoatTree<Integer, String> sgt = new ScapegoatTree<Integer, String>(0.5);
        IntComparator ic = new IntComparator();
        sgt.put(6, "f", ic);
        sgt.put(1, "a", ic);
        sgt.put(4, "d", ic);
        sgt.put(2, "b", ic);
        sgt.put(3, "c", ic);
        sgt.put(5, "e", ic);
        System.out.println(sgt.root.key);
        sgt.delete(4, ic);
        System.out.println((sgt.inOrderTraversalKeys(5)).toString());
        String ans = sgt.get(1, ic);
        System.out.println(ans);
    }
}
