/**
 * 
 */
package testProject;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import core.guihandlers.GenerateLatexActiveFile;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class DocProjectTest {
	
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
	
	/**
	 * Test if the doc translation creates a new doc Project
	 * with all the correct structure 
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws CoreException
	 */
	@Test
	public void testDocProjectCreation() throws InterruptedException, IOException, CoreException{
		
		String projectName = "testDocProject";
		String rslFileName = "COUNTER.rsl";
		String content = "scheme COUNTER = class end";
		
		IProject project = createRSLProject(projectName);
		
		//add RSL file
		IFile rslFile = project.getFile("src/"+rslFileName);
		rslFile.create(new ByteArrayInputStream(content.getBytes()), true, progressMonitor);
		        
        //Generate Latex        
		GenerateLatexActiveFile.generateLatex(rslFile);
        
        //check that the Doc Project exists
        IProject docproject = root.getProject(projectName+"Doc");
        Assert.assertNotNull(docproject);
       
        //test that the project contains .project file
        IFile projectFile = docproject.getFile(".project");
        if( !projectFile.exists() )
        	Assert.fail();
        
        //test that the project contains the src folder
        IFolder srcFolder = docproject.getFolder("src");
        if( !srcFolder.exists() )
        	Assert.fail();
        
        IFile texFile = srcFolder.getFile("main.tex");
        if(!texFile.exists())
        	Assert.fail();
        
        //docproject.delete(true, progressMonitor);
        project.delete(true, progressMonitor);
 
	}

	/**
	 * Test that Doc project was created even when RSL file is not correct
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws CoreException
	 */
	@Test
	public void testDocProjectCreationIncorrectFile() throws InterruptedException, IOException, CoreException{
		
		String projectName = "testDocWrongProject";
		String rslFileName = "badFile.rsl";
		String content = "scheme class end";
		
		IProject project = createRSLProject(projectName);
		
		//add RSL file
		IFile rslFile = project.getFile("src/"+rslFileName);
		rslFile.create(new ByteArrayInputStream(content.getBytes()), true, progressMonitor);
		        
        //Generate Doc        
		GenerateLatexActiveFile.generateLatex(rslFile);
        
		//check that the Doc Project exists
        IProject docproject = root.getProject(projectName+"Doc");
        Assert.assertNotNull(docproject);
       
        //test that the project contains .project file
        IFile projectFile = docproject.getFile(".project");
        if( !projectFile.exists() )
        	Assert.fail();
        
        //test that the project contains the src folder
        IFolder srcFolder = docproject.getFolder("src");
        if( !srcFolder.exists() )
        	Assert.fail();
        
        IFile texFile = srcFolder.getFile("main.tex");
        if(!texFile.exists())
        	Assert.fail();
        
        //docproject.delete(true, null);
        project.delete(true, progressMonitor);
	}

	
	private IProject createRSLProject(String projectName) throws CoreException{
		//create project
		IProject project = root.getProject(projectName);
		project.create(progressMonitor);
		project.open(progressMonitor);
		
		//add source folder
		IFolder sourceFolder = project.getFolder("src");
		sourceFolder.create(false, true, null);
		
		return project;
		
	}

}
