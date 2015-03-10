/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package textModel;

import java.util.ArrayList;

import Nodes.Cluster;
import Nodes.ProcessNode;


/**
 * @author ff
 *
 */
public class SentenceNode extends Cluster {
	
	private ArrayList<WordNode> f_wordNodes = new ArrayList<WordNode>();
	private int f_index = 0;

	/**
	 * 
	 */
	public SentenceNode() {

	}
	
	/**
	 * @param index 
	 * 
	 */
	public SentenceNode(int index) {
		f_index = index;		
	}
	
	public void addWord(WordNode word){
		f_wordNodes .add(word);
		super.addProcessNode(word);
	}

	/**
	 * @return
	 */
	public int getIndex() {
		return f_index;
	}
	
	@Override
	public void removeProcessNode(ProcessNode n) {
		//not possible its a build only model
	}

}
