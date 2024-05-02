package Ao3Analyzer;

import java.io.*;
import java.util.*;

public class BinarySearchTree<Key, Value> implements SymbolTable<Key, Value>{
	
	private Node root;
	private int size;
    private int maxSize;
    private Node scapegoat;

	public BinarySearchTree(){
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

    public static void main(String[] args) {

    }
}