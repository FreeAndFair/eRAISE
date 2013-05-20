/**
 * 
 */
package core;

import org.eclipse.core.resources.IFile;

/**
 * Interface that must be implemented by all
 * test cases listeners
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public interface IRSLTestCasesListener {
	
	public void testFinished(String message, IFile rslfile);

	public void testsStarted();
	
}
