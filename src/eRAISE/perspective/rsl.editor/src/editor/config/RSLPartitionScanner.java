package editor.config;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * Scans the RSL source code based on 
 * the rules specified in the constructor
 * 
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RSLPartitionScanner extends RuleBasedPartitionScanner {
	
	/**
	 * Partition name for the code representing a comment 
	 */
	public final static String RSL_COMMENT = "__rsl_comment";

	/**
	 * Constructor. Defines the rules for
	 * scanning the document.  
	 */
	public RSLPartitionScanner() {

		IToken rslComment = new Token(RSL_COMMENT);

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("/*", "*/", rslComment);
		rules[1] = new SingleLineRule("--", "", rslComment);

		setPredicateRules(rules);
	}
}
