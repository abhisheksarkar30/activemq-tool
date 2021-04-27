/**
 * 
 */
package edu.abhi.tools.activemq.action.upload;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import edu.abhi.tools.activemq.action.GenericMessageAction;
import edu.abhi.tools.activemq.constants.Constants;
import edu.abhi.tools.activemq.utils.ResourceLoader;

/**
 * @author abhisheksa
 *
 */
public class TextMessageUploader extends GenericMessageAction {
	
	int count = 0;
	String msgStartsWith, msgEndsWith;
	
	@Override
	public void process(ConnectionFactory cf) throws JMSException {
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		
		String uploadQueueName = ResourceLoader.getResourceProperty(Constants.QUEUE1_NAME);
		String folderLocation = ResourceLoader.getResourceProperty(Constants.FOLDER_LOCATION);
		String participantId = ResourceLoader.getResourceProperty(Constants.PARTICIPANT_ID);
		String messsageFormat = ResourceLoader.getResourceProperty(Constants.MESSAGE_FORMAT);
		msgStartsWith = ResourceLoader.getResourceProperty(Constants.MESSAGE_STARTS + messsageFormat);
		msgEndsWith = ResourceLoader.getResourceProperty(Constants.MESSAGE_ENDS + messsageFormat);

		System.out.println("****** Uploading message(s) to queue *******");
		try {
			connection = cf.createConnection();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue destQueue = session.createQueue(uploadQueueName);
			producer = session.createProducer(destQueue);
			
			File folder = new File(folderLocation);
			for (File fileEntry : folder.listFiles())
				if (fileEntry.isFile()) {
					uploadFromFile(fileEntry, session, producer, participantId);
					System.out.println("Message successfully uploaded to [" + uploadQueueName + "] Queue.");
				}
			session.commit();
		} catch (JMSException | IOException e) {
			System.out.println("Failed to upload message. Make sure MQ is connected");
			e.printStackTrace();
			session.rollback();
		} finally {
			if(producer != null) producer.close();
			if(session != null) session.close();
			if(connection != null) connection.close();
		}
		
		System.out.println("No. of Message(s) successfully uploaded = " + count);
	}

	private void uploadFromFile(File fileEntry, Session session, MessageProducer producer, String participantId)
			throws IOException, JMSException {
		System.out.println("Uploading message from file : " + fileEntry.getName());
		
		Scanner sc = new Scanner(fileEntry);
		
		String currMsg = "";
		
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if(line.startsWith(msgStartsWith)) {
				currMsg += line + "\n";
			} else if(!currMsg.isEmpty()) {
				currMsg += line + "\n";
				
				if(line.startsWith(msgEndsWith)) {
					TextMessage txtMessage = session.createTextMessage(currMsg);
					for (int i = 0; i < Integer.parseInt(participantId); i++) {
						producer.send(txtMessage);
					}
					System.out.println(++count + " message(s) uploaded till now from file " + fileEntry.getName());
					
					currMsg = "";
				}
			}
		}
		
		sc.close();
	}

}