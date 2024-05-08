package Ao3Analyzer;

import java.io.*;
import java.util.*;

public class FicFunctions {
	public static ScapegoatTree tree;

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
		private Integer chapters;
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

	private String getNormal(Queue<Character> c) {
		String cat = "";
		while (c.peek() != ',') cat += c.remove();
		c.remove();
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
		return cat;
	}
	private String getCSArray(Queue<Character> c) {
		String cat = "";
		c.remove();
		while (c.peek() != ']') cat += c.remove();
		c.remove();
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
					if (currentChar == '\"') temp.add(getString(linecharsqueue));
					else if (currentChar == '[') temp.add(getCSArray(linecharsqueue));
					else temp.add(getNormal(linecharsqueue));
				}
				// now depopulate queue into f
				f.id = Integer.parseInt(temp.remove());
				System.out.println("id: " + f.id);
				f.title = temp.remove();
				System.out.println("title: " + f.title);
				f.author = temp.remove().split(",");
				System.out.println("author: " + f.author);
				f.rating = temp.remove();
				System.out.println("rating: " + f.rating);
				f.category = temp.remove().split(",");
				System.out.println("category: " + f.category);
				f.fandom = temp.remove();
				System.out.println("fandom: " + f.fandom);
				f.relationship = temp.remove();
				System.out.println("relationship: " + f.relationship);
				f.characters = temp.remove().split(",");
				System.out.println("characters: " + f.characters);
				f.tags = temp.remove();
				System.out.println("tags: " + f.tags);
				f.language = temp.remove();
				System.out.println("language: " + f.language);
				f.published = temp.remove();
				System.out.println("published: " + f.published);
				f.status = temp.remove();
				System.out.println("status: " + f.status);
				f.statusDate = temp.remove();
				System.out.println("statusDate: " + f.statusDate);
				f.words = Integer.parseInt(temp.remove());
				System.out.println("words: " + f.words);
				f.chapters = Integer.parseInt(temp.remove());
				System.out.println("chapters: " + f.chapters);
				f.comments = Integer.parseInt(temp.remove());
				System.out.println("comments: " + f.comments);
				f.kudos = Integer.parseInt(temp.remove());
				System.out.println("kudos: " + f.kudos);
				f.bookmarks = Integer.parseInt(temp.remove());
				System.out.println("bookmarks: " + f.bookmarks);
				f.hits = Integer.parseInt(temp.remove());
				System.out.println("hits: " + f.hits);
				f.allKudos = temp.remove().split(",");
				System.out.println("allKudos: " + f.allKudos);
				f.allBookmarks = temp.remove().split(",");
				System.out.println("allBookmarks: " + f.allBookmarks);
				// now add f to the tree
				/*
				Queue<String> lineQueue = new LinkedList<String>(Arrays.asList(currentline.split(",")));

				// the easiest one is the first one
				f.id = Integer.parseInt(lineQueue.remove());
				// it gets weird now
				// if a field has multiple entires, it's wrapped in ""
				// therefore, we make a new array broken at " and not commas
				String[] lineQuotes = currentline.split("\"");
				Queue<String> lineQuotesQueue = new LinkedList<String>(Arrays.asList(currentLine.split("\"")));
				// now check if the expected title entry begins with a "
				// assign based on that or something
				f.title = (lineQueue.peek().charAt(0) == "\"") ?
						lineQuotesQueue.remove():
						lineQueue.remove();

				// maybe characterwise scanning is better
				*/
				// and now the fun logic begins
				/*
				while (!strq.isEmpty()) {
					String currItem = strq.remove();
					// if not in an array, quite simple
					if ()
				}
				*/
			}
		} catch(FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		FicFunctions f = new FicFunctions();
		System.out.println("Enter a fic to search for: ");
		f.ingest("fanfics.csv");
	}
}
