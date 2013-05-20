package editor.config;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * Registers the source code scanners to 
 * the presentation reconciler
 *
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RSLConfiguration extends SourceViewerConfiguration {

	/**
	 * Stores the instance of the double click strategy
	 */
	private RSLDoubleClickStrategy doubleClickStrategy;
	
	/**
	 * The editor color manager
	 */
	private ColorManager colorManager;
	
	/**
	 * Instance of the code scanner
	 */
	private RSLCodeScanner codeScanner;
	
	/**
	 * Constructor.
	 * @param colorManager The editor color manager
	 */
	public RSLConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	
	/**
	 * Returns the existing content types
	 */
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			RSLPartitionScanner.RSL_COMMENT 
			};
	}
	
	/**
	 * Returns the instance of the double click strategy.
	 * If one does not exit, this method creates one 
	 */
	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer,
			String contentType) {
			if (doubleClickStrategy == null)
				doubleClickStrategy = new RSLDoubleClickStrategy();
			return doubleClickStrategy;
		}
	
	/**
	 * Returns the instance of the code scanner.
	 * If one does not exit, this method creates one
	 * @return the code scanner instance
	 */
	private RSLCodeScanner getRSLCodeScanner() {
		if (codeScanner == null) {
			codeScanner = new RSLCodeScanner(colorManager);
			codeScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IRSLSyntaxColors.BLACK))));
		}
		return codeScanner;
	}


	/**
	 * Returns the presentation reconciler used with the
	 * RSL editor
	 */
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {	
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr= new DefaultDamagerRepairer
				(getRSLCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		
		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(IRSLSyntaxColors.RED)));
		reconciler.setDamager(ndr, RSLPartitionScanner.RSL_COMMENT);
		reconciler.setRepairer(ndr, RSLPartitionScanner.RSL_COMMENT);

		return reconciler;
	}
		
}
