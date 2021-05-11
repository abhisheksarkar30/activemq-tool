/**
 * 
 */
package edu.abhi.tools.activemq.action.transfer;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import edu.abhi.tools.activemq.action.GenericMessageAction;
import edu.abhi.tools.activemq.constants.Constants;
import edu.abhi.tools.activemq.utils.ResourceLoader;

/**
 * @author abhisheksa
 *
 */
public class GenericMessageTransferrer extends GenericMessageAction {
	
	private int count = 0;
	private int exitCode = 0;
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public void process(ConnectionFactory cf) throws JMSException {
		Connection connection = null;
		Session session = null;
		QueueBrowser browser = null;
		MessageProducer producer = null;
		
		String queue1Name = ResourceLoader.getResourceProperty(Constants.QUEUE1_NAME);
		String queue2Name = ResourceLoader.getResourceProperty(Constants.QUEUE2_NAME);
		String participantId = ResourceLoader.getResourceProperty(Constants.PARTICIPANT_ID);

		System.out.println("****** Transferring/copying message(s) from queue *******");
		try {
			connection = cf.createConnection();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue srcQueue = session.createQueue(queue1Name);
			Queue destQueue = session.createQueue(queue2Name);
			browser = session.createBrowser(srcQueue);
			producer = session.createProducer(destQueue);
			Enumeration<Message> enumeration = browser.getEnumeration();
			connection.start();
			
			while(enumeration.hasMoreElements()) {
				Message msg = enumeration.nextElement();
				for (int i = 0; i < Integer.parseInt(participantId); i++) {
					producer.send(msg);
					System.out.println("No.of message(s) transferred/copied till now = " + ++count);
				}
			}
			session.commit();
			System.out.println("Total No. of Message(s) successfully transferred/copied = " + count);
		} catch (JMSException e) {
			System.out.println("Failed to transfer/copy message. Make sure MQ is connected");
			e.printStackTrace();
			session.rollback();
			exitCode = -1;
		} finally {
			if(browser != null) browser.close();
			if(producer != null) producer.close();
			if(session != null) session.close();
			if(connection != null) connection.close();
		}
		
		System.exit(exitCode);
	}

}