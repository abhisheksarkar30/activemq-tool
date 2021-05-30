/**
 * 
 */
package edu.abhi.tools.activemq;

import edu.abhi.tools.activemq.action.GenericMessageAction;
import edu.abhi.tools.activemq.constants.Constants;
import edu.abhi.tools.activemq.utils.ApplicationOptions;
import edu.abhi.tools.activemq.utils.ResourceValidatorResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * @author abhisheksa
 *
 */
public class ApplicationLoader {
	
	/**
	 * @param args
	 * @throws JMSException
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws JMSException {
		final ApplicationOptions options = new ResourceValidatorResolver().resolve(args);

		ApplicationContext context = new ClassPathXmlApplicationContext(args[0]);

		String targetBeanId = options.getMessageType().equalsIgnoreCase(Constants.CONST_BINARY)?
				Constants.CONST_BINARY : Constants.CONST_TEXT;
		targetBeanId += Constants.CONST_MSG + options.getActionTypeEnum().getActor();
		
		GenericMessageAction bean = context.getBean(targetBeanId, GenericMessageAction.class);
		bean.setOptions(options);
		bean.process(context.getBean(Constants.CONNECTION_FACTORY, ConnectionFactory.class));
	}

}
