/**
 * 
 */
package rslperspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class PerspectiveFactoryRSL implements IPerspectiveFactory {
	
	/**
	 * Proof view ID
	 */
	//private static final String ID_PROOF_VIEW = "rsl.perspective.view.proof";
	
	/**
	 * REPL view ID
	 */
	private static final String ID_REPL_VIEW = "rslperspective.view.repl";

	/**
	 * RTest view ID
	 */
	private static final String ID_RTEST_VIEW = "rslperspective.view.rtest";

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		//get editor area
		String editorArea = layout.getEditorArea();
		
		//put the Explorer view on the left
		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.20f, editorArea);
		
		//put the outline view in the  right
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, 0.75f, editorArea);
		
		//put Consoles, Problems and REPL views under the editor
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.66f, editorArea);		
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView(ID_REPL_VIEW);
		
		//put Proof view under the Outline view
		//layout.addView(ID_PROOF_VIEW, IPageLayout.BOTTOM, 0.5f, IPageLayout.ID_OUTLINE);
		
		//put RTest view under the Project Explorer
		layout.addView(ID_RTEST_VIEW, IPageLayout.BOTTOM, 0.5F, IPageLayout.ID_PROJECT_EXPLORER);

	}

}
