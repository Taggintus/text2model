/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package etc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;




/**
 * @author ff
 *
 */
public class TextToProcessPlugin implements ActionListener {

	private JMenuItem f_mi = new JMenuItem("Generate Model from Text...");
//	private T2Pgui f_gui;
	//private static final String f_testFile = "TestData/Oracle - Expense Report Process - eng.txt";
	private static final String f_testFile = "TestData/Inubit tutorial - eng.txt";
	


	
	/**
	 * @param workbench
	 */
	public TextToProcessPlugin() {
		super();
		f_mi.addActionListener(this);	     
	}




	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	//@Override
//	public void init(SplashScreen splashScreen) {

	//}
	
	
	



	

	}

	

