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

import core.guihandlers.SMLTranslateActiveFile;


/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class SMLProjectTest {
	
	
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
	 * Test if the SMLtranslation creates a new SMLFolder
	 * with all the correct structure 
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws CoreException
	 */
	@Test
	public void testSMLProjectCreation() throws InterruptedException, IOException, CoreException{
		
		String projectName = "testSMLProject";
		String rslFileName = "COUNTER.rsl";
		String content = "scheme COUNTER = class end";
		
		IProject project = createRSLProject(projectName);
		
		//add RSL file
		IFile rslFile = project.getFile("src/"+rslFileName);
		rslFile.create(new ByteArrayInputStream(content.getBytes()), true, progressMonitor);
		        
        //SML translate        
		SMLTranslateActiveFile.translateandprint(rslFile);
        
        //check that the SML Project exists
        IProject smlproject = root.getProject(projectName+"SML");
        Assert.assertNotNull(smlproject);
       
        //test that the project contains .project file
        IFile projectFile = smlproject.getFile(".project");
        if( !projectFile.exists() )
        	Assert.fail();
        
        //test that the project contains the src folder
        IFolder srcFolder = smlproject.getFolder("src");
        if( !srcFolder.exists() )
        	Assert.fail();
        
        IFile smlFile1 = srcFolder.getFile("COUNTER.sml");
        if(!smlFile1.exists())
        	Assert.fail();
        
        IFile smlFile2 = srcFolder.getFile("COUNTER_.sml");
        if(!smlFile2.exists())
        	Assert.fail();
        
        project.delete(true, progressMonitor);
        smlproject.delete(true, progressMonitor);

	}

	/**
	 * Test that project was not created when RSL file is not correct
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws CoreException
	 */
	@Test
	public void testSMLProjectCreationIncorrectFile() throws InterruptedException, IOException, CoreException{
		
		String projectName = "testSMLWrongProject";
		String rslFileName = "TEST.rsl";
		String content = "scheme TEST = end";
		
		IProject project = createRSLProject(projectName);
		
		//add RSL file
		IFile rslFile = project.getFile("src/"+rslFileName);
		rslFile.create(new ByteArrayInputStream(content.getBytes()), true, progressMonitor);
		        
        //SML translate        
		SMLTranslateActiveFile.translateandprint(rslFile);
        
        //check that the SML Project does not exist
        IProject smlproject = root.getProject(projectName+"SML");
        if(smlproject.exists())
        	Assert.fail();
        
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
