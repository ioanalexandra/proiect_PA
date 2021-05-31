package main.window;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.graph.Graf;
import main.graph.Muchie;
import main.graph.Nod;

public class GraphGenerator extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6000992113362564675L;
	private JFrame frame;
	private Graf graph;
	private int nrNoduriDesenate;
	private int limita;
	private Nod subarboreDrept = null;

	public GraphGenerator(JFrame frame) {
		this.frame = frame;
		this.graph = new Graf();
		this.setLayout(new BorderLayout());
		nrNoduriDesenate = 5;
		Nod root = new Nod(Integer.toString(nrNoduriDesenate));
		root.setPosition(500, 50);
		int initial = nrNoduriDesenate;
		graph.addNode(root);
		limita = 1;
		deseneazaGraf(root, 460, 80, -40, 30);
		limita = -initial;
		subarboreDrept.setPosition(590, 80);
		deseneazaGraf(subarboreDrept, 560, 100, -30, 20);
	}

	private void deseneazaGraf(Nod n, double x, double y, double offX, double offY) {
		nrNoduriDesenate--;
		Nod stg = new Nod(Integer.toString(nrNoduriDesenate));
		if (nrNoduriDesenate < limita) {
			return;
		} else {
			if (nrNoduriDesenate - 1 >= limita - 1) {
				stg.setPosition(x, y);
				graph.addNode(stg);
				Muchie m1 = new Muchie("", n, stg);
				graph.addEdge(m1);
			}
			nrNoduriDesenate--;
			if (nrNoduriDesenate - 1 >= limita - 1) {
				Nod dr = new Nod(Integer.toString(nrNoduriDesenate));
				dr.setPosition(x + 60, y);
				graph.addNode(dr);
				Muchie m2 = new Muchie("", n, dr);
				graph.addEdge(m2);
				if (subarboreDrept == null)
					subarboreDrept = dr;
			}
			deseneazaGraf(stg, x + offX, y + offY, offX, offY);

		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		graph.draw(g);
		frame.repaint();
		frame.revalidate();
	}

	public void clearGraph() {
		graph.clear();
	}

	public void destroy() {
		clearGraph();
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public Graf getGraph() {
		return graph;
	}

	public void setGraph(Graf graph) {
		this.graph = graph;
	}

}
