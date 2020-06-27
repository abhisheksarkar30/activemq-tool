/**
 * 
 */
package edu.abhi.tools.activemq;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author abhisheksa
 *
 */
public class ApplicationLoader {
	
	static String messageType;
	static ActionType actionTypeEnum;
	
	/**
	 * @param args
	 * @throws JMSException 
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws JMSException {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		
		validateProperties(ResourceLoader.getResource());
		
		String targetBeanId = messageType.equalsIgnoreCase(Constants.CONST_BINARY)? Constants.CONST_BINARY : Constants.CONST_TEXT;
		targetBeanId += Constants.CONST_MSG + actionTypeEnum.getActor();
		
		GenericMessageAction bean = context.getBean(targetBeanId, GenericMessageAction.class);
		bean.process(context.getBean(Constants.CONNECTION_FACTORY, ConnectionFactory.class));
	}
	
	private static void validateProperties(Properties properties) {
		String queue1Name = ResourceLoader.getResourceProperty(Constants.QUEUE1_NAME);
		String participantId = ResourceLoader.getResourceProperty(Constants.PARTICIPANT_ID);
		String actionType = ResourceLoader.getResourceProperty(Constants.ACTION_TYPE);
		messageType = ResourceLoader.getResourceProperty(Constants.MESSAGE_TYPE);

		if (queue1Name == null || queue1Name.isEmpty()) {
			System.out.println("Please provide Queue1 name");
			System.exit(-1);
		}
		
		if (participantId == null || participantId.isEmpty()) {
			System.out.println("Please provide participant id");
			System.exit(-1);
		}
		
		if (actionType == null || actionType.isEmpty()) {
			System.out.println("Please provide action type");
			System.exit(-1);
		} else {
			actionTypeEnum = ActionType.valueOf(actionType.toUpperCase());
			if(actionTypeEnum == null) {
				System.out.println("Please provide valid action type");
				System.exit(-1);
			}
		}
		
		if (!Constants.CONST_BINARY.equalsIgnoreCase(messageType) && !Constants.CONST_TEXT.equalsIgnoreCase(messageType)) {
			System.out.println("Please provide binary/text message type");
			System.exit(-1);
		}
		
		if(actionTypeEnum != ActionType.TRANSFER) {
			String folderLocation = ResourceLoader.getResourceProperty(Constants.FOLDER_LOCATION);
			if (folderLocation == null || folderLocation.isEmpty()) {
				System.out.println("Please provide folder location from where message will be loaded");
				System.exit(-1);
			}
		
			if(actionTypeEnum == ActionType.UPLOAD) {
				String messageFormat = ResourceLoader.getResourceProperty(Constants.MESSAGE_FORMAT);
				if (messageFormat == null || messageFormat.isEmpty()) {
					System.out.println("Please provide message type : xml/swift ");
					System.exit(-1);
				}
				
				String firstLineStarts = ResourceLoader.getResourceProperty(Constants.MESSAGE_STARTS + messageFormat);
				String lastLineStarts = ResourceLoader.getResourceProperty(Constants.MESSAGE_ENDS + messageFormat);
				if (firstLineStarts == null || firstLineStarts.isEmpty() || lastLineStarts == null || lastLineStarts.isEmpty()) {
					System.out.println("Please provide message starts/ends with properties for message type : " + messageFormat);
					System.exit(-1);
				}
			}
		} else {
			String queue2Name = ResourceLoader.getResourceProperty(Constants.QUEUE2_NAME);
			
			if (queue2Name == null || queue2Name.isEmpty()) {
				System.out.println("Please provide Queue2 name");
				System.exit(-1);
			}
		}
	}

}
