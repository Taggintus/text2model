/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package etc;


import Models.BPMNModel;
import Nodes.FlowObject;
import textModel.TextModel;
import worldModel.SpecifiedElement;
import edu.stanford.nlp.trees.Tree;

/**
 * @author ff
 *
 */
public interface TextToProcessListener {

	public void textModelChanged(TextModel model);

	/**
	 * @param model
	 */
	public void modelGenerated(BPMNModel model);

	/**
	 * @param tree
	 */
	public void displayTree(Tree tree);

	/**
	 * @param string
	 */
	public void displayDependencies(String string);

	/**
	 * @param _element
	 * @param _corr
	 */
	public void textElementClicked(SpecifiedElement _element, FlowObject _corr);
	
}
