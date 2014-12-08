package Wordnet;


import edu.smu.tspell.wordnet.*;
import edu.smu.tspell.wordnet.impl.*;
import edu.smu.tspell.wordnet.impl.file.*;
import edu.smu.tspell.wordnet.impl.file.synset.*;


public class Wordnet {

	public static void main(String[] args)
	{
		//Datenbank erstellen
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		
		Synset[] nounSynset;		//Synonyme sammlen
		NounSynset[] hyponyms;		//Ober/Unterbegriffe sammeln
		int i;
		//Synonyme erhalten
		for (i=0; i < args.length; i++) {
		
			nounSynset =  database.getSynsets(args[i]);
		}
	}
}