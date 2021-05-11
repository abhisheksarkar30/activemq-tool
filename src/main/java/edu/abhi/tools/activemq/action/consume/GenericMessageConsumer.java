/**
 * 
 */
package edu.abhi.tools.activemq.action.consume;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
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
public class GenericMessageConsumer extends GenericMessageAction {

	private int count = 0;
	private int exitCode = 0;
	
	@Override
	public void process(ConnectionFactory cf) throws JMSException {
		Connection connection = null;
		Session session = null;
		MessageConsumer consumer = null;
		Message message = null;
		
		String uploadQueueName = ResourceLoader.getResourceProperty(Constants.QUEUE1_NAME);

		System.out.println("****** Consuming message(s) from queue *******");
		try {
			connection = cf.createConnection();
			connection.start();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue destQueue = session.createQueue(uploadQueueName);
			consumer = session.createConsumer(destQueue);
			
			while(true) {
				message = consumer.receive(1000);
				
				if(message == null) break;
				
				count++;

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println("Received: " + text);
				} else {
					System.out.println("Received: " + message);
				}
			}
			session.commit();
			System.out.println("No. of Message(s) successfully consumed = " + count);
		} catch (JMSException e) {
			System.out.println("Failed to consume message. Make sure MQ is connected");
			e.printStackTrace();
			session.rollback();
			exitCode = -1;
		} finally {
			if(consumer != null) consumer.close();
			if(session != null) session.close();
			if(connection != null) {
				connection.stop();
				connection.close();
			}
		}

		System.exit(exitCode);
	}

}