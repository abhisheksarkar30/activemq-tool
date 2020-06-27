/**
 * 
 */
package edu.abhi.tools.activemq;

/**
 * @author abhisheksa
 *
 */
public enum ActionType {
	
	UPLOAD("Uploader"), TRANSFER("Transferrer"), DOWNLOAD("Downloader");
	
	String actor;
	
	ActionType(String actor) {
		this.actor = actor;
	}
	
	public String getActor() {
		return actor;
	}
}
