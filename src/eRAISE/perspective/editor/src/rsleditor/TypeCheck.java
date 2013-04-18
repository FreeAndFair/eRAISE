/**
 * 
 */
package rsleditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import tc.TypeChecker;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class TypeCheck extends TypeChecker {

	/**
	 * 
	 */
	public TypeCheck() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Get file opened in editor
	 * and call type check on it
	 */
	public void typeCheck(){
		//Get the editor
		
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		IFile ifile = null;
		IEditorInput input = null;
		
		try{
			input = editorPart.getEditorInput();
		}catch(Exception e){
			System.out.println("Error when trying to get file from editor");
			return;
		}
	
		if (input instanceof FileEditorInput) {
		    ifile = ((FileEditorInput) input).getFile();
		    //call the type checker if ifile is a file
			if(ifile != null){
				clear(ifile);
				super.typeCheck(ifile);				
			}
		}

	
	}
	
	public void clear(IFile file){
		//delete all markers of a file
		try {
			file.deleteMarkers(null, true, IResource.DEPTH_INFINITE);
			//System.out.println("delete markers for file"+file.getName());
		} catch (CoreException e) {
			System.out.println("Error when deleting a file markers "+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

}
