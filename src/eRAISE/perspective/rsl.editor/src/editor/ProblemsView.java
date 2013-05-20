/**
 * 
 */
package editor;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerUtilities;


/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class ProblemsView {

	private static ProblemsView instance = null;
	
	
	public void update(IFile file, Problem problem){
			
		try {
				
			int lineNumber = problem.getLinenumber();
			String message = problem.getMessage();
			//map that stores all marker properties
			HashMap<String, Integer> map = new HashMap<String, Integer>();
				
			MarkerUtilities.setLineNumber(map, lineNumber);
			MarkerUtilities.setMessage(map, message);
			
			map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
			
			if (problem.getOffset() != 0) {
				//map.put(IMarker.CHAR_START, pr.getOffset());
				//map.put(IMarker.CHAR_END, pr.getOffset()+10);
			}
			
			//first set values and then create marker
			MarkerUtilities.createMarker(file, map, IMarker.PROBLEM);
		
		
		} catch (CoreException e) {
			System.out.println("Error when displaying markers "+ e.getLocalizedMessage());
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Assures singleton 
	 * @return
	 */
	public static ProblemsView getInstance() {
		if(instance == null){
			instance = new ProblemsView();
		}
		return instance;
	}
	
}
