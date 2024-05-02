package Ao3Analyzer; 

import java.util.Comparator;

public interface SymbolTable<Key, Value>{

	// Returns true if the table is empty  
	public abstract boolean isEmpty();

	// Returns the number of keys 
	public abstract int size();

	// Puts the key-value pair into the table 
	// The comparator is used to compare keys
	public abstract void put(Key key, Value val, Comparator<Key> comparator); 

	// Returns the value paired with the key 
	// Returns null if the key is not in the table
	// The comparator is used to compare keys
	// "Search operation" 
	public abstract Value get(Key key, Comparator<Key> comparator);

	// Returns true or false if the given key is/is not in the table
	public abstract boolean contains(Key key, Comparator<Key> comparator);

	// Given a key, deletes its corresponding node and value from the table
    // Prints an error if the key is not in the table 
    // The comparator is used to compare keys
	public abstract void delete(Key key, Comparator<Key> comparator);
}