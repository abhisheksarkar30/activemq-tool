/**
 * 
 */
package edu.abhi.tools.activemq.action;

import edu.abhi.tools.activemq.constants.Constants;
import edu.abhi.tools.activemq.utils.ApplicationOptions;
import edu.abhi.tools.activemq.utils.ResourceLoader;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * @author abhisheksa
 *
 */
public abstract class GenericMessageAction {
	private ApplicationOptions options;

	public ApplicationOptions getOptions() {
		return options;
	}

	public void setOptions(ApplicationOptions options) {
		this.options = options;
	}

	public abstract void process(ConnectionFactory connectionFactory)  throws JMSException;
}
