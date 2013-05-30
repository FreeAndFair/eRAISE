/**
 * 
 */
package output;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import core.guihandlers.SMLTranslateActiveFile;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class SMLOutputTest {
	IProgressMonitor progressMonitor;
	IWorkspaceRoot root;
	
	/**
	 * Get root of the workspace
	 * @throws PartInitException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Before
	public void setUp() throws PartInitException, InterruptedException{
		
		progressMonitor = new NullProgressMonitor();
		root = ResourcesPlugin.getWorkspace().getRoot();
		
		// obtain the active page
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
						
		IConsoleView view = null; 
				
		view = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);	
		MessageConsole rslConsole = findConsole(IConsoleConstants.ID_CONSOLE_VIEW);
						
		if(view != null){
			view.display(rslConsole);
		}
		waitJobs();
		delay(2000);
        
	}
	
	@Test 
	public void testCorrectTypeCheckOutput() throws CoreException, InterruptedException{
		String projectName = "testprjSML";
		IProject rslProject = createRSLProject(projectName);
		
		String rslFileName="COUNTER.rsl";
		String content="scheme COUNTER = class end";
		String outputMsg1 = "Checking COUNTER ... ";
		String outputMsg2 = "Finished COUNTER";
		String outputMsg3 = "SML output is in files COUNTER.sml and COUNTER_.sml";
		String outputMsg4 =	"rsltc completed: 0 error(s) 0 warning(s)";
		
		IFile rslFile = createRSLFile(rslProject, rslFileName, content);
		
		//SML translate file
		SMLTranslateActiveFile.translateandprint(rslFile);
				
		delay(2000);
		
	    //check Console exists
		MessageConsole rslConsole = findConsole(IConsoleConstants.ID_CONSOLE_VIEW);
	    Assert.assertNotNull(rslConsole);
	    
	    IDocument consoleDoc = rslConsole.getDocument();
	    //check Console doc exists
	    Assert.assertNotNull(consoleDoc);
	    
	    //check doc is not empty
	    if(consoleDoc.getLength() == 0)
	    	Assert.fail();
	    
	    if(!consoleDoc.get().contains(outputMsg1) || !consoleDoc.get().contains(outputMsg2) ||
	    		!consoleDoc.get().contains(outputMsg3) || !consoleDoc.get().contains(outputMsg4))
	    	Assert.fail();
		
	    delay(2000);
	    rslProject.delete(true, null);
	}
	
	
	@Test
	public void testTypeCheckOutputWithErrors() throws CoreException, InterruptedException{
		String projectName = "projecterrors";
		IProject rslProject = createRSLProject(projectName);
		
		String rslFileName="COUNTER.rsl";
		String content="scheme WRONG = class end";
		String outputMsg1 = "Checking WRONG ... ";
		String outputMsg2 = "Finished WRONG";
		String outputMsg3 = "Errors found, so SML translation cannot be generated";
		String outputMsg4 = "rsltc completed: 1 error(s) 0 warning(s)";
		
		IFile rslFile = createRSLFile(rslProject, rslFileName, content);
		
		//SML translate file
		SMLTranslateActiveFile.translateandprint(rslFile);
				
		delay(2000);
		
	    //check Console exists
		MessageConsole rslConsole = findConsole(IConsoleConstants.ID_CONSOLE_VIEW);
	    Assert.assertNotNull(rslConsole);
	    
	    IDocument consoleDoc = rslConsole.getDocument();
	    //check Console doc exists
	    Assert.assertNotNull(consoleDoc);
	    
	    //check doc is not empty
	    if(consoleDoc.getLength() == 0)
	    	Assert.fail();
	    
	    if(!consoleDoc.get().contains(outputMsg1) || !consoleDoc.get().contains(outputMsg2) ||
	    		!consoleDoc.get().contains(outputMsg3) || !consoleDoc.get().contains(outputMsg4))
	    	Assert.fail();
		
	    rslProject.delete(true, null);
	}
	
	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
	    IConsoleManager conMan = plugin.getConsoleManager();
	    IConsole[] existing = conMan.getConsoles();
	    //search for the console
	    for (int i = 0; i < existing.length; i++)
	         if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	    
	    //if none found create a new console 
	    MessageConsole tcConsole = new MessageConsole(name, null);
	    conMan.addConsoles(new IConsole[]{tcConsole});
	    return tcConsole;
	}

	private IFile createRSLFile(IProject project, String rslFileName, String content) throws CoreException {
		//add RSL file
		IFile rslFile = project.getFile("src/"+rslFileName);
		rslFile.create(new ByteArrayInputStream(content.getBytes()), true, progressMonitor);
		
		return rslFile;
				       
	}

	private IProject createRSLProject(String projectName) throws CoreException{
		//create project
		IProject project = root.getProject(projectName);
		project.create(progressMonitor);
		project.open(progressMonitor);
		
		//add source folder
		IFolder sourceFolder = project.getFolder("src");
		sourceFolder.create(false, true, null);
		
		return project;
		
	}
	
	private void delay(long sec) throws InterruptedException{
		Display display = Display.getCurrent();
		if(display != null){
			long endTime = System.currentTimeMillis()+ sec;
			while(System.currentTimeMillis() < endTime){
				if(!display.readAndDispatch())
					display.sleep();
			}
			display.update();
		}
		else
			Thread.sleep(sec);
	}
	
	private void waitJobs() throws InterruptedException{
		while(Job.getJobManager().currentJob()!=null)
			delay(1000);
	}

}
