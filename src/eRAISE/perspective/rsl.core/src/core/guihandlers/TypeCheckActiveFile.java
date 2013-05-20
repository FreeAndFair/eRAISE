/**
 * 
 */
package core.guihandlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import core.Console;
import core.ResourceHandler;
import core.PluginLog;
import core.TypeChecker;


/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class TypeCheckActiveFile extends AbstractHandler {


	private static PluginLog log = PluginLog.getInstance();
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		//Get the editor
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();		
		IEditorInput input = null;
		
		try{
			input = editorPart.getEditorInput();
		}catch(Exception e){
			log.error("Error when trying to get file from editor",e);
			return null;
		}
	
		if (input instanceof FileEditorInput) {
			final IFile ifile = ((FileEditorInput) input).getFile();
		    
		    //clear console
		    Console.getInstance().clear();
		    
		    log.debug("Checking if file is null");
		    //call the type checker if ifile is a file
			if(ifile != null){
			
				log.debug("File"+ ifile.getName()+" is not null");
				BusyIndicator.showWhile(null, new Runnable() {
					@Override
					public void run() {
						typecheckAndPrint(ifile);
					}
				});
			}
		}
	
		return null;
		
	}
	
	public static void typecheckAndPrint(IFile ifile){
		//clear markers first
		ResourceHandler.clearMarkers(ifile);
		
		//type check file
		log.debug("Calling typecheck for "+ifile.getLocation());
		
		TypeChecker tc = new TypeChecker();		
		tc.typeCheck(ifile);
		
	}
	
	

}
