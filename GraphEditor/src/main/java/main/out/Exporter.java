package main.out;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.apache.batik.svggen.SVGGraphics2D;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import main.window.GraphGenerator;
import main.window.GraphPanel;

public class Exporter {

	private final JFileChooser fc;

	public Exporter() {
		fc = new JFileChooser();

		fc.addChoosableFileFilter(new FileFilter() {
			public String getDescription() {
				return "PDF Documents (*.pdf)";
			}

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".pdf");
				}
			}
		});
		fc.addChoosableFileFilter(new FileFilter() {
			public String getDescription() {
				return "SVG Files (*.svg)";
			}

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".svg");
				}
			}
		});

	}

	public void export(SVGGraphics2D svg, JPanel dp) {
		String path = getFileChooserPath(".svg");
		if (path == null)
			return;
		if (dp instanceof GraphPanel) {
			((GraphPanel) dp).disableToolbar();
			dp.remove(((GraphPanel) dp).getToolbar());
		}
		try {
			OutputStreamWriter char_output = new OutputStreamWriter(new FileOutputStream(path),
					Charset.forName("UTF-8").newEncoder());
			svg.setSVGCanvasSize(dp.getSize());
			dp.printAll(svg);
			svg.stream(char_output, true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (dp instanceof GraphPanel)
			dp.add(((GraphPanel) dp).getToolbar(), BorderLayout.WEST);

	}

	public void export(JPanel dp) {

		String path = getFileChooserPath(".pdf");
		if (path == null)
			return;
		if (dp instanceof GraphPanel) {
			((GraphPanel) dp).disableToolbar();
			dp.remove(((GraphPanel) dp).getToolbar());
		}
		BufferedImage image = new BufferedImage(dp.getWidth(), dp.getHeight(), BufferedImage.TYPE_INT_RGB);
		dp.paint(image.getGraphics());
		try {
			Document d = new Document();
			PdfWriter writer = PdfWriter.getInstance(d, new FileOutputStream(path));
			d.open();

			Image iTextImage = Image.getInstance(writer, image, 1);
			iTextImage.setAbsolutePosition(0, 0);
			iTextImage.scaleToFit(d.getPageSize());
			d.add(iTextImage);

			d.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (dp instanceof GraphPanel)
			dp.add(((GraphPanel) dp).getToolbar(), BorderLayout.WEST);
	}

	public void exportPNG(JPanel dp) {
		String path = getFileChooserPath(".png");
		if (path == null)
			return;
		if (dp instanceof GraphPanel) {
			((GraphPanel) dp).disableToolbar();
			dp.remove(((GraphPanel) dp).getToolbar());
		}

		BufferedImage image = new BufferedImage(dp.getWidth(), dp.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		dp.printAll(g);
		g.dispose();
		try {
			ImageIO.write(image, "png", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (dp instanceof GraphPanel)
			dp.add(((GraphPanel) dp).getToolbar(), BorderLayout.WEST);
	}

	private String getFileChooserPath(String extension) {// deschide file chooser-ul si returneaza path-ul in functie de
															// ce am selectat
		// o folosim la toate exporturile
		String path;
		int result = fc.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			path = fc.getSelectedFile().getAbsolutePath();
		} else
			return null;
		path += (path.endsWith(extension)) ? "" : extension;
		return path;
	}

}
