/**
 * 
 */
package rsleditor.problems;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

/**
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class PatternMatchListenerDelegate implements
		IPatternMatchListenerDelegate {

	private TextConsole tconsole;
	
	private static ProblemsView problemView = ProblemsView.getInstance();  
	
	private static final Pattern ERROR_PATTERN = Pattern.compile("(.*):(\\d+):(\\d+):(\\s*)(.*)");
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.console.IPatternMatchListenerDelegate#connect(org.eclipse.ui.console.TextConsole)
	 */
	@Override
	public void connect(TextConsole console) {
		tconsole = console;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.console.IPatternMatchListenerDelegate#disconnect()
	 */
	@Override
	public void disconnect() {
		tconsole = null;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.console.IPatternMatchListenerDelegate#matchFound(org.eclipse.ui.console.PatternMatchEvent)
	 */
	/**
	 * Pattern
	 * D:/eRAISE/src/eRAISE/runtime-EclipseApplication/Test/src/SET_DATABASE.rsl:4:12:   parse error
	 * ----------------------------------------1-------------------------------- 2--3--4------5------
	 * (.*):(\d+):(\d+):(\s*)(.*)
	 */
	@Override
	public void matchFound(PatternMatchEvent event) {
		//System.out.println("Match found");
		String lineStr = null;
		int offset = event.getOffset();
		int length = event.getLength();
		//System.out.println("   "+offset+" "+length);
		
		IDocument doc = tconsole.getDocument();
		
		try {
			lineStr = doc.get(offset, length);
		} catch (BadLocationException e) {
			System.out.println("Error when reading Console content "+e.getMessage());
			e.printStackTrace();
		}
		
		//extract error messages from Console		
		Matcher matcher = ERROR_PATTERN.matcher(lineStr);
		String fileName; 
		int line;
		int column;
		String err_message;

		if (matcher.matches()) {
			
			fileName = matcher.group(1);
		    line = Integer.parseInt(matcher.group(2));
		    column = Integer.parseInt(matcher.group(3));
		    err_message = matcher.group(5);
		    //System.out.println(fileName+ " "+ line+ " "+ column+ " "+ err_message);
		} 
		else {
		    return;
		}

		//create a Problem and send it to ProblemsView to display it
		Problem prb = new Problem(2, err_message, line, column, column+4);
	
		IWorkspace workspace= ResourcesPlugin.getWorkspace();    
		IPath path= Path.fromOSString(fileName); 
		IFile ifile= workspace.getRoot().getFileForLocation(path);
		
		problemView.printProblems(ifile, prb);
	}

}
