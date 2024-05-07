package Ao3Analyzer;

import java.io.*;
import java.util.*;

public class FicFunctions {
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
		private Date statusDate;
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
		return null;
	}
	private String getString(Queue<Character> c) {
		return null;
	}
	private String getCSArray(Queue<Character> c) {
		return null;
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
					else if (currentChar == ',') linecharsqueue.remove();
					else temp.add(getNormal(linecharsqueue));
				}
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
	}
}
