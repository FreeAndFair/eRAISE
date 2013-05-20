/**
 * 
 */
package core;

import org.eclipse.core.resources.IFile;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public interface IRSLTypeCheckListener {
	
	public void typecheckFinished(String message, IFile rslfile);

	public void typecheckStarted();
}
