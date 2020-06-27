/**
 * 
 */
package edu.abhi.tools.activemq;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * @author abhisheksa
 *
 */
public abstract class GenericMessageAction {
	
	public abstract void process(ConnectionFactory connectionFactory)  throws JMSException;

}
