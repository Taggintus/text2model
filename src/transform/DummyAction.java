/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package transform;

import worldModel.Action;

/**
 * @author ff
 *
 */
public class DummyAction extends Action{

	/**
	 * @param action 
	 * @param origin
	 * @param wordInSentece
	 * @param verb
	 */
	public DummyAction(Action action) {
		super(action.getOrigin(), action.getWordIndex()+1, "Dummy Node");
	}

	public DummyAction() {
		super(null, -1, "Dummy Node");
	}

	
}
