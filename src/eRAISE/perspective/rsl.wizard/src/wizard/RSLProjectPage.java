package wizard;

import java.io.File;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Page collecting the user input
 *
 */
public class RSLProjectPage extends WizardPage{

	//Characters that can not exit in a directory name. The List is not complete
	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
	////Characters that can not exit in an absolute path. The list is not complete
	private static final char[] ILLEGAL_CHARACTERS_2 = { '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '<', '>', '|', '\"' };
	
	private static Text projectNameField;
	private static Text locationPathField;
	private Button browseButton;
	private Button linkButton;
	
	private ModifyListener pNameModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			updatePageComplete();
		}
	};
	
	private SelectionListener linkButtonListener = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			
			if(linkButton.getSelection()){
				locationPathField.setText(workspaceDirPath.toString());
				locationPathField.setEnabled(false);
				locationLabel.setEnabled(false);
				browseButton.setEnabled(false);
			}
			else{
				locationPathField.setText("");
				locationPathField.setEnabled(true);
				locationLabel.setEnabled(true);
				browseButton.setEnabled(true);
			}
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			locationPathField.setText(workspaceDirPath.toString());
			locationPathField.setEnabled(true);
			locationLabel.setEnabled(true);
			browseButton.setEnabled(true);
			
		}
	};
	
	private ModifyListener pLocationModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			//setPageComplete(validatePage());
			updatePageComplete();
		}
	};
			
	private SelectionListener browseLocationListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			browseForProjectLocation();
		}
	};	
	
	
	/**
	 * Stores the path of the current active workspace location
	 */
	private String workspaceDirPath = null;
	
	/**
	 * Used to display the "Location: " text in the page wizard
	 */
	private Label locationLabel = null;
	
	
	/**
	 * Constructor. Sets the name and the description of the page 
	 * @param pageName 
	 * @param description
	 */
	protected RSLProjectPage(String pageName,String description) {
		super(pageName);
		setTitle(pageName);
		setDescription(description);
		
	}

	/**
	 * Creates and aligns the page UI elements
	 */
	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
       
        layout.numColumns = 3;
        container.setLayout(layout);
 
        setControl(container);

		// new project label
		Label projectLabel = new Label(container, SWT.NONE);
		GridData gd1= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		projectLabel.setLayoutData(gd1);
		projectLabel.setText("Project name:");        
		
		// new project name entry field
		projectNameField = new Text(container, SWT.BORDER);		
		projectNameField.addModifyListener(pNameModifyListener);
        GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
        projectNameField.setLayoutData(gd2);
		projectNameField.setText("");		
        
		
		// new empty label
		Label emptyLabel1 = new Label(container, SWT.NONE);
		GridData gd3 = new GridData();
		gd3.horizontalSpan = 3;
		emptyLabel1.setLayoutData(gd3);
		
		
		//Use default location button
        linkButton = new Button(container, SWT.CHECK);        
        linkButton.setFont( parent.getFont());
        linkButton.addSelectionListener(linkButtonListener);
        GridData gd4= new GridData();
        gd4.horizontalAlignment= GridData.FILL;
        gd4.grabExcessHorizontalSpace= false;
        gd4.horizontalSpan= 3;
        linkButton.setLayoutData(gd4);				
        linkButton.setText("Use default location");	
        
		//Location label		
		locationLabel = new Label(container, SWT.NONE);		
		locationLabel.setFont(parent.getFont());
		GridData gd6 = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		
	    locationLabel.setLayoutData(gd6);
	    locationLabel.setText("Location:");

		// project location entry field
		locationPathField = new Text(container, SWT.BORDER);
		locationPathField.addModifyListener(pLocationModifyListener);		
		GridData gd7 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);	
		//gd.horizontalSpan = 2;
		locationPathField.setLayoutData(gd7);
		locationPathField.setText("");
		//locationPathField.setFont(dialogFont);		
		
		// browse button
		browseButton = new Button(container, SWT.NONE);
		browseButton.addSelectionListener(browseLocationListener );
		browseButton.setText("   Browse...   ");
			
        
		
		// Show description on opening
		
		initContents();
	}

	/**
	 * Populates the wizard page with content already selected by 
	 * the user
	 */
	private void initContents() {
		if(workspaceDirPath == null){
			setPageComplete(false);
			return;
		}
		
		//if there is a workbench selected check the "Use default location" button
		//and make the selection inactive
		linkButton.setSelection(true);
		locationPathField.setText(workspaceDirPath.toString());
		locationPathField.setEnabled(false);
		locationLabel.setEnabled(false);
		browseButton.setEnabled(false);
		
		updatePageComplete();
		
	}

	protected void browseForProjectLocation() {
		//gets the destination introduced in the locationPathField field
		IPath destination = getDestination();
		
		//opens the browse dialog and returns the chosen directory
		IPath path = browse(destination, false);
		
		if(path == null){
			updatePageComplete();
			return;
		}
		
		locationPathField.setText(path.toString());
		updatePageComplete();
	}

	private IPath browse(IPath destination, boolean mustExist) {
		int openSave;
		
		if(mustExist)
			openSave = SWT.OPEN;
		else
			openSave = SWT.SAVE;
		
		Shell sh = getShell();
		//create the browse page
		DirectoryDialog dialog = new DirectoryDialog(sh, openSave);
		
		//set the path that the dialog will use to filter the directories
		dialog.setMessage("Choose a directory for the project contents");
		if(destination != null){			
				dialog.setFilterPath(destination.toString());			
		}
		
		String result = dialog.open();
		if(result == null)
			return null;
		
		return new Path(result);
	}

	/**
	 * 
	 * @return
	 */
	private IPath getDestination() {
		String text = locationPathField.getText().trim();
		if(text.length() == 0)
			return null;
		
		IPath path = new Path(text);
		if( !path.isAbsolute() )
			path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		
		return path;
	}

	/**
	 * 
	 */
	protected void updatePageComplete() {		
		
		setPageComplete(false);
		String projectName = projectNameField.getText().trim();
		if(projectName.equals("")){
			setMessage(null);
			setErrorMessage(null);
			return;
		}
		
		String destinationFolder = locationPathField.getText().trim();
		if(destinationFolder.equals("")){
			setMessage(null);
			setErrorMessage(null);
			return;
		}
		
		if(projectName.contains(" ")){
			setMessage(null);
			setErrorMessage("There can not be spaces in a project name");
			return;
		}
		
		if(destinationFolder.contains(" ")){
			setMessage(null);
			setErrorMessage("There can not be spaces in a project path");
			return;
		}
		
		String fullProjectPath = destinationFolder+"/"+projectName;
		File newPrj = new File(fullProjectPath);
		if(newPrj.exists()){
			setMessage(null);
			setErrorMessage("The project already exists");
			return;
		}
		
		//let's check if the name has illegal chars
		//But don't forget keywords, like trying to create a file called COM, COM1, COM2, PRN
		for(int index = 0; index < projectName.length(); index++){
			for(int illindex = 0; illindex< ILLEGAL_CHARACTERS.length; illindex++ )
				if(projectName.charAt(index) == ILLEGAL_CHARACTERS[illindex]){
					setMessage(null);
					setErrorMessage("Illegal character in project name");
					return;
				}
		}
		
		for(int index = 0; index < destinationFolder.length(); index++){
			for(int illindex = 0; illindex< ILLEGAL_CHARACTERS_2.length; illindex++ )
				if(destinationFolder.charAt(index) == ILLEGAL_CHARACTERS_2[illindex]){
					setMessage(null);
					setErrorMessage("Illegal character in project path");
					return;
				}
		}
		
		setPageComplete(true);
		setMessage(null);
		setErrorMessage(null);
	}

	/**
	 * Finds the workspace location so that the
	 * initContent method can use it in the wizard page
	 * 
	 * @param intialSelection
	 */
	public void init(IStructuredSelection intialSelection) {
		//get object which represents the workspace  
		IWorkspace workspace = ResourcesPlugin.getWorkspace();  
		  
		//get location of workspace (java.io.File)  
		File workspaceDirectory = workspace.getRoot().getLocation().toFile();
		
		//get its absolute path
		workspaceDirPath  = workspaceDirectory.getAbsolutePath();		
		
	}

	/**
	 * Returns the values introduced by the user
	 * @return
	 */
	public static String[] getUserInput() {
		String[] userInput= new String[2];
		userInput[0] = projectNameField.getText().trim(); 
		userInput[1] = locationPathField.getText().trim();
		
		return userInput;
	}
	

}
