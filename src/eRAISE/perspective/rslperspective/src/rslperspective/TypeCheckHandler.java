/**
 * code inspired from http://wiki.eclipse.org/FAQ_How_do_I_write_to_the_console_from_a_plug-in%3F
 * and 
 */
package rslperspective;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
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
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.*;
import org.osgi.framework.Bundle;

/**
 * Handler of the 'TypeCheck' button
 */

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class TypeCheckHandler extends AbstractHandler implements IHandler {

	/**
	 * The method executed when the Type check  
	 * button is pressed.
	 * 
	 */
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String infomessage = "";
		Process process = null;
		
		//get the absolute paths to the rsltc and a rsl file
		String programPath = accessRuntimeResource("resources/rsltc.exe");
		//String parameter = accessRuntimeResource("resources/SET_DATABASE.rsl");
		//String parameter = accessRuntimeResource("resources/COUNTER.rsl");
		
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		IPath ipath = null;
		
		IEditorInput input = editorPart.getEditorInput();
		if (input instanceof FileEditorInput) {
		    IFile file = ((FileEditorInput) input).getFile();
		    ipath = file.getLocation();
		}

		String parameter = ipath.toString();
		
		/*
		 * to execute a DOS command from a Java program, you need to prepend
		 * the Windows command shell cmd /c to the command
		 *  you want to execute
		 * The '/c' switch terminates the command shell after the command completes
		 */
		String commands[] = {"cmd","/C", programPath, parameter};
		
		ProcessBuilder builder = new ProcessBuilder(commands);
		//correlate the error messages with the output messages
		builder.redirectErrorStream(true);
		
		try {
			//start the program
			process = builder.start();
		
			//get the input stream
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			//read the program output line by line
			String line = br.readLine();
			
			infomessage+= "Output is: \n";			
			while (line != null && ! line.trim().equals("--EOF--")) {
		        infomessage += line+"\n";
		        line = br.readLine();
		    }
		
		} catch (IOException e) {
			infomessage += e.getMessage();
			e.printStackTrace();
		}
		
		int exitValue = 1;
		//wait to see if program exited successfully or not
		try {
            exitValue = process.waitFor();
            infomessage +="\nThe type check was: " + (exitValue == 0 ? "successful": "unsuccessful");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		//display the result in a info view
		//MessageDialog.openInformation(null, "Info", infomessage+"\nThe type check was: " + (exitValue == 0 ? "successful": "unsuccessful"));
		
		RSLConsoleDisplay(infomessage, exitValue);
		
		return null;
	}

	/**
	 * Displays output to plugin Console
	 * @param infomessage
	 * @param exitValue
	 */
	private void RSLConsoleDisplay(String infomessage, int exitValue) {
		//make the console visible
		//IConsole myConsole = ...;// your console instance
		
		// obtain the active page
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		
		String id = IConsoleConstants.ID_CONSOLE_VIEW;
		IConsoleView view=null;
		
		try {
			view = (IConsoleView) page.showView(id);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MessageConsole rslConsole = findConsole(IConsoleConstants.ID_CONSOLE_VIEW);
		
		if(view != null){
			view.display(rslConsole);
			rslConsole.clearConsole();
			MessageConsoleStream out = rslConsole.newMessageStream();
			out.println(infomessage);
		}
		
	}

	/**
	 * Identifies a console by its id
	 *  
	 * @param idConsoleView
	 * @return
	 */
	private MessageConsole findConsole(String name) {
		
		ConsolePlugin plugin = ConsolePlugin.getDefault();
	    IConsoleManager conMan = plugin.getConsoleManager();
	    IConsole[] existing = conMan.getConsoles();
	    //search for the console
	    for (int i = 0; i < existing.length; i++)
	         if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	    
	    //create a new console 
	    MessageConsole tcConsole = new MessageConsole(name, null);
	    conMan.addConsoles(new IConsole[]{tcConsole});
	    return tcConsole;
	}

	/**
	 * This method takes a relative path of a file and
	 * returns the absolute path of the file according
	 * to the runtime workspace
	 * 
	 * @param relativePath
	 * @return 
	 */
	private String accessRuntimeResource(String relativePath) {
		File setupFile = null;
		
		//get access to bundle
		Bundle bundle = Activator.getDefault().getBundle();
		
		//get the runtime path to the resources/rsltc program
		IPath path = new Path(relativePath);				
		URL setupUrl = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
		
		//get the file with the absolute path 
		try {
			setupFile = new File(FileLocator.toFileURL(setupUrl).toURI());	
		
		} catch (URISyntaxException | IOException e) {			
			e.printStackTrace();
		}
	
		return setupFile.getAbsolutePath();
	}

}
