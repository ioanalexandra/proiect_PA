package main.window;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Window {
	private JFrame frame;// frame-ul cu cele 2 butoane

	private GraphPanel deseneazaPanel;// panel deseneaza graf
	private GraphGenerator generatorPanel;// genereaza graf
	private JFrame fereastraDesenat;
	private JFrame fereastraGenerare;
	private JButton deseneaza = new JButton("Desenează graf");
	private JButton genereaza = new JButton("Generează graf");

	public Window(String name, int width, int height) {
		fereastraGenerare = new JFrame("Generează un graf!");
		fereastraGenerare.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		fereastraGenerare.setSize(width, height);
		fereastraGenerare.setLocationRelativeTo(null);// centrare
		generatorPanel = new GraphGenerator(fereastraGenerare);
		fereastraGenerare.add(generatorPanel);
		fereastraDesenat = new JFrame("Desenează un graf");
		fereastraDesenat.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		fereastraDesenat.setSize(width, height);
		fereastraDesenat.setLocationRelativeTo(null);
		deseneazaPanel = new GraphPanel(fereastraDesenat);
		fereastraDesenat.add(deseneazaPanel);// adaug continutul ferestrei

		genereaza.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				fereastraGenerare.setVisible(true);
			}
		});

		deseneaza.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				fereastraDesenat.setVisible(true);
			}

		});
		fereastraDesenat.addWindowListener(new WindowListener() {

			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			// cand inchid fereastra de desenat sa se stearga graful
			public void windowClosing(WindowEvent e) {
				deseneazaPanel.clearGraph();
				deseneazaPanel.setNrNoduri(0);// setez nr de noduri inapoi pe 0
			}

			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

		});

		frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(deseneaza, BorderLayout.NORTH);// pozitia butoanelor
		frame.add(genereaza, BorderLayout.SOUTH);
		frame.setVisible(true);
		frame.pack();
	}

}
