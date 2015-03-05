package transform;

import worldModel.Action;
import worldModel.ExtractedObject;
import worldModel.WorldModel;
import Models.ProcessModel;
import etc.TextToProcess;

public class EPCModelBuilder extends ProcessModelBuilder {

	public EPCModelBuilder(TextToProcess parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ProcessModel createProcessModel(WorldModel world) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildDataObjects(WorldModel world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DataObject createDataObject(Action targetAc, String doName,
			boolean arriving) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName(ExtractedObject a, boolean addDet, int level,
			boolean compact) {
		// TODO Auto-generated method stub
		return null;
	}



}
