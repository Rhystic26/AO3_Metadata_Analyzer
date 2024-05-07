package Ao3Analyzer;

import java.io.*;
import java.util.*;

/*
Notes from Katie:
    - How can we structure the application to take advantage of the scapegoat tree's extremely fast search times?
*/

public class ScapegoatTree<Key, Value> implements SymbolTable<Key, Value>{
	
	private Node root;
	private int size;
    private int maxSize;	// what is this for?? -alex
    private Node scapegoat;
    private int alpha; // Must be between 0.5 and 1

	public ScapegoatTree(int alpha){
		this.size = 0;
        this.alpha = alpha;
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
            if(checkHeightBalance() == false){
                rebalance(r);
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

    public void rebalance(Node insertedNode){
        // TODO
    }

    public boolean checkHeightBalance(){
        // TODO
        return null;
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

    public static void main(String[] args) {

    }
}
