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
        if(n1 > n2) return 1;
        if(n1 < n2) return -1;
        return 0;
    }
}

public class ScapegoatTree<Key, Value> implements SymbolTable<Key, Value>{
	
	private Node root;
	private int size;
    private int maxSize;	// what is this for?? -alex
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
        return size == 0;
    }

    // Returns the number of keys 
    public int size(){
        // TODO
        return 0;
    }

    // Puts the key-value pair into the table 
    // The comparator is used to compare keys
    public void put(Key key, Value val, Comparator<Key> comparator){
        // TODO
        int depthCounter = 0;
        root = putHelper(key, val, comparator, root, depthCounter);
    }

    public Node putHelper (Key key, Value val, Comparator<Key> comparator, Node r, int depthCounter){
        // Change to self-balance

        if(r == null){
            r = new Node(key, val, null, null, 1, depthCounter);
            this.size += 1;
          
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
            r.left = putHelper(key, val, comparator, r.left, depthCounter+1);
        }
        
        if(comparator.compare(key, r.key) > 0){
            r.right = putHelper(key, val, comparator, r.right, depthCounter+1);
        }

        if(checkHeightBalance(this.root) == false){
            if(checkWeightBalance(r) == false){
                r.isScapegoat = true;
                rebalance(r, comparator, depthCounter);
            }
        }

        return r;
    }

    public boolean checkWeightBalance(Node r){
        return(weightBalanceSize(r.left) <= (this.alpha*weightBalanceSize(r)) && weightBalanceSize(r.right) <= (this.alpha*weightBalanceSize(r)));
    }

    public int weightBalanceSize(Node r){
        if(r == null){
            return 0;
        }

        return weightBalanceSize(r.left) + weightBalanceSize(r.right) + 1;
    }

    public void rebalance(Node r, Comparator<Key> comparator, int depthCounter){
        // TODO
        ArrayList<Key> unsortedKeys = new ArrayList<Key>();
        ArrayList<Value> unsortedValues = new ArrayList<Value>();
        rebalanceTraversal(unsortedKeys, unsortedValues, r);
        ArrayList<Key> sortedKeys = new ArrayList<Key>(unsortedKeys);
        sortedKeys.sort(comparator);
        ArrayList<Value> sortedValues = new ArrayList<Value>();
       
        for(int i=0; i<unsortedValues.size(); i++){
            sortedValues.add((sortedKeys.indexOf(unsortedKeys.get(i))), unsortedValues.get(i));
        }

        rebalanceTraversalDelete(r);
        int medianIndex = sortedKeys.size()/2;
        r = new Node(sortedKeys.get(medianIndex), sortedValues.get(medianIndex), null, null, 1, depthCounter);
        for(int i=0; i<sortedKeys.size(); i++){
            rebalancePut(r, sortedKeys.get(i), sortedValues.get(i), comparator, depthCounter);
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

    public void rebalanceTraversalDelete(Node currentNode){
        if(currentNode == null){
            return;
        }
        Node l = currentNode.left;
        Node r = currentNode.right;
        rebalanceTraversalDelete(l);
        currentNode = null;
        rebalanceTraversalDelete(r);
    }


    public void rebalancePut(Node r, Key key, Value val, Comparator<Key> comparator, int depthCounter){
        r = rebalancePutHelper(r, key, val, comparator, depthCounter);
    }

    public Node rebalancePutHelper(Node r, Key key, Value val, Comparator<Key> comparator, int depthCounter){
        // TODO

        if(r == null){
            r = new Node(key, val, null, null, 1, depthCounter);
            this.size += 1;
          
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
            r.left = putHelper(key, val, comparator, r.left, depthCounter+1);
        }
        
        if(comparator.compare(key, r.key) > 0){
            r.right = putHelper(key, val, comparator, r.right, depthCounter+1);
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
        double treeSizeLog = Math.log(this.size)/Math.log(1/this.alpha);
        return(maxDepth <= Math.floor(treeSizeLog));
    }

    public double floorOops(double f){
        // TODO
        return 1.0;
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
        // TODO
        System.out.println("Not implemented yet");
    }

    public ArrayList<Key> inOrderTraversalKeys(){
        ArrayList<Key> keys = new ArrayList<Key>();
        inOrderTraversalKeysHelper(keys, this.root);
        return keys;
    }

    public void inOrderTraversalKeysHelper(ArrayList<Key> keys, Node currentNode){
        if(currentNode == null){
            return;
        }
        inOrderTraversalKeysHelper(keys, currentNode.left);
        keys.add(currentNode.key);
        inOrderTraversalKeysHelper(keys, currentNode.right);
    }

    public static void main(String[] args) {
        ScapegoatTree<Integer, String> sgt = new ScapegoatTree<Integer, String>(0.5);
        IntComparator ic = new IntComparator();
        sgt.put(4, "d", ic);
        sgt.put(1, "a", ic);
        sgt.put(5, "e", ic);
        sgt.put(2, "b", ic);
        ArrayList<Integer> k1 = sgt.inOrderTraversalKeys();
        System.out.println(k1.toString());   
        sgt.put(3, "c", ic);
        ArrayList<Integer> k2 = sgt.inOrderTraversalKeys();
        System.out.println(k2.toString()); 

    }
}
