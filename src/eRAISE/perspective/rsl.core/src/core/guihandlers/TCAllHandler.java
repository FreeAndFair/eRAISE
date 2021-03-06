/**
 * 
 */
package core.guihandlers;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.swt.custom.BusyIndicator;

import core.Console;
import core.PluginLog;


/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class TCAllHandler extends AbstractHandler{

	private static PluginLog log = PluginLog.getInstance();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//get all projects available in workspace
		IWorkspaceRoot root = org.eclipse.core.resources.ResourcesPlugin.getWorkspace().getRoot();
		final IProject[] projects = root.getProjects();
		
		final TCHandler tch = new TCHandler();
		
		//clear console
		Console.getInstance().clear();
		
		// Show a busy indicator while the runnable is executed
		BusyIndicator.showWhile(null, new Runnable() {

			@Override
			public void run() {
				for(int index = 0; index < projects.length; index++){
					log.debug("For each project call typeCheckObject");
					tch.typeCheckObject(projects[index]);
				}
			}
		});
		return null;
	}

}
