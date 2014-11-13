package com.winfo.text2model.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Text2Model implements EntryPoint {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextArea newTextArea = new TextArea();
	private Button sendButton = new Button("Send");


/**  * Entry point method.  */  
public void onModuleLoad() {
	
	
    // Assemble send panel.
    addPanel.add(newTextArea);
    addPanel.add(sendButton);
	
    // Assemble Main panel.
    mainPanel.add(addPanel);

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

/**
 * Speichert eingegebenen Text als String
 * 
 */
private void sendtext() {
    final String text = newTextArea.getText();
    newTextArea.setFocus(true);
    
    System.out.print(text);
}


}
