package Ao3Analyzer;

import java.io.*;
import java.util.*;

public class ScapegoatTree<Key, Value> implements SymbolTable<Key, Value>{
	
	private Node root;
	private int size;
    private int maxSize;
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

        private Node(Key key, Value val, Node left, Node right, int size, boolean isScapegoat){
            this.key = key;
            this.val = val;
            this.left = left;
            this.right = right;
            this.size = size;
            this.isScapegoat = isScapegoat;
        } 
    }

    public boolean isEmpty(){
        // To do
        return false;
    }

    // Returns the number of keys 
    public int size(){
        // To do
        return 0;
    }

    // Puts the key-value pair into the table 
    // The comparator is used to compare keys
    public void put(Key key, Value val, Comparator<Key> comparator){
        // To do
        System.out.println("Not implemented yet");
    } 

    // Returns the value paired with the key 
    // Returns null if the key is not in the table
    // The comparator is used to compare keys
    // "Search operation" 
    public Value get(Key key, Comparator<Key> comparator){
        // To do
        return null;
    }

    public void delete(Key key, Comparator<Key> comparator){
        // To do
        System.out.println("Not implemented yet");
    }

    public static void main(String[] args) {

    }
}