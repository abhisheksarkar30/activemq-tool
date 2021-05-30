/**
 * 
 */
package edu.abhi.tools.activemq.action.download;

import edu.abhi.tools.activemq.action.GenericMessageAction;
import edu.abhi.tools.activemq.utils.DownloadUtils;

import javax.jms.*;
import java.io.IOException;
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
		
		System.out.println("****** Downloading message(s) from queue *******");
		try {
			connection = cf.createConnection();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue destQueue = session.createQueue(getOptions().getQueue1Name());
			browser = session.createBrowser(destQueue);
			Enumeration<BytesMessage> enumeration = browser.getEnumeration();
			connection.start();

			while(enumeration.hasMoreElements()) {
				BytesMessage msg = enumeration.nextElement();
				DownloadUtils.downloadToFile(msg, getOptions(), ++count);
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

}