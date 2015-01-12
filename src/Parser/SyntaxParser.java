package Parser;

import edu.stanford.nlp.trees.Tree;

/*
 * an abstract class providing methods
 * for different parsers
 */

public abstract class SyntaxParser {
	
	/*
	 * parses a sentence
	 * I: sentence to parse as String
	 * O: syntax tree of the parsed sentence as Tree
	 */
	
	abstract Tree parse(String str);
	
	/*
	 * prints a the single words of a sentence
	 * with its POS
	 * I: sentence to parse
	 */
	
	abstract void printWordsAndPOSTags(String str);
}
