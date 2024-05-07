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

	public ScapegoatTree(){
		this.size = 0;
	}

	private class Node{
        // DONE!
        private Key key;
        private Value val;
        private Node left;
        private Node right;
        private int size;
        private boolean isScapegoat;

        private Node(Key key, Value val){
            this.key = key;
            this.val = val;
            this.left = null;
            this.right = null;
            this.size = 1;
            this.isScapegoat = false;
        } 

		// checking scapegoatedness is useful
		private boolean checkScapegoatedness() {
			int leftsize = this.left == null ? 0: left.size;
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
        // TODO
        System.out.println("Not implemented yet");
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
