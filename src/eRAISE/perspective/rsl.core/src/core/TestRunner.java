package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Executes the SML files
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class TestRunner{
	
	private static PluginLog log = PluginLog.getInstance();

	public String execute(IFile ifile){
		log.debug("----Enter test execute");
		
		String infomessage = "";
		Process process = null;
		ProcessBuilder builder = null;
		
		//path of the file opened in the editor
		IPath ipath = ifile.getLocation();
		String smlfilePath = ipath.toString();

		String os = System.getProperty("os.name");
		
		String smlnjPath = System.getProperty("smlnj");
		String rslmlPath = System.getProperty("rslml");
		
		if( smlnjPath == null || rslmlPath == null){
			Activator.getDefault().setBinariesPath();
			smlnjPath = System.getProperty("smlnj");
			rslmlPath = System.getProperty("rslml");
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
			
			log.debug("Building process: cmd c/ sml "+"\n   "+smlfilePath);
			builder = new ProcessBuilder("cmd","/C", "sml", smlfilePath);
			
			
			Map<String, String> env = builder.environment();
			log.debug("Setting environment variable: ");
			if (! env.containsKey("Path")) {
		    	env.put("Path", smlnjPath+"\\bin\\");
		    	log.debug("New variable: path= "+ smlnjPath+"\\bin\\");
		    }
			else{
				String path = env.get("Path");
				if(!path.contains(smlnjPath+"\\bin\\")){
					path = path.concat(";"+smlnjPath+"\\bin\\");
					env.put("Path", path);
					log.debug("Add to path: ;"+ smlnjPath+"\\bin\\");
				}
				log.debug("Path already exists and contains: "+smlnjPath);
			}
			
		    if (! env.containsKey("SMLNJ_PATH")) {		    
		    	env.put("SMLNJ_HOME", smlnjPath);
		    	log.debug("New variable: SMLNJ_HOME= "+ smlnjPath);
		    	
		    }
		    
		    if (! env.containsKey("RSLML_PATH")) {
		    	env.put("RSLML_HOME", rslmlPath);
		    	log.debug("New variable: RSLML_HOME= "+ rslmlPath);
		    }
		    
		}
		
		else
			if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 ){
				log.debug("Running SML on "+os);
				//give rights
				String programPath = findPluginResource("rsl.core", "resources/linux/sml/bin/sml");
				log.debug("sml path: "+programPath);
				ProcessBuilder giveRights = new ProcessBuilder("chmod", "777",programPath);
				
				String archPath = findPluginResource("rsl.core", "resources/linux/sml/bin/.arch-n-opsys");
				ProcessBuilder giveRightsArch = new ProcessBuilder("chmod", "777", archPath);
				
				String runPath = findPluginResource("rsl.core", "resources/linux/sml/bin/.run");
				ProcessBuilder giveRightsRun = new ProcessBuilder("chmod", "777", "-R", runPath);
				
				String heapPath = findPluginResource("rsl.core", "resources/linux/sml/bin/.heap");
				ProcessBuilder giveRightsHeap = new ProcessBuilder("chmod", "777", "-R", heapPath);
				
				log.debug("Rights given");
				try {
					giveRights.start();
					giveRightsArch.start();
					giveRightsRun.start();
					giveRightsHeap.start();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
				
				
				log.debug("building builder");
				builder = new ProcessBuilder(programPath, smlfilePath);
				
				Map<String, String> env = builder.environment();				
				log.debug("sml full path: "+smlnjPath);
				
				if (! env.containsKey("Path")) {			    	
			    	env.put("Path", smlnjPath+"/bin/");
			    	log.debug("Adding path: "+smlnjPath+"/bin/");
			    }
				else{
					String path = env.get("Path");
					if(!path.contains(smlnjPath+"/bin/")){
						path = path.concat(";"+smlnjPath+"/bin/");
						env.put("Path", path);
						log.debug("Adding path: ;"+smlnjPath+"/bin/");
					}
				}
				
			    if (! env.containsKey("SMLNJ_HOME")) {
			    	env.put("SMLNJ_HOME", smlnjPath);
			    	log.debug("Adding variable SMLNJ_HOME: "+smlnjPath);
			    }
			    			 
			    if (! env.containsKey("RSLML_HOME")) {			
			    	env.put("RSLML_HOME", rslmlPath);
			    	log.debug("Adding variable RSLML_HOME: "+rslmlPath);
			    }
			    
				
			}
	
		//correlate the error messages with the output messages
		builder.redirectErrorStream(true);
	    
		try {
			//start the program
			log.debug("Process starts....");
			process = builder.start();
		
			//get the input stream
		
			OutputStream stdin = process.getOutputStream ();
			InputStream stdout = process.getInputStream ();
			
			BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
			
			//read the program output line by line
			String line = reader.readLine();
			
			//apparently bash receives EOF on its stdin and exits when close its called 
			writer.close();
			
			while (line != null && ! line.trim().equals("--EOF--")) {
		        infomessage += line+"\n";
		        line = reader.readLine();
		    }
			log.debug("Process ends");
		
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		
		//return test case execution message
		log.debug("----Returning message: "+infomessage);
		return infomessage;
				
	}

	
	/**
	 * This method takes a relative path of a file and
	 * returns the absolute path of the file according
	 * to the runtime workspace
	 * @param pluginName  The name of the plugin
	 * @param path  The path of the file within the plugin
	 * @return The absolute file system location of the target file
	 * 
	 */
	  public static String findPluginResource(final String pluginName, 
	                                          final String path){
		  log.debug("----Enter findPLuginresource");
	   
		  final Bundle bundle = Platform.getBundle(pluginName);
	    
		  log.debug("Getting bundle");
		  IPath p = new Path(path);
		  final URL url = FileLocator.find(bundle, p, null);
		  log.debug("Getting url: "+url);
	 
		  URL resolvedURL=null;
		  try {
			  resolvedURL = FileLocator.resolve(url);
			  log.debug("Resolved url: "+resolvedURL);
		  } catch (IOException e) {
			  log.error(e.getMessage(), e);
		  }
		  final String filePath = resolvedURL.getPath();
		  log.debug("Resolved filePath: "+filePath);
	    
		  final Path resourcePath = new Path(filePath);
		  p = resourcePath.makeAbsolute();
	    
		  log.debug("ResourcePath: "+p);
		  String s = p.toOSString();
	    
		  if(s.startsWith("file:/") || s.startsWith("file:\\"))
			  s = s.substring(6);
	      
		  log.debug("----Returning "+s);
		  return s;
	  }
	
}
