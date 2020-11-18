/**
 * 
 */
package edu.abhi.tools.activemq.action.download;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import edu.abhi.tools.activemq.action.GenericMessageAction;
import edu.abhi.tools.activemq.constants.Constants;
import edu.abhi.tools.activemq.utils.ResourceLoader;

/**
 * @author abhisheksa
 *
 */
public class TextMessageDownloader extends GenericMessageAction {
	
	int count = 0;
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public void process(ConnectionFactory cf) throws JMSException {
		Connection connection = null;
		Session session = null;
		QueueBrowser browser = null;
		
		String queueName = ResourceLoader.getResourceProperty(Constants.QUEUE1_NAME);
		String folderLocation = ResourceLoader.getResourceProperty(Constants.FOLDER_LOCATION);

		System.out.println("****** Downloading message(s) from queue *******");
		try {
			connection = cf.createConnection();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue destQueue = session.createQueue(queueName);
			browser = session.createBrowser(destQueue);
			Enumeration<TextMessage> enumeration = browser.getEnumeration();
			connection.start();
			String fileName = folderLocation + File.separator + "textMsg-" + System.currentTimeMillis() + ".txt";
			
			while(enumeration.hasMoreElements()) {
				try (FileWriter fw = new FileWriter(fileName, true)) {
					TextMessage msg = enumeration.nextElement();
					System.out.println("Writing message(s) : " + ++count);				
					fw.write(msg.getText() + "\n\n");
				}
			}
			session.commit();
			System.out.println("Text Message generated in file = " + fileName);
		} catch (JMSException | IOException e) {
			System.out.println("Failed to download message. Make sure MQ is connected");
			e.printStackTrace();
			session.rollback();
		} finally {
			if(browser != null) browser.close();
			if(session != null) session.close();
			if(connection != null) connection.close();
		}
		
		System.out.println("No. of Message(s) successfully downloaded = " + count);
		System.exit(0);
	}

}