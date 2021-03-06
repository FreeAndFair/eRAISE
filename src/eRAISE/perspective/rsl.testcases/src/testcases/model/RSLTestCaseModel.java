/**
 * 
 */
package testcases.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import testcases.PluginLog;


/**
 * Stores the model that will be displayed in RTestView
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RSLTestCaseModel {
	private PluginLog log = PluginLog.getInstance();
	
	List<RSLTestFile> rtfList = new ArrayList<RSLTestFile>();
	
	public void addRSLFile(RSLTestFile rslfile){
		rtfList.add(rslfile);
		log.debug("Added "+rslfile.getName()+" to list");
		Collections.sort(rtfList);
	}
	
	public void removeRSLFile(RSLTestFile rslfile){
		if(rtfList.contains(rslfile))
			rtfList.remove(rslfile);
	}
	
	public List<RSLTestFile> getRSLTestFiles() { 
		    return rtfList;
	}
	
	 
	public void mockModel(){

	}

	public RSLTestFile hasFile(String name) {
		if(rtfList.isEmpty() == false){
			for(int i=0; i<rtfList.size() ;i++){
				RSLTestFile file = rtfList.get(i); 
				if(file.getName().equals(name))
					return file;
			}
		}
		return null;
	}
}
