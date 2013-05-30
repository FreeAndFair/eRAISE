/**
 * 
 */
package testProject;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

import wizard.NewRSLProjectWizard;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RSLProjectTest {
	
	NewRSLProjectWizard RSLProject = new NewRSLProjectWizard();
	IProgressMonitor progressMonitor;
	IWorkspaceRoot root;
	
	/**
	 * Get root of the workspace
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Before
	public void setUp(){
		
		progressMonitor = new NullProgressMonitor();
		root = ResourcesPlugin.getWorkspace().getRoot();
        
	}
	
	
	@Test
	public void testProjectCreation() throws CoreException, InterruptedException{
		String projectName = "testProject";
		String projectPath = "D:/RAISE";
		String[] projectArgs = {projectName, projectPath};
		
		//call the project creation method
		RSLProject.createRSLProject(projectArgs, progressMonitor);
	
		//test that the project contains .project file
		File projectFile = new File("D:/RAISE/testProject/.project");
		if( !projectFile.exists() )
			Assert.fail();
		
		//test that the project contains the src folder
		File srcFolder = new File("D:/RAISE/testProject/src");
		if( !srcFolder.exists() )
			Assert.fail();
		
	}
	@Test
	public void testProjectInsideWorkspace() throws CoreException, InterruptedException{
		String projectName = "testPrj";
		String[] projectArgs = {projectName, root.getLocation().toOSString()};
		
		//call the project creation method
		RSLProject.createRSLProject(projectArgs, progressMonitor);
		
		//test that the project was created
		IProject rslProject = root.getProject(projectName);
		if(! rslProject.exists())
			Assert.fail();
		
		//test that the project contains .project file
        IFile projectFile = rslProject.getFile(".project");
        if( !projectFile.exists() )
        	Assert.fail();
        
        //test that the project contains the src folder
        IFolder srcFolder = rslProject.getFolder("src");
        if( !srcFolder.exists() )
        	Assert.fail();
        
        rslProject.delete(true, null);
       
	}
}
