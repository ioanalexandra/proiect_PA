package main.color;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class ColorChooser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6216662500963612607L;
	public JColorChooser jcc = null;

	public ColorChooser(JPanel jp) {
		jcc = new JColorChooser(jp.getForeground());
	    jcc.setPreviewPanel(new JPanel());
	    jcc.setColor(new Color(200, 100, 100));
	    AbstractColorChooserPanel[] ap= jcc.getChooserPanels();
	    for (int i = 0; i < ap.length; i++) {
	        String clsName = ap[i].getClass().getName();
	        if (clsName.equals("javax.swing.colorchooser.DefaultSwatchChooserPanel")) {
	        	jcc.removeChooserPanel(ap[i]);
	        } else if (clsName.equals("javax.swing.colorchooser.DefaultRGBChooserPanel")) {
	        	jcc.removeChooserPanel(ap[i]);
	        } else if (clsName.equals("javax.swing.colorchooser.DefaultHSBChooserPanel")) {
	        	jcc.removeChooserPanel(ap[i]);
	        }
	      }
		this.add(jcc);
	}
}