/**
 * 
 */
package edu.abhi.tools.activemq.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import edu.abhi.tools.activemq.Constants;
import edu.abhi.tools.activemq.GenericMessageAction;
import edu.abhi.tools.activemq.ResourceLoader;

/**
 * @author abhisheksa
 *
 */
public class BinaryMessageDownloader extends GenericMessageAction {
	
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
			Enumeration<BytesMessage> enumeration = browser.getEnumeration();
			connection.start();
			String fileNameConv = folderLocation + File.separator + "binaryMsg-" + System.currentTimeMillis() + "-";
			
			while(enumeration.hasMoreElements()) {
				BytesMessage msg = enumeration.nextElement();
				downloadToFile(msg, fileNameConv);
			}
			session.commit();
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