# csci-136-final-project-jacob-alex
A program to analyze tag density and fanfiction metadata from Archive of Our Own.

## Overview
This package is a command-line tool that can perform analysis on CSV files of Archive of Our Own (Ao3) metadata. The package comes bundled with a starter dataset (every General Audiences-rated fanfiction under the 'Chess' tag), but if you want to create your own dataset check out this fantastic project by radiolarian: https://github.com/radiolarian/AO3Scraper.

## Requirements
- OpenJDK Runtime Environment Corretto-11.0.23 or later

## Installation
1. `git clone` the repository to your machine.
2. Open a terminal and `cd` to the repository.
3. `mkdir bin`
4. `javac -d bin Ao3Analyzer/*.java`
5. `java -cp bin Ao3Analyzer.FicFunctions`
6. (Optional) To use a custom dataset, rename it to 'fanfics.csv' and put it in the root folder of the repository (erasing the starter dataset).

## Commands
The following commands can be run from within the program:

[1] - Get metadata for a specific fic - given a fanfiction ID number, this returns the work's name, author(s), views, and tags.

[2] - Get metadata for a specific tag - given a tag, this returns the number of fics with that tag and the most popular fic from that tag in the dataset.

[3] - Get most popular fics for a specific tag - given a tag and a number `n`, this returns the n most popular works for that tag in the dataset.

[4] - Get most popular tags for this data - given a number `n`, this returns the n most popular fics in the dataset.

[5] - Delete all fics with a specific tag - given a tag, this deletes all fics containing that tag from the **active** dataset (it will not delete them from the CSV file - this is useful if you want to see the effects of large-scale changes to the dataset without permanently altering your data).