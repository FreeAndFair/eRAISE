/**
 * 
 */
package editor.config;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * Defines what characters can be considered white spaces
 *
 */
public class RSLWhitespaceDetector implements IWhitespaceDetector {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWhitespaceDetector#isWhitespace(char)
	 */
	@Override
	public boolean isWhitespace(char c) {
		
			return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
		}
}

