/**
 * 
 */
package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;


/**
 * Translates RSL specifications to SML code
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class SMLTranslator {
	
	private static PluginLog log = PluginLog.getInstance();
	/**
	 * Translates to SML an RSL file
	 * 
	 * @param context an RSL file
	 */
	public String translate(IFile ifile){
		log.debug("----Inside SML translate");
		
		String infomessage = "";
		Process process = null;
		
		//path of the file 
		IPath ipath = ifile.getLocation();
		String filePath = ipath.toString();
			
		ProcessBuilder builder = null;		
		String os = System.getProperty("os.name");
		
		String programPath = System.getProperty("rsltc");
		
		if( programPath == null){
			Activator.getDefault().setBinariesPath();
			programPath = System.getProperty("rsltc");
		}
		
		if( os.indexOf("win") >= 0 || os.indexOf("Win")>=0){
			log.debug("os: Windows");
			/*
			 * to execute a DOS command from a Java program, you need to prepend
			 * the Windows command shell cmd /c to the command
			 * you want to execute. In other words the type checker
			 * needs to be wrapped in the cmd.
			 * The '/c' switch terminates the command shell after the command completes
			 */
			
			log.debug("Building program: cmd /c \n    "+ programPath+ " -m" +" \n   "+filePath);
			builder = new ProcessBuilder("cmd", "/C", programPath, "-m", filePath);
	
		}
		else
			if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 ){
				
				log.debug("Linux \n rsltc path: "+programPath);
			
				//give rights
				ProcessBuilder giveRights = new ProcessBuilder("chmod", "777",programPath);
				log.debug("Rights given");
			
				try {
					giveRights.start();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
				
				log.debug("New process builder ");
				builder = new ProcessBuilder(programPath, "-m", filePath);
			}
		
		//correlate the error messages with the output messages
		builder.redirectErrorStream(true);
		
		try {
			//start the program
			log.debug("Process starts...");
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
			log.debug("Process ends");
		
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		
		log.debug("----Returning message ");
		//return sml translator message
		return infomessage;
	
				
	}
	
}
