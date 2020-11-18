/**
 * 
 */
package edu.abhi.tools.activemq.constants;

/**
 * @author abhisheksa
 *
 */
public enum ActionType {
	
	UPLOAD("Uploader"), TRANSFER("Transferrer"), DOWNLOAD("Downloader"), CONSUME("Consumer");
	
	String actor;
	
	ActionType(String actor) {
		this.actor = actor;
	}
	
	public String getActor() {
		return actor;
	}
}
