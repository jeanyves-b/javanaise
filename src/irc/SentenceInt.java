/***
 * Sentence class : used for representing the text exchanged between users
 * during a chat application
 * Contact: 
 *
 * Authors: 
 */

package irc;

import annotations.*;

public interface SentenceInt {
	//String 		data;
  
	@Ecriture
	public void write(String text);
	
	@Lecture
	public String read();
	
}