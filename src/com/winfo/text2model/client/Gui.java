package com.winfo.text2model.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
//import com.google.gwt.event.dom.client.KeyCodes;
//import com.google.gwt.event.dom.client.KeyDownEvent;
//import com.google.gwt.event.dom.client.KeyDownHandler;

public class Gui extends Composite {

	// Initiate Panels & Areas
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private VerticalPanel buttonPanel = new VerticalPanel();
	private TextArea textInput = new TextArea();
	private TextArea textOutput = new TextArea();

	// Initiate Buttons
	private Button sendButton = new Button("Send");
	private RadioButton bpmnb = new RadioButton("modeltype", "BPMN");
	private RadioButton epcb = new RadioButton("modeltype", "EPC");
	// private RadioButton engb = new RadioButton("language", "English");
	// private RadioButton gerb = new RadioButton("language", "Deutsch");
  
	// Initiate variables for type and language
	int t = 1;
	// int l = 1;

	// Async part of the rpc
	private MessageServiceAsync messageService = GWT
			.create(MessageService.class);

	private class MessageCallBack implements AsyncCallback<Message> {
		@Override
		public void onFailure(Throwable caught) {
			/* server side error occured */
			Window.alert("Unable to obtain server response: "
					+ caught.getMessage());
		}

		@Override
		public void onSuccess(Message result) {
			/* server returned result, show user the message */

			textOutput.setText(result.getMessage());
			// Window.alert(result.getMessage());
		}
	}

	// Initiate Gui
	@SuppressWarnings("deprecation")
	public Gui() {

		// Build main panel
		initWidget(this.mainPanel);
		mainPanel.setWidth("100%");
		mainPanel.setBorderWidth(3);

		// Build send area
		mainPanel.add(textInput);
		textInput.setWidth("98%");
		textInput.setVisibleLines(25);

		// Build button area.
		mainPanel.add(buttonPanel);

		// Build output panel.
		mainPanel.add(textOutput);
		textOutput.setWidth("98%");
		textOutput.setVisibleLines(25);
		textOutput.setReadOnly(true);

		// Assemble Buttons
		buttonPanel.add(bpmnb);
		buttonPanel.add(epcb);
		// buttonPanel.add(engb);
		// buttonPanel.add(gerb);
		buttonPanel.add(sendButton);

		bpmnb.setChecked(true);
		// engb.setChecked(true);

		// Move cursor focus to the input box.
		textInput.setFocus(true);

		// Listen for mouse events on the Add button.
		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sendtext();
			}
		});

		// Listen for keyboard events in the input box.
		/*
		 * textInput.addKeyDownHandler(new KeyDownHandler() { public void
		 * onKeyDown(KeyDownEvent event) { if (event.getNativeKeyCode() ==
		 * KeyCodes.KEY_ENTER) { sendtext(); } } });
		 */

	}

	// Save input text as string
	@SuppressWarnings("deprecation")
	private void sendtext() {
		textInput.setFocus(true);

		// Save the modeltype in t // 1 = BPMN; 0 = EPK
		if (bpmnb.isChecked()) {
			t = 1;
		} else {
			t = 0;
		}

		/*
		 * // Save the language in l // 1 = english; 0 = german if
		 * (engb.isChecked()) { l = 1; } else { l = 0; }
		 */

		// do the call
		messageService.getMessage(textInput.getValue(), new MessageCallBack());

	}

}
