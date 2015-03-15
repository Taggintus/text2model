package com.winfo.text2model.client;

import java.io.File;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MessageServiceAsync {
   void getMessage(String input, AsyncCallback<Message> callback);
}