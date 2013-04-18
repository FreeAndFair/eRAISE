/**
 * 
 */
package tc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import core.Activator;
import core.Console;




/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class TypeChecker {

	/**
	 * Type checks an RSL file
	 * and calls Console to display the output
	 * 
	 * @param context an RSL file
	 */
	public void typeCheck(IFile ifile){
		String infomessage = "";
		Process process = null;
		
		//path of the file opened in the editor
		IPath ipath = ifile.getLocation();
		String filePath = ipath.toString();
		
		//get the absolute path to the rsltc 
		String programPath = accessRuntimeResource("resources/rsltc.exe");
		
		/*
		 * to execute a DOS command from a Java program, you need to prepend
		 * the Windows command shell cmd /c to the command
		 * you want to execute. In other words the type checker
		 * needs to be wrapped in the cmd.
		 * The '/c' switch terminates the command shell after the command completes
		 */
		String commands[] = {"cmd","/C", programPath, filePath};
		
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
						
			while (line != null && ! line.trim().equals("--EOF--")) {
		        infomessage += line+"\n";
		        line = br.readLine();
		    }
		
		} catch (IOException e) {
			infomessage += e.getMessage();
			e.printStackTrace();
		}
		
		int channel = 1;
		//wait to see if program exited successfully or not
		try {
            channel = process.waitFor();
            //infomessage +="\nThe type check was: " + (exitValue == 0 ? "successful": "unsuccessful");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		
		Console cons = Console.getInstance();
		
		//ConsoleToProblems ctp = new ConsoleToProblems();
		//System.out.println("Added observer");
		//cons.addObserver(ctp);
		//call console to print the message
		
		cons.print(infomessage, channel, ifile);
	
				
	}
	
	/**
	 * This method takes a relative path of a file and
	 * returns the absolute path of the file according
	 * to the runtime workspace
	 * 
	 * @param relativePath
	 * @return the absolute path  
	 */
	private static String accessRuntimeResource(String relativePath) {
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
