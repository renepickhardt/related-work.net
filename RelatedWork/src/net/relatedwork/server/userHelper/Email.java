package net.relatedwork.server.userHelper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import net.relatedwork.server.utils.IOHelper;


/**
 * Email container class
 * 
 * @author heinrich
 *
 */
public class Email {
	
	private static String MAIL_QUEUE_FILE = "mail_queue.txt";
	
	public String address;
	public String subject;
	public String body;

	public void send() {
		// send out email
		// cf. http://www.tutorialspoint.com/java/java_sending_email.htm

		// Sender's email ID needs to be mentioned
		String from = "related-work@gmx.de";

		// Assuming you are sending email from localhost
		String host = "smtp.gmx.de";

		String username = "related-work@gmx.de";
		String password = "q2349r8jsfd";
		
		// Get system properties
		Properties properties = System.getProperties();

		properties.setProperty("mail.user", username);
		properties.setProperty("mail.password", password);
		
		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.auth", "true");


		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					address));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(body);

			// Send message
			Transport transport = session.getTransport("smtp");
		    transport.connect(host, username, password);
		    
		    transport.sendMessage(message, message.getAllRecipients());
		    transport.close();
		    
			System.out.println("Sent message successfully.");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	public Email(String address, String subject, String body) {
		this.address = address;
		this.subject = subject;
		this.body = body;
	}
	
	public void enqueue() {
		BufferedWriter fh = IOHelper.openAppendFile(MAIL_QUEUE_FILE);
		try {
			fh.write( serialize() + "\nEOF\n" );
			fh.close();
		} catch (IOException e) {
			IOHelper.log("Faild queing email: " + serialize());
		}
	}

	private String serialize() {
		String out = address + "|" + subject + "|" + body;
		return out;
	}	
}
