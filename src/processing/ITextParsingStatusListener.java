/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package processing;

/**
 * @author ff
 *
 */
public interface ITextParsingStatusListener {
	
	public void setNumberOfSentences(int number);
	public void sentenceParsed(int number);

}
