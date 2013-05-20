/**
 * 
 */
package wizard;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RSLPerspective implements IPerspectiveFactory {

	
	/**
	 * RTest view ID
	 */
	private static final String ID_RTEST_VIEW = "rsl.testcases.testview";

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		//get editor area
		String editorArea = layout.getEditorArea();
		
		//add the Explorer view on the left
		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.30f, editorArea);
				
		//add Consoles, Problems and REPL views under the editor
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.66f, editorArea);		
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		
		//add RTest view under the Project Explorer
		layout.addView(ID_RTEST_VIEW, IPageLayout.BOTTOM, 0.5f, IPageLayout.ID_PROJECT_EXPLORER);

	}

}
