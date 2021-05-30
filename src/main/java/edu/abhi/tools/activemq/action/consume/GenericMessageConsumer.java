/**
 * 
 */
package edu.abhi.tools.activemq.action.consume;

import javax.jms.*;

import edu.abhi.tools.activemq.action.GenericMessageAction;
import edu.abhi.tools.activemq.constants.Constants;
import edu.abhi.tools.activemq.utils.DownloadUtils;
import edu.abhi.tools.activemq.utils.ResourceLoader;

import java.io.IOException;

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
		Message message;
		
		System.out.println("****** Consuming message(s) from queue *******");
		try {
			connection = cf.createConnection();
			connection.start();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue destQueue = session.createQueue(getOptions().getQueue1Name());
			consumer = session.createConsumer(destQueue);

			while(true) {
				message = consumer.receive(1000);

				if(message == null) break;

				count++;

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					if(getOptions().isMessageStore()) {
						DownloadUtils.downloadToFile(textMessage, getOptions(), count);
					} else {
						String text = textMessage.getText();
						System.out.println("Received:- \n" + text);
					}
				} else {
					if(getOptions().isMessageStore()) {
						DownloadUtils.downloadToFile((BytesMessage) message, getOptions(), count);
					} else
						System.out.println("Received:- \n" + message);
				}
			}
			session.commit();
			System.out.println("No. of Message(s) successfully consumed = " + count);
		} catch (JMSException | IOException e) {
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