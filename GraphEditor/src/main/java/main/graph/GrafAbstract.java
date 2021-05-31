package main.graph;

import java.awt.Graphics;

import javax.swing.JPanel;

//contine orice tip de element al unui graf: nod, muchie sunt de tip GrafAbstract
public abstract class GrafAbstract extends JPanel {

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public abstract void draw(Graphics g);

	public abstract void setPosition(double x, double y);

	public abstract double getPosX();

	public abstract double getPosY();

	public abstract boolean collides(double x, double y);// daca un punct se intersecteaza cu un element
}
