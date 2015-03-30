/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package transform;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import Nodes.ProcessNode;

import Models.ProcessModel;
import BPMN.DataObject;
import BPMN.SequenceFlow;



//import com.inubit.research.layouter.gridLayouter.GridLayouter;

import worldModel.Action;
import worldModel.ExtractedObject;

import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.WorldModel;

/**
 * @author ff
 *
 */
public abstract class ProcessModelBuilder {
	
/**
 * @param os
 * @param _a
 * @param _b
 * @param s
 */

protected abstract void put(HashMap<Action, List<String>> os, Action a, String dataObj);

/**
 * @param _a
 * @return
 */
protected abstract Set<String> getDataObjectCandidates(SpecifiedElement ob);


public abstract Map<ProcessNode, String> getCommLinks();


/**
 * @param specifiers
 * @return
 */
protected abstract Specifier containedReceiver(List<Specifier> specifiers);

/**
 * @param specifiers
 * @return
 */
protected abstract Specifier containedSender(List<Specifier> specifiers);

/**
 * @param world 
 * 
 */
protected abstract void processMetaActivities(WorldModel world);

/**
 * @param world 
 * 
 */
protected abstract void removeDummies(WorldModel world);

/**
 * @param a
 * @return
 */
protected abstract String getEventText(Action a);

/**
 * 
 */
protected abstract void eventsToLabels();

protected abstract SequenceFlow removeNode(Action a);

/**
 * removes a node from the model but keeps the predecessor and successor connected
 * @param a
 * 
 */
public abstract SequenceFlow removeNode(ProcessNode node);


/**
 * @param actorFrom
 * @return
 */
protected abstract boolean hasHiddenAction(ExtractedObject obj);



/**
 * @return
 */
protected abstract String createTaskText(Action a);




/**
 * @param spec
 * @return
 */
protected abstract boolean considerPhrase(Specifier spec);



protected abstract String getName(ExtractedObject a,boolean addDet);


protected abstract void addSpecifier(int level, StringBuilder _b, Specifier s,boolean compact);


public abstract void layoutModel(ProcessModel _result);


public abstract ProcessModel createProcessModel(WorldModel world);

public abstract void buildDataObjects(WorldModel world);

public abstract DataObject createDataObject(Action targetAc,String doName,boolean arriving);

public abstract String getName(ExtractedObject a,boolean addDet,int level,boolean compact);

}
