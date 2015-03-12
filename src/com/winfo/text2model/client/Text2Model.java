package com.winfo.text2model.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/** * Entry point classes define <code>onModuleLoad()</code>. */
public class Text2Model implements EntryPoint {

	/** * Entry point method. */
	public void onModuleLoad() {

		Gui gui = new Gui();

		RootPanel.get().add(gui);

	}

}
