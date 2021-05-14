/**
 * 
 */
package edu.abhi.tools.activemq.action.download;

import edu.abhi.tools.activemq.action.GenericMessageAction;
import edu.abhi.tools.activemq.constants.Constants;
import edu.abhi.tools.activemq.utils.ResourceLoader;

import javax.jms.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author abhisheksa
 *
 */
public class BinaryMessageDownloader extends GenericMessageAction {

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
		String datePattern = ResourceLoader.getResourceProperty(Constants.FILE_DATE_PATTERN);
		String formattedDate = datePattern != null && !datePattern.isEmpty()? new SimpleDateFormat(datePattern)
				.format(new Date()) : "" + System.currentTimeMillis();


		System.out.println("****** Downloading message(s) from queue *******");
		try {
			connection = cf.createConnection();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue destQueue = session.createQueue(queueName);
			browser = session.createBrowser(destQueue);
			Enumeration<BytesMessage> enumeration = browser.getEnumeration();
			connection.start();

			String fileNameConv = folderLocation + File.separator + String.format("binaryMsg-%s-%s-", queueName, formattedDate);
			
			while(enumeration.hasMoreElements()) {
				BytesMessage msg = enumeration.nextElement();
				downloadToFile(msg, fileNameConv);
			}
			session.commit();
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

	private void downloadToFile(BytesMessage msg, String fileNameConv)
			throws IOException, JMSException {
		long length = msg.getBodyLength();
		
		String currFileName = fileNameConv + ++count;
		try (OutputStream fout = new FileOutputStream(currFileName)) {
			for(long i=1;i<=length;i++){
				fout.write(msg.readByte());
			}
		}
		System.out.println("Binary Message generated in file = " + currFileName);
	}

}