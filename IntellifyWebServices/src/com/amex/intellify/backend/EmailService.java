package com.amex.intellify.backend;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
	
	static boolean isSendtingToEmail = false; 
	
	public void sendOffers(String req) {
		
		
		String flag = "false";
		 String emailId = "";
		 String mobile="1"+"4049924507"+"@tmomail.net";
		 if(!mobile.isEmpty() && mobile!=null){
		isSendtingToEmail=false;
		sendEmail(mobile,flag,req);
		 }

	}
	
	public void sendEmail(String reciepient,String flag, String req)
	{
		String[] to= new String[]{reciepient};
		try{
		String host = "smtp.gmail.com";
	    String from = "IntellifyAmEx@gmail.com";
	    String pass = "intellify12345";
	    Properties props = System.getProperties();
	    props.put("mail.smtp.starttls.enable", "true"); // added this line
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.user", from);
	    props.put("mail.smtp.password", pass);
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    Session session = Session.getDefaultInstance(props, null);
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(from));

	    InternetAddress[] toAddress = new InternetAddress[to.length];

	    // To get the array of addresses
	    for( int i=0; i < to.length; i++ ) { // changed from a while loop
	        toAddress[i] = new InternetAddress(to[i]);
	        System.out.println(toAddress[i].toString());
	    }
	    System.out.println(Message.RecipientType.TO);
	    

	    for( int i=0; i < toAddress.length; i++) { // changed from a while loop
	        message.addRecipient(Message.RecipientType.TO, toAddress[i]);
	    }
	   
	    if(isSendtingToEmail)
	    {
	    	if(flag.equalsIgnoreCase("false")){
	    		 message.setSubject("Intellify Promo Offer");
	    		 message.setText(req);
	    	}else{
	    		message.setSubject("Intellify Promo Offer");
	    		message.setText(req);
	    	}
	   }
	    else{
	    	if(flag.equalsIgnoreCase("false")){
	    	 message.setSubject("Intellify Promo Offer");
	    	message.setText(req);	
	    	}else{
	    		message.setSubject("Intellify Promo Offer");
	    		message.setText(req);
	    	}
	    	
	    }
	    
	    System.out.println("host"+host);
	    System.out.println("from"+from);
	    System.out.println("pass"+pass);
	   Transport transport = session.getTransport("smtp");
	    transport.connect(host, from, pass);
	    transport.sendMessage(message, message.getAllRecipients());
	    transport.close();
         System.out.println("Sent message successfully....");
		
		 }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
		 }


}
