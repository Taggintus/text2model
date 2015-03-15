package com.winfo.text2model.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.winfo.text2model.client.MessageService;
import com.winfo.text2model.client.Message;

import etc.Test;

public class MessageServiceImpl extends RemoteServiceServlet 
   implements MessageService{

   private static final long serialVersionUID = 1L;
   private Test tt = new Test();
   File file = new File("data.txt");

   public Message getMessage(String input) {
      String messageString = "Ihre reversierte Eingabe: " + reverse(input) + "!";
      try {
		conversion(input);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
   
   public void saveInput (String input) throws FileNotFoundException{
	   PrintWriter out = new PrintWriter(file);
	   out.println(input);
	   out.close();
   }
   
   public void conversion (String input) throws FileNotFoundException{
	   saveInput(input);
	   tt.convert(file, true);	   
   }
   
   
   
}