/**
 * 
 */
package testcases.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import testcases.PluginLog;
import testcases.model.RSLTestFile;
import testcases.model.TestCase;

/**
 * Provides the labels for the TestView
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class TestCaseLabelProvider extends LabelProvider{
	
	private PluginLog log = PluginLog.getInstance();
	
	private static final Image TEST_TRUE = getIcon("testok.gif");
	private static final Image TEST_FALSE = getIcon("testfail.gif");
	private static final Image TEST_ERROR = getIcon("testerr.gif");
	  
	private static final Image FILE_TEST_TRUE = getIcon("file_true.gif");
	private static final Image FILE_TEST_FALSE = getIcon("file_false.gif");
	private static final Image FILE_TEST_ERROR = getIcon("file_error.gif");
	  
	private final String RUNTIME_ERR_FILE = "resources/sml_rt_errors.txt";
	
	@Override
	public String getText(Object element) {
	    if (element instanceof RSLTestFile) {
	      RSLTestFile rtf = (RSLTestFile) element;
	      return rtf.getName();
	    }
	  
	    if(element instanceof TestCase){
	    	TestCase tc = (TestCase) element;
	    	String tcText = tc.getName();
	    	if(tcText == "")
	    		return tc.getValue();
	    	return tcText+" "+tc.getValue();
	    }
	    return "";
	 
	  }

	@Override
	public Image getImage(Object element) {
		boolean runtimeErr = false;
		boolean falseTest = false;
	    if (element instanceof RSLTestFile) {
	    	List<TestCase> tflist= ((RSLTestFile) element).getTestCases();
	    	for(TestCase tc : tflist ){
	    		if( this.isRunTimeError(tc.getValue()))
	    			runtimeErr = true;
	    		if(tc.getValue().equals("false"))
	    			falseTest =true;
	    	}
	    	
	    	if(runtimeErr == true)
	    		return FILE_TEST_ERROR;
	    	if(falseTest == true)
	    		return FILE_TEST_FALSE;
	    	return FILE_TEST_TRUE;
	    }
	    
	    if(element instanceof TestCase){
	    	TestCase tc = (TestCase) element;
	    	if( tc.getValue().equals("false") )
	    		return TEST_FALSE;
	    	if( this.isRunTimeError(tc.getValue()))
	    		return TEST_ERROR;
	    	return TEST_TRUE;
	    }
	    return null;
	  }

	// Helper Method to load the images
	private static Image getIcon(String file) {
	    Bundle bundle = FrameworkUtil.getBundle(TestCaseLabelProvider.class);
	    URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
	    ImageDescriptor image = ImageDescriptor.createFromURL(url);
	    return image.createImage();

	} 
	
	public boolean isRunTimeError(String value) {
		URL url;
		try {
			
			String pathToRes ="platform:/plugin/rsl.testcases/"+RUNTIME_ERR_FILE; 
			url = new URL(pathToRes);
			
			log.debug("Runtime_err_file url: "+url.toString());
			
			InputStream inputStream = url.openConnection().getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			
			while ((line = in.readLine()) != null) {
			   String[] pieces = line.split(" x ");
			   
			   if(pieces.length == 1 && value.contains(pieces[0])){ //it contains one line with no x
				  log.debug("Just one line "+pieces[0]);
				  in.close();
				  return true;
			   }
			   
			   int i=0;
			   for(i=0; i<pieces.length; i++){
				   //log.debug(" "+pieces[i]);
				   if(!value.contains(pieces[i])) //does not contain one of the pieces
					   i = pieces.length + 3;
			   }
			   
			   if(i < pieces.length+3){//if all pieces were found in the test value
				   in.close();
				   return true;
			   }
			}
			in.close();
			
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		
		return false;
	}
}
