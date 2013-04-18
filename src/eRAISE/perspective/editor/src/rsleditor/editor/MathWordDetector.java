/**
 * 
 */
package rsleditor.editor;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Rule for accepting a character as
 * being part of a word replacing a math
 * symbol
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class MathWordDetector implements IWordDetector {

	/**
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c) {
		return Character.isLetter(c) || c=='-' || c=='~';
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {
		return (Character.isLetter(c)) || c=='-' || c=='~';
	}

}
