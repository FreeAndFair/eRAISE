/**
 * 
 */
package rsleditor.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;

import rsleditor.TypeCheck;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RSLEditor extends TextEditor {

	/**
	 *Stores the color manager 
	 */
	private ColorManager colorManager;

	/**
	 * Constructor that instantiates the color manager,
	 * the document provider and the source viewer
	 */
	public RSLEditor() {
		super();
		colorManager = new ColorManager();
		
		SourceViewerConfiguration viewerCongif = new RSLConfiguration(colorManager);
		setSourceViewerConfiguration(viewerCongif);
		
		FileDocumentProvider documentProvider = new RSLDocumentProvider();
		setDocumentProvider(documentProvider);
	}
	
	
	/**
	 * The method reacts only when
	 * something has changed 
	 */
	@Override
	public void doSave(IProgressMonitor monitor){
		super.doSave(monitor);		
		
		//call type check
		TypeCheck tc = new TypeCheck();
		tc.typeCheck();
		
	}
	
	@Override
	public void doSaveAs(){
		super.doSaveAs();
		
		//call type check
		TypeCheck tc = new TypeCheck();
		tc.typeCheck();
	}

	
	/**
	 * Disposes the color manager and the rsl editor
	 */
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
