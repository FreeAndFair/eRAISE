/**
 * 
 */
package rsleditor.editor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

/**
 * Scans the RSL source in search for key words, strings 
 * and a white spaces
 *  
 * @author Marieta V. Fasie
 * 	marietafasie at gmail dot com
 *
 */
public class RSLCodeScanner extends RuleBasedScanner {
	/**
	 * Stores the RSL keywords
	 */
	private static String[] keyWords = { "Bool", "Char", "Int", "Nat", "Real", "Text", "Unit", "abs", 
		"any", "as", "axiom", "card", "case", "channel", "chaos", "class", "do", "dom", "elems", 
		"else", "elseif", "end", "extend", "false", "for", "forall", "hd", "hide", "if", "in",
		"inds", "initialise", "int", "len", "let", "local", "object", "of", "out", "post", "pre",
		"read", "real", "rng", "scheme", "skip", "stop", "swap", "then", "ti", "true", "type",
		"test_case", "until", "use", "value", "variable", "while", "with", "write"};
	
	/**
	 * Stores the RSL word for some mathematical symbols
	 */
	private static String[] mathWords = { "all", "always", "exists", "inter", "is", 
		"isin", "union", "-inflist", "~isin","-list", "-set" };
	
	/**
	 * Constructor
	 * @param colorManager
	 */
	public RSLCodeScanner(ColorManager colorManager){
		
		IToken keyword = new Token(
				new TextAttribute(
					colorManager.getColor(IRSLSyntaxColors.PINK), null, SWT.BOLD));

		IToken mathword = new Token(
				new TextAttribute(
					colorManager.getColor(IRSLSyntaxColors.PINK), null, SWT.BOLD));

		IToken string = new Token(new TextAttribute(
				colorManager.getColor(IRSLSyntaxColors.GREEN)));
		
		IToken other= new Token
	          (new TextAttribute(colorManager.getColor(IRSLSyntaxColors.BLACK)));
	
		
		List<IRule> rules= new ArrayList<IRule>();
					
		WordRule wordRule= new WordRule(new RSLWordDetector(),other);
		for (int i= 0; i < keyWords.length; i++)
			wordRule.addWord(keyWords[i], keyword);
    
		rules.add(wordRule);
		
		WordRule mathWordRule= new WordRule(new MathWordDetector(),other);
		for (int i= 0; i < mathWords.length; i++)
			wordRule.addWord(mathWords[i], mathword);
		
		rules.add(mathWordRule);
    
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new RSLWhitespaceDetector()));
		
		//rule for detecting Strings
		rules.add(new MultiLineRule("\"", "\"", string));

		//rule for detecting Strings
		rules.add(new SingleLineRule("\'", "\'", string));
		
		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}


}
