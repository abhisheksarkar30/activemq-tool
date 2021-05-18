/**
 * 
 */
package edu.abhi.tools.activemq.action.download;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	private int count = 0;
	private int exitCode = 0;
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public void process(ConnectionFactory cf) throws JMSException {
		Connection connection = null;
		Session session = null;
		QueueBrowser browser = null;
		
		String queueName = ResourceLoader.getResourceProperty(Constants.QUEUE1_NAME);
		String folderLocation = ResourceLoader.getResourceProperty(Constants.FOLDER_LOCATION);
		boolean multipleMessage = Constants.CONST_YES.equalsIgnoreCase(ResourceLoader.getResourceProperty(Constants.MESSAGE_MULTIPLE));
		String datePattern = ResourceLoader.getResourceProperty(Constants.FILE_DATE_PATTERN);
		String formattedDate = datePattern != null && !datePattern.isEmpty()? new SimpleDateFormat(datePattern)
				.format(new Date()) : "" + System.currentTimeMillis();

		System.out.println("****** Downloading message(s) from queue *******");
		try {
			connection = cf.createConnection();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue destQueue = session.createQueue(queueName);
			browser = session.createBrowser(destQueue);
			Enumeration<TextMessage> enumeration = browser.getEnumeration();
			connection.start();

			String fileName = folderLocation + File.separator + String.format("textMsg-%s-%s-", queueName, formattedDate);
			String currFileName = fileName;
			
			while(enumeration.hasMoreElements()) {
				currFileName = fileName + (multipleMessage? 0 : ++count) + ".txt";
				try (FileWriter fw = new FileWriter(currFileName, true)) {
					TextMessage msg = enumeration.nextElement();
					fw.write(msg.getText() + (multipleMessage? "\n\n" : ""));
					System.out.println("Writing message(s) : " + (multipleMessage? ++count : currFileName));
				}
			}
			session.commit();
			if(count > 0 && multipleMessage)
				System.out.println("Text Message generated in file = " + currFileName);
			System.out.println("No. of Message(s) successfully downloaded = " + count);
		} catch (JMSException | IOException e) {
			System.out.println("Failed to download message. Make sure MQ is connected");
			e.printStackTrace();
			session.rollback();
			exitCode = -1;
		} finally {
			if(browser != null) browser.close();
			if(session != null) session.close();
			if(connection != null) connection.close();
		}

		System.exit(exitCode);
	}

}