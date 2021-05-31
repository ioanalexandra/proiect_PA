package main.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import main.color.ColorChooser;
import main.graph.Graf;
import main.graph.GrafAbstract;
import main.graph.Muchie;
import main.graph.Nod;
import main.out.Exporter;

public class GraphPanel extends JPanel {

	private static final long serialVersionUID = 4412645150465651052L;
	private JCheckBox digraf;
	private JComboBox optiuniSalvare;
	private JButton salveaza;
	private JButton nod, muchie, edit, sterge, parcurgereBtn;
	private JComboBox parcurgere;
	private JPanel toolbar;
	private Graf graf;
	private JFrame frame;
	private TipDesen mode;
	private DrawPanelHandler mouseHandler;
	private JPanel nodEdit;
	private Nod nodMiscat;
	boolean[] apar = new boolean[100];
	private Nod parcNod = null;
	private int nrNoduri = 0;
	private Nod nodLaEditare;
	private Exporter exporter;
	private GraphPanel instantaPanel;
	// pentru libraria svg
	private DOMImplementation domImpl;
	private Document document;
	private SVGGraphics2D svgGenerator;

	private void initializeSVG() {
		domImpl = GenericDOMImplementation.getDOMImplementation();
		document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);
		svgGenerator = new SVGGraphics2D(document);
	}

	public JPanel getToolbar() {
		return toolbar;
	}

	public void setToolbar(JPanel toolbar) {
		this.toolbar = toolbar;
	}

	private void initEdit() {

		JTextField nodText = new JFormattedTextField();
		nodText.setColumns(10);

		nodText.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!graf.exists(e.getActionCommand())) {
					graf.modificaId(nodLaEditare, e.getActionCommand());
					((JTextField) e.getSource()).setText("");
				}
			}

		});

		JButton ascunde = new JButton("Ascunde");

		ascunde.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				disableToolbar();
			}
		});
		nodEdit = new JPanel();

		final ColorChooser jNode = new ColorChooser(nodEdit);
		jNode.jcc.getSelectionModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Color c = jNode.jcc.getColor();
				graf.setNodesColor(c);
			}

		});
		nodEdit.setPreferredSize(new Dimension(210, 100));
		nodEdit.setLayout(new FlowLayout(FlowLayout.LEFT));
		nodEdit.add(ascunde);
		nodEdit.add(nodText);
		nodEdit.add(jNode);
	}

	public void dfs(Nod n, boolean oriented, boolean coloring) {

		for (int i = 0; i < graf.getNodes().size(); i++) {
			if (!graf.getNodes().get(i).getId().equals(n)) {
				if (graf.areConnected(n, graf.getNodes().get(i), oriented)) {
					if (!apar[i]) {

						graf.draw(getGraphics());
						frame.repaint();
						frame.revalidate();
						apar[i] = true;
						if (coloring) {
							double acm = System.currentTimeMillis();
							double dupa = System.currentTimeMillis();
							do {
								dupa = System.currentTimeMillis();
							} while (dupa - acm < 1000);
							n.setNodeColor(Color.red);
							graf.getNodes().get(i).setNodeColor(Color.red);

						} else {
							// System.out.println(mucColor[i].toString());
							// Muchie e = graf.getEdge(graf.getNodes().get(i), n);
							n.setNodeColor(new Color(138, 209, 189));
							graf.getNodes().get(i).setNodeColor(new Color(138, 209, 189));
						}
						dfs(graf.getNodes().get(i), oriented, coloring);
					}
				}
			}
		}
	}

	public void bfs(Nod n, int nodeIndex, boolean coloring) {
		Arrays.fill(apar, false);
		int[] coada = new int[100];
		Arrays.fill(coada, 0);
		int ic = 0;
		int sf = 0;
		System.out.println(apar);

		System.out.println(n.getId());

		apar[nodeIndex] = true;
		coada[0] = nodeIndex;

		System.out.println(coada[ic]);

		while (ic <= sf) {
			Nod X = graf.getNodes().get(coada[ic]);
			ic++;
			for (int i = 0; i < graf.getNodes().size(); i++) {
				if (!graf.getNodes().get(i).getId().equals(X.getId())) {
					if (graf.areConnected(X, graf.getNodes().get(i), graf.isOrdered())) {
						if (!apar[i]) {
							graf.draw(getGraphics());
							frame.repaint();
							frame.revalidate();

							apar[i] = true;
							if (coloring) {
								double acm = System.currentTimeMillis();
								double dupa = System.currentTimeMillis();
								do {
									dupa = System.currentTimeMillis();
								} while (dupa - acm < 1500);

								n.setNodeColor(Color.red);
								graf.getNodes().get(i).setNodeColor(Color.red);
							} else {
								n.setNodeColor(new Color(138, 209, 189));
								graf.getNodes().get(i).setNodeColor(new Color(138, 209, 189));
							}
							coada[++sf] = i;
						}
					}
				}
			}
		}
		Arrays.fill(apar, false);
	}

	public GraphPanel(JFrame frame) {
		initializeSVG();
		exporter = new Exporter();
		ButtonActions ba = new ButtonActions();
		this.frame = frame;
		graf = new Graf();// initializare graf
		initEdit();// initializeaza bara de editare
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
				boolean selected = abstractButton.getModel().isSelected();
				graf.setOrdered(selected);
			}
		};

		toolbar = new JPanel();
		toolbar.setLayout(new GridLayout(9, 1));
		toolbar.setSize(50, 50);
		JCheckBox digraf = new JCheckBox("Este ordonat?");
		digraf.setBackground(new Color(138, 209, 189));
		digraf.addActionListener(actionListener);
		optiuniSalvare = new JComboBox();
		optiuniSalvare.addItem("Salvează ca PNG");
		optiuniSalvare.addItem("Salvează ca SVG");
		optiuniSalvare.addItem("Salvează ca PDF");
		optiuniSalvare.setBackground(new Color(138, 209, 189));
		salveaza = new JButton("Salvează");
		salveaza.setBackground(new Color(138, 209, 189));
		instantaPanel = this;
		salveaza.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {// cand apas pe salveaza exporteaza ini fctie de tipul ales
				if (optiuniSalvare.getSelectedItem().toString().endsWith("PNG")) {
					exporter.exportPNG(instantaPanel);
				} else if (optiuniSalvare.getSelectedItem().toString().endsWith("SVG")) {
					exporter.export(svgGenerator, instantaPanel);
				} else {
					exporter.export(instantaPanel);
				}
			}

		});
		parcurgere = new JComboBox();
		parcurgere.setBackground(new Color(138, 209, 189));
		parcurgere.addItem("BFS");
		parcurgere.addItem("DFS");
		parcurgereBtn = new JButton("Parcurge");
		parcurgereBtn.setBackground(new Color(138, 209, 189));
		sterge = new JButton("Șterge");
		sterge.setBackground(new Color(138, 209, 189));
		nod = new JButton("+ nod");
		nod.setBackground(new Color(138, 209, 189));
		muchie = new JButton("+ muchie");
		muchie.setBackground(new Color(138, 209, 189));
		edit = new JButton("Editează");
		edit.setBackground(new Color(138, 209, 189));
		sterge.addActionListener(ba);
		nod.addActionListener(ba);
		muchie.addActionListener(ba);
		edit.addActionListener(ba);
		parcurgereBtn.addActionListener(ba);
		toolbar.setBackground(new Color(138, 209, 189));
		// panel care contine toate butoanele
		toolbar.add(nod);
		toolbar.add(muchie);
		toolbar.add(digraf);
		toolbar.add(edit);
		toolbar.add(sterge);
		toolbar.add(parcurgere);
		toolbar.add(parcurgereBtn);
		toolbar.add(optiuniSalvare);
		toolbar.add(salveaza);
		toolbar.setAlignmentX(Component.CENTER_ALIGNMENT);
		mode = TipDesen.INIT;//
		mouseHandler = new DrawPanelHandler();

		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				if (mode == TipDesen.MUTARE) {
					nodMiscat.setPosition(e.getPoint().getX(), e.getPoint().getY());
					setCursor(new Cursor(Cursor.HAND_CURSOR));// cand mut se pune manuta
				}
			}

			public void mouseMoved(MouseEvent e) {
			}

		});

		this.setLayout(new BorderLayout());
		this.addMouseListener(new DrawPanelHandler());
		this.add(toolbar, BorderLayout.WEST);
		this.setBackground(Color.white);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		graf.draw(g);
		frame.repaint();
		frame.revalidate();
	}

	public void disableToolbar() {
		frame.remove(nodEdit);
	}

	private class DrawPanelHandler implements MouseListener {

		private Nod edgeSource, edgeDest;

		public void mouseClicked(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			double msX = e.getPoint().getX();
			double msY = e.getPoint().getY();
			if (e.getButton() == MouseEvent.BUTTON1) {
				switch (mode) {
				case STERGERE:
					for (Nod el : graf.getNodes()) {
						if (el.collides(msX, msY)) {
							graf.deleteNode(el);
						}
					}
					for (Muchie el : graf.getEdges()) {
						if (el.collides(msX, msY)) {
							graf.deleteEdge(el);
						}
					}
					break;
				case MUCHIE1:
					for (GrafAbstract el : graf.getNodes()) {
						if (el.collides(msX, msY)) {
							edgeSource = (Nod) el;
							mode = TipDesen.MUCHIE2;
						}
					}
					break;
				case MUCHIE2:
					for (GrafAbstract el : graf.getNodes()) {
						if (el.collides(msX, msY)) {
							edgeDest = (Nod) el;
							if (!graf.isOrdered() && edgeSource.getId().equals(edgeDest.getId()))
								break;
							Muchie edge = new Muchie("", edgeSource, edgeDest);
							graf.addEdge(edge);
							graf.connect(edgeSource, edgeDest, edge);
							mode = TipDesen.MUCHIE1;
						}
					}
					break;
				case EDITARE:
					for (Nod el : graf.getNodes()) {
						if (el.collides(msX, msY)) {
							nodLaEditare = el;
							frame.add(nodEdit, BorderLayout.EAST);
							mode = TipDesen.INIT;
							break;
						}
					}
					break;
				case ALGORITM:
					int i = 0;
					for (Nod el : graf.getNodes()) {
						if (el.collides(msX, msY)) {
							parcNod = el;
							if (parcurgere.getSelectedItem().toString().equals("DFS")) {
								apar[i] = true;
								dfs(el, graf.isOrdered(), true);
								break;
							} else {
								apar[i] = true;
								bfs(el, i, true);
								break;
							}
						}
						i++;
					}
					parcurgereBtn.setText("OPREȘTE");
					mode = TipDesen.INIT;
					break;
				default:
					break;
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				for (Nod el : graf.getNodes()) {
					if (el.collides(msX, msY)) {
						nodMiscat = el;
						mode = TipDesen.MUTARE;
						break;
					}
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				double msX = e.getPoint().getX();
				double msY = e.getPoint().getY();
				switch (mode) {
				case NOD:
					Nod n = new Nod(Integer.toString(nrNoduri++), msX, msY, 60);
					graf.addNode(n);
					break;
				default:
					break;

				}

			} else if (e.getButton() == MouseEvent.BUTTON3) {
				switch (mode) {
				case MUTARE:
					mode = TipDesen.INIT;
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					break;
				default:
					break;

				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public void clearGraph() {
		graf.clear();
	}

	private class ButtonActions implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == nod) {
				disableToolbar();
				mode = TipDesen.NOD;
			} else if (e.getSource() == muchie) {
				disableToolbar();
				mode = TipDesen.MUCHIE1;
			} else if (e.getSource() == sterge) {
				disableToolbar();
				mode = TipDesen.STERGERE;
			} else if (e.getSource() == edit) {
				disableToolbar();
				mode = TipDesen.EDITARE;
			} else if (e.getSource() == parcurgereBtn) {
				if (parcurgereBtn.getText().equals("OPREȘTE")) {
					parcurgereBtn.setText("Rulează");
					Arrays.fill(apar, false);
					int i;
					for (i = 0; i < graf.getNodes().size(); ++i) {
						if (graf.getNodes().get(i).getId().equals(parcNod.getId())) {
							apar[i] = true;
							break;
						}
					}
					if (parcurgere.getSelectedItem().toString().equals("DFS")) {

						dfs(parcNod, graf.isOrdered(), false);
						Arrays.fill(apar, false);
					} else {
						bfs(parcNod, i, false);
					}
				} else {
					mode = TipDesen.ALGORITM;
				}
			}
		}

	}

	private enum TipDesen {
		NOD, MUCHIE1, MUCHIE2, STERGERE, INIT, MUTARE, EDITARE, ALGORITM,
	}

	public void setNrNoduri(int nr) {
		nrNoduri = nr;
	}

}