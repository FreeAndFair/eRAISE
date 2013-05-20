/**
 * 
 */
package testcases.model;

/**
 * Represents the second level in the RTestView
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class TestCase {
	 private String name = "";
	 private String value = "";

	 
	  public TestCase(String name) {
	    this.name = name;
	  }

	  public TestCase(String name, String value) {
	    this.name = name;
	    this.value = value;

	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  public String getValue() {
	    return value;
	  }

	  public void setValue(String value) {
	    this.value = value;
	  }

}
