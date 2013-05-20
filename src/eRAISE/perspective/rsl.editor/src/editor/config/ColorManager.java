package editor.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Manages the conversion between RGB color representation
 * and swt.graphics.Color 
 * 
 */

public class ColorManager {

	protected Map<RGB, Color> colorTable = new HashMap<RGB, Color>(10);

	/**
	 * Creates swt.graphics.Color objects based on their
	 * RGB representation. The colors are stored in a table
	 * 
	 * @param rgb Color description is terms of Red, Green and Blue
	 * @return Return a swt.graphics.Color object 
	 */
	public Color getColor(RGB rgb) {
		
		Color color = (Color) colorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colorTable.put(rgb, color);
		}
		return color;
	}
	
	/**
	 * Destroys the table storing the colors
	 */
	public void dispose() {
		Iterator<Color> e = colorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
	}
}
