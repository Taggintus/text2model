package com.winfo.text2model.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.winfo.text2model.client.MessageService;
import com.winfo.text2model.client.Message;

public class MessageServiceImpl extends RemoteServiceServlet 
   implements MessageService{

   private static final long serialVersionUID = 1L;

   public Message getMessage(String input) {
      String messageString = "Ihre Eingabe " + reverse(input) + "!";
      Message message = new Message();
      message.setMessage(messageString);
      return message;
   }   
   
   public static String reverse(String input){
	    char[] in = input.toCharArray();
	    int begin=0;
	    int end=in.length-1;
	    char temp;
	    while(end>begin){
	        temp = in[begin];
	        in[begin]=in[end];
	        in[end] = temp;
	        end--;
	        begin++;
	    }
	    return new String(in);
	}
}