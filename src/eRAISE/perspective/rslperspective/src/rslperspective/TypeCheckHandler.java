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
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
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
		String parameter = accessRuntimeResource("resources/COUNTER.rsl");
		
		//String[] commands={"notepad"};
		//String[] commands={"java","-version"};	
		//String commands[] = {"rsltc.exe", "SET_DATABASE.rsl"};
		
		/*
		 * to execute a DOS command from a Java program, you need to prepend
		 * the command to run the Windows command shell cmd /c to the command
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
		
		//wait to see if program exited successfully or not
		try {
            int exitValue = process.waitFor();
            infomessage +="\nThe type check was: " + (exitValue == 0 ? "successful": "unsuccessful");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		//display the result in a info view
		MessageDialog.openInformation(null, "Info", infomessage);
		
		return null;
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