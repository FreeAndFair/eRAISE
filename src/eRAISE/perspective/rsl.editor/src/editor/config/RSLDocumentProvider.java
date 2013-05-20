package editor.config;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * **
 * Document provider for the RSL files. It extends FileDocumentProvider
 * which is specialized for file resources
 *
 */
public class RSLDocumentProvider extends FileDocumentProvider {

	/**
	 * Creates a document representation 
	 * of the Object received as parameter
	 */
	protected IDocument createDocument(Object element) throws CoreException {
		
		//create the document
		IDocument document = super.createDocument(element);
		
		//create a new document partitioner and attach it to the document
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(
					new RSLPartitionScanner(),
					new String[] { RSLPartitionScanner.RSL_COMMENT});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}
