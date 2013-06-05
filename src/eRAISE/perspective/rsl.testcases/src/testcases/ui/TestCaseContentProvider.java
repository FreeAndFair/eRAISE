/**
 * 
 */
package testcases.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import testcases.model.RSLTestCaseModel;
import testcases.model.RSLTestFile;

/**
 * Provides the content for
 * the TestView
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class TestCaseContentProvider implements ITreeContentProvider{
	
	private RSLTestCaseModel model;
	private TreeViewer viewer;
	
	private static TestCaseContentProvider instance = null;
	
	public TestCaseContentProvider(){
		instance = this;
	}
	
	public static TestCaseContentProvider getInstance(){
		return instance;
		
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.model = (RSLTestCaseModel) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return model.getRSLTestFiles().toArray();
		
	}

	@Override
	public Object[] getChildren(Object parentElement) {
	    if (parentElement instanceof RSLTestFile) {
	      RSLTestFile testfile = (RSLTestFile) parentElement;
	      return testfile.getTestCases().toArray();
	    }
	   
	    return null;
	  }

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof RSLTestFile) {
			return true;
		}
		return false;
	}
	
	public void setViewer(TreeViewer treeViewer){
		this.viewer = treeViewer;
	}


	public RSLTestCaseModel getModel() {
		return this.model;
	}

	public void refreshView() {
		viewer.refresh();
		
	}


}
