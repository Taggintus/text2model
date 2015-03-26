package etc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import processing.WordNetWrapper;

public class Test {
	
	private TextToProcess t2p = new TextToProcess();
	private File file = new File("data.txt");
	
	public Test () {
		
	}
	
	public void convert (File f, boolean bpmn){
		WordNetWrapper.init();
		t2p.parseText(f, bpmn);
	   }
	
	public TextToProcess getT2P (){
		return t2p;
	}
	
	public void saveInput (String input) throws FileNotFoundException{
		   PrintWriter out = new PrintWriter(file);
		   out.println(input);
		   out.close();
	   }
	   
	public void testing (String input) throws FileNotFoundException{
		   saveInput(input);
		   convert(file, true);	   
	   }	

}
