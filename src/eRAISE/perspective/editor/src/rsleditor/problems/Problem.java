/**
 * 
 */
package rsleditor.problems;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class Problem { 
	
	/**
	 * The level of severity a marker can have
	 * 0-info 1-warning 2-error
	 */
	private int severity;
	
	/**
	 * The message associated to the problem
	 */
	private String message;
	
	/**
	 * The line number where the problem
	 * was encountered
	 */
	private int linenumber;
	
	/**
	 * The offset where the squiggly line
	 * should start 
	 */
	private int offset;
	
	/**
	 * The end position of the squiggly  line
	 */
	private int char_end;
	

	/**
	 * Default constructor
	 */
	public Problem(){
		
	}
	
	/**
	 * Constructor that sets all fields
	 * at once
	 * 
	 * @param severity severity
	 * @param message message
	 * @param linenumber line number
	 * @param offset column
	 * @param char_end where the poblem ends
	 */
	public Problem(int severity, String message, int linenumber, int offset,
			int char_end) {
	
		this.severity = severity;
		this.message = message;
		this.linenumber = linenumber;
		this.offset = offset;
		this.char_end = char_end;
	}

	/**
	 * @return the severity
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(int severity) {
		this.severity = severity;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the linenumber
	 */
	public int getLinenumber() {
		return linenumber;
	}

	/**
	 * @param linenumber the linenumber to set
	 */
	public void setLinenumber(int linenumber) {
		this.linenumber = linenumber;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the char_end
	 */
	public int getChar_end() {
		return char_end;
	}

	/**
	 * @param char_end the char_end to set
	 */
	public void setChar_end(int char_end) {
		this.char_end = char_end;
	}
	
}
