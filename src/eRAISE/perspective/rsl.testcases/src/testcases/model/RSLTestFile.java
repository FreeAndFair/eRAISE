/**
 * 
 */
package testcases.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the first level in the RTestView
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RSLTestFile implements IRSLTestFile{

	private String name;
	private List<TestCase> tests = new ArrayList<TestCase>();

	public RSLTestFile(){
		
	}
	
	public RSLTestFile(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TestCase> getTestCases() {
		return tests;
	}

	@Override
	public int compareTo(RSLTestFile o) {
		int i = this.name.compareTo(o.getName());
		return i;
	}
	
}
