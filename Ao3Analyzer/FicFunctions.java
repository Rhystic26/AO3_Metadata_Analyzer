package Ao3Analyzer;

import java.io.*;
import java.util.*;

public class FicFunctions {
	public static ScapegoatTree<Integer, Fic> tree = new ScapegoatTree<Integer, Fic>(0.75);

	private class Date {
		public int year;
		public int month;
		public int day;

		public Date(int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;
		}
	}
	
	private class Fic{
		private Integer id;
		private String title;
		private String[] author;
		private String rating;
		private String[] category;
		private String fandom;
		private String relationship;
		private String[] characters;
		private String tags;
		private String language;
		private String published;
		private String status;
		private String statusDate;
		private Integer words;
		private String chapters;
		private Integer comments;
		private Integer kudos;
		private Integer bookmarks;
		private Integer hits;
		private String[] allKudos;
		private String[] allBookmarks;

		public Fic() {
			this.id = null;
			this.title = null;
			this.author = null;
			this.rating = null;
			this.category = null;
			this.fandom = null;
			this.relationship = null;
			this.characters = null;
			this.tags = null;
			this.language = null;
			this.published = null;
			this.status = null;
			this.statusDate = null;
			this.words = null;
			this.chapters = null;
			this.comments = null;
			this.kudos = null;
			this.bookmarks = null;
			this.hits = null;
			this.allKudos = null;
			this.allBookmarks = null;
		}
	}

	// helper functions
	private String getNormal(Queue<Character> c) {
		String cat = "";
		while (c.peek() != ',') cat += c.remove();
		c.remove();
		//System.out.println("normal adding " + cat);
		return cat;
	}
	private String getString(Queue<Character> c) {
		String cat = "";
		c.remove();
		while (true) {
			// need to handle quotes in string literals, which are represented as ""
			if (c.peek() == '\"') {
				c.remove();
				// after checking first ", check to see if there is another
				if (c.peek() == '\"') cat += c.remove();
				// otherwise end of string
				else break;
			}
			else cat += c.remove();
		}
		c.remove();
		//System.out.println("string adding " + cat);
		return cat;
	}
	private String getCSArray(Queue<Character> c) {
		String cat = "";
		c.remove();
		while (c.peek() != ']') cat += c.remove();
		c.remove();
		if (c.peek() != ',') cat += getNormal(c);
		else c.remove();
		//System.out.println("array adding " + cat);
		return cat;
	}
	private String makeNumberGreatAgain(String s) {
		if (s.equals("null")) return "0";
		String cat = "";
		char[] c = s.toCharArray();
		for (int i = 0; i < c.length; i++) { if (c[i] != ',') cat += c[i]; }
		return cat;
	}

	private void ingest(String filename) {
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			// disregard header line
			scanner.nextLine();
			// now ingest
			while (scanner.hasNextLine()) {
				// System.out.println("ingesting new line");
				// assign a new variable for accumulation purposes
				Fic f = new Fic();
				String currentline = scanner.nextLine();
				// tbh I don't entirely understand what's happening here
				// if it doesn't work then a for loop would suffice
				Character[] charray =
						currentline.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
				// we'll make the line a chararray
				Queue<Character> linecharsqueue = new LinkedList<Character>(Arrays.asList(charray));
				Queue<String> temp = new LinkedList<String>();
				String cat = "";
				while (!linecharsqueue.isEmpty()) {
					Character currentChar = linecharsqueue.peek();
					//System.out.println(currentChar);
					if (currentChar == '\"') temp.add(getString(linecharsqueue));
					else if (currentChar == '[') temp.add(getCSArray(linecharsqueue));
					else temp.add(getNormal(linecharsqueue));
				}
				// now depopulate queue into f
				f.id = Integer.parseInt(temp.remove());
				//System.out.println("id: " + f.id);
				f.title = temp.remove();
				//System.out.println("title: " + f.title);
				f.author = temp.remove().split(",");
				//System.out.println("author: " + Arrays.toString(f.author));
				f.rating = temp.remove();
				//System.out.println("rating: " + f.rating);
				f.category = temp.remove().split(",");
				//System.out.println("category: " + Arrays.toString(f.category));
				f.fandom = temp.remove();
				//System.out.println("fandom: " + f.fandom);
				f.relationship = temp.remove();
				//System.out.println("relationship: " + f.relationship);
				f.characters = temp.remove().split(",");
				//System.out.println("characters: " + f.characters);
				f.tags = temp.remove();
				//System.out.println("tags: " + f.tags);
				f.language = temp.remove();
				//System.out.println("language: " + f.language);
				f.published = temp.remove();
				//System.out.println("published: " + f.published);
				f.status = temp.remove();
				//System.out.println("status: " + f.status);
				f.statusDate = temp.remove();
				//System.out.println("statusDate: " + f.statusDate);
				f.words = Integer.parseInt(makeNumberGreatAgain(temp.remove()));
				//System.out.println("words: " + f.words);
				f.chapters = temp.remove();
				//System.out.println("chapters: " + f.chapters);
				f.comments = Integer.parseInt(makeNumberGreatAgain(temp.remove()));
				//System.out.println("comments: " + f.comments);
				f.kudos = Integer.parseInt(makeNumberGreatAgain(temp.remove()));
				//System.out.println("kudos: " + f.kudos);
				f.bookmarks = Integer.parseInt(makeNumberGreatAgain(temp.remove()));
				//System.out.println("bookmarks: " + f.bookmarks);
				f.hits = Integer.parseInt(makeNumberGreatAgain(temp.remove()));
				//System.out.println("hits: " + f.hits);
				f.allKudos = temp.remove().split(",");
				//System.out.println("allKudos: " + f.allKudos);
				f.allBookmarks = temp.remove().split(",");
				//System.out.println("allBookmarks: " + f.allBookmarks);
				// now add f to the tree
				tree.put(f.id, f, new IDComparator());
			}
		} catch(FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		FicFunctions f = new FicFunctions();
		System.out.println("reading from file...");
		f.ingest("fanfics.csv");
		System.out.println("done!");

		Scanner in = new Scanner(System.in);
		IDComparator comp = new IDComparator();

		while (true) {
		System.out.println("Enter a fic to search for: ");
		String input = in.nextLine();
		Fic searchFor = f.tree.get(new Integer(Integer.parseInt(input)), comp);
		if (searchFor != null) {
			System.out.println("title: " + searchFor.title);
			System.out.println("author(s): " + Arrays.toString(searchFor.author));
			System.out.println("views: " + searchFor.hits);
		}
		else System.out.println("not a valid id >:/");
		}
	}
}

class IDComparator implements Comparator<Integer> {
	public int compare(Integer int1, Integer int2) {
		return int1 - int2;
	}
}
