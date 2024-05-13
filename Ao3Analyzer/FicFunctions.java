package Ao3Analyzer;

import java.io.*;
import java.util.*;

public class FicFunctions {
	public static ScapegoatTree<Integer, Fic> tree = new ScapegoatTree<Integer, Fic>(0.75);
	private Hashtable<String, ScapegoatTree<Integer, Fic>> tags =
			new Hashtable<String, ScapegoatTree<Integer, Fic>>();

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
					//System.out.println("ingesting new line");
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
				// now add id for f to appropriate things
				tagAssign(f);
			}
		} catch(FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
		}
	}

	private void tagAssign(Fic f) {
		List<String> ficTags = Arrays.asList(f.tags.split(","));
		for (String tag : ficTags) {
			tag = tag.trim();
			if (!tags.containsKey(tag)) tags.put(tag, new ScapegoatTree<Integer, Fic>(0.85));
			tags.get(tag).put(f.hits, f, new RevIntComparator());
		}
		//if (!ficTags.contains("Chess")) System.out.println(++wtf);
		//if (!ficTags.contains("Chess") && !ficTags.contains(" Chess")) System.out.println(ficTags + "\n" + f.title);
	}

	public static void main(String[] args) {
		FicFunctions f = new FicFunctions();
		System.out.println("reading from file...");
		f.ingest("fanfics.csv");
		System.out.println("done!");

		Scanner in = new Scanner(System.in);

		boolean running = true;
		System.out.println("-".repeat(60));
		System.out.println("\nWelcome to the AO3 Fanfic Analyzer!\n");
		while (running) {
			System.out.println("-".repeat(60));
			System.out.println("\tPlease enter an option:");
			System.out.println("\t  [1] - Get metadata for a specific fic");
			System.out.println("\t  [2] - Get metadata for a specific tag");
			System.out.println("\t  [3] - Get most popular fics for a specific tag");
			System.out.println("\t  [4] - Get most popular tags for this data");
			System.out.println("\t  [q] - Quit");
			System.out.print("> ");

			switch (in.nextLine()) {
				case "1":
					getFic(in, f);
					break;
				case "2":
					getTag(in, f);
					break;
				case "3":
					getPopFics(in, f);
					break;
				case "4":
					getPopTags(in, f);
					break;
				case "q":
					running = false;
					break;
				default:
					System.out.println("Invalid input!");
					break;
			}
		}
	}

	// some helper functions to improve readability
	private static void getFic(Scanner in, FicFunctions f) {
		System.out.println("Enter a fic to search for:");
		String input = in.nextLine();
		Fic searchFor = null;
		try {
			searchFor = f.tree.get(Integer.parseInt(input), new IDComparator());
		} catch (NumberFormatException e) {
			System.err.println("Not an integer!");
			return;
		}
		if (searchFor != null) {
			System.out.println("title: " + searchFor.title);
			System.out.println("author(s): " + Arrays.toString(searchFor.author));
			System.out.println("views: " + searchFor.hits);
			System.out.println("tags: " + searchFor.tags);
		}
		else System.out.println("Not a valid ID!");
	}
	private static void getTag(Scanner in, FicFunctions f) {
		System.out.println("Enter a tag to search for:");
		String input = in.nextLine();
		ScapegoatTree<Integer, Fic> tagTree = null;
		tagTree = f.tags.get(input);
		if (tagTree != null) {
			System.out.println("number of fics: " + tagTree.size);
			System.out.println("most popular fic: " + 
					f.tree.get(tagTree.inOrderTraversalValues(1).get(0).id, new IntComparator()).title);
		} else System.out.println("Not a valid tag!");
	}
	private static void getPopFics(Scanner in, FicFunctions f) {
		System.out.println("Enter a tag to search for\n(Leave blank for all tags):");
		String input = in.nextLine();
		ScapegoatTree<Integer, Fic> gpfTree = null;
		gpfTree = (input.equals("")) ? f.tree : f.tags.get(input);
		if (gpfTree != null) {
			System.out.println("How many fics? (Max: " + gpfTree.size + ")");
			try {
				int n = Integer.parseInt(in.nextLine());
				ArrayList<Fic> fics = gpfTree.inOrderTraversalValues(n);
				String toprint = "Most popular fics";
				toprint += (input.equals("")) ? ":" : " for tag: " + input + ":";
				System.out.println(toprint);
				for (int i = 0; i < fics.size(); i++) {
					System.out.println(i + 1 + ". - " + fics.get(i).title + " (" + fics.get(i).hits + " hits)");
				}
			}
			catch (NumberFormatException e) {
				System.err.println("Not an integer!");
				return;
			}
		} else System.out.println("Not a valid tag!");
	}
	private static void getPopTags(Scanner in, FicFunctions f) {}
}

class IDComparator implements Comparator<Integer> {
	public int compare(Integer int1, Integer int2) {
		return int1 - int2;
	}
}

class RevIntComparator implements Comparator<Integer> {
	public int compare(Integer int1, Integer int2) {
		return int2 - int1;
	}
}
