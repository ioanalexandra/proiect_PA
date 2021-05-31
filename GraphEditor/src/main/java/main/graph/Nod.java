package main.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Nod extends GrafAbstract {

	private double x, y, centerX;
	private int radius, lgText;// radius=raza cercului
	private String id;

	public void setId(String id) {
		this.id = id;// textul din nod
	}

	private Ellipse2D ellipse;
	private Color nodeColor;

	public Nod(String id) {
		this.id = id;
		x = y = 0;
		nodeColor = new Color(138, 209, 189);
		radius = 25;
	}

	public Nod(String id, double x, double y, int radius) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.radius = radius;
		nodeColor = new Color(138, 209, 189);

	}

	@Override
	public void draw(Graphics g) {
		lgText = g.getFontMetrics().stringWidth(id);
		centerX = (x + x + Math.max(radius, lgText) - radius) / 2;// centrul elipsei
		ellipse = new Ellipse2D.Double(x - radius / 2, y - radius / 2, Math.max(radius, lgText), radius);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		Color c = g2.getColor();
		g2.setPaint(nodeColor);
		g2.fill(ellipse);
		g2.setPaint(Color.black);
		g2.setStroke(new BasicStroke(2));
		g2.draw(ellipse);
		g2.setColor(c);
		g2.drawString(id, (int) centerX - lgText / 2, (int) y);
	}

	@Override
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getPosX() {
		return x;
	}

	public double getPosY() {
		return y;
	}

	public boolean collides(double x, double y) {
		double a = Math.max(radius, lgText) / 2;
		double b = radius / 2;
		return ((Math.pow((x - centerX), 2) / Math.pow(a, 2)) + (Math.pow((y - this.y), 2) / Math.pow(b, 2))) <= 1;// intersectia
																													// unui
																													// pct
																													// cu
																													// o
																													// elipsa
	}

	public String getId() {
		return id;
	}

	public double getCenterX() {
		return centerX;
	}

	public int getLgText() {
		return lgText;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Color getNodeColor() {
		return nodeColor;
	}

	public void setNodeColor(Color nodeColor) {
		this.nodeColor = nodeColor;
	}

}
