package com.winfo.text2model.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/** * Entry point classes define <code>onModuleLoad()</code>. */
public class Text2Model implements EntryPoint {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextArea newTextArea = new TextArea();
	private Button sendButton = new Button("Send");
	private RadioButton bpmnb = new RadioButton("modeltype", "BPMN");
    private RadioButton epcb = new RadioButton("modeltype", "EPC");
	private RadioButton engb = new RadioButton("language", "English");
    private RadioButton gerb = new RadioButton("language", "Deutsch");
    int t = 1;
    int l = 1;


/**  * Entry point method.  */  
public void onModuleLoad() {
	
	
    // Assemble send panel.
    addPanel.add(newTextArea);
    newTextArea.setCharacterWidth(100);
    newTextArea.setVisibleLines(25);
    
    addPanel.add(sendButton);
	
    // Assemble Main panel.
    mainPanel.add(addPanel);
    
    // Assemble Radio Buttons
    mainPanel.add(bpmnb);
    mainPanel.add(epcb);
    mainPanel.add(engb);
    mainPanel.add(gerb);
    
    // Set standard button
    bpmnb.setChecked(true);
    engb.setChecked(true);
    
    // Associate the Main panel with the HTML host page.
    RootPanel.get("text2model").add(mainPanel);
    
    // Move cursor focus to the input box.
    newTextArea.setFocus(true);

    
    // Listen for mouse events on the Add button.
    sendButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        sendtext();
      }
    });
    
    // Listen for keyboard events in the input box.
    newTextArea.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          sendtext();
        }
      }
    });


}

/**  * Save input text as string */ 
private void sendtext() {
    final String text = newTextArea.getText();
    newTextArea.setFocus(true);
    
    // Save the modeltype in t // 1 = BPMN; 0 = EPK
    if (bpmnb.isChecked()) {
    	t = 1;
    } else {
    	t = 0 ;
    }
    
    // Save the language in l // 1 = english; 0 = german
    if (engb.isChecked()) {
    	l = 1;
    } else {
    	l = 0 ;
    }
 }
}
