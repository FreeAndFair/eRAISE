package rsleditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;




public class TypeCheckHandler implements IEditorActionDelegate{

	//private IWorkbenchPart targetPart;
	
	public TypeCheckHandler() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
	
		TypeCheck tc = new TypeCheck();
		tc.typeCheck();
				
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart editor) {
		//this.targetPart = editor;
		
	}
	
	public void setActivePart(IAction action, IWorkbenchPart part){
		//this.targetPart = part;
	}

}
