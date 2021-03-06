/**
 * 
 */
package core.guihandlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import core.Activator;
import core.Console;
import core.ResourceHandler;
import core.PluginLog;
import core.TestRunner;


/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RunTestCasesActiveFile extends AbstractHandler {
	
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
			log.error(e.getMessage(), e);
			return null;
		}
	
		if (input instanceof FileEditorInput) {
		    final IFile ifile = ((FileEditorInput) input).getFile();
		    //clear console
		    Console.getInstance().clear();
		    
		    //call the runandprint if ifile is a file
		    log.debug("Checking if file is null");
			if(ifile != null){
				 
				log.debug("File is not null");
		        BusyIndicator.showWhile(null, new Runnable() {

					@Override
					public void run() {
						
						log.debug("Calling Activator to notify listeners");
						Activator.getDefault().testsStarted();
				
						runandprint(ifile);
					}
		        });
			}
		}
	
		return null;
	}
	
	/**
	 * 
	 * @param ifile
	 */
	public static void runandprint(IFile ifile) {
		
		log.debug("Calling SML translate first");
		boolean translate = SMLTranslateActiveFile.translateandprint(ifile);
		
		//if the SML file was created
		if(translate == true){
			
			//execute the test cases in this ifile
			log.debug(" SML was corectly translated");
			//gather data			
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject smlProject = root.getProject(ifile.getProject().getName()+"SML");
			
			IPath rslPath = ifile.getProjectRelativePath().removeLastSegments(1); 
			String rslFileName = ifile.getName();
			int dotPosition = rslFileName.lastIndexOf(".");
			String smlFileName = rslFileName.substring(0, dotPosition).concat(".sml");
			String smlFileName_ = rslFileName.substring(0, dotPosition).concat("_.sml");
			IFile smlIFile = smlProject.getFile(rslPath.append(smlFileName));
			
			//edit SML file
			log.debug("Calling edit file: "+smlFileName);
			ResourceHandler.editFile(smlIFile, smlProject.getLocation().append(rslPath).append(smlFileName_));
						
			TestRunner smlnj = new TestRunner();
			//execute file
			
			log.debug("Calling smlnj execute");
			String message = smlnj.execute(smlIFile);
			
			Activator.getDefault().testsFinished(message, ifile);
		}
	}

}
