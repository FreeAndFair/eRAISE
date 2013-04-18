/**
 * 
 */
package rsleditor.editor;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Rule for accepting a character as 
 * being part of a keyword
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RSLWordDetector implements IWordDetector {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c) {
		return Character.isLetter(c);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {
		return (Character.isLetter(c) || c=='_');
	}



}
