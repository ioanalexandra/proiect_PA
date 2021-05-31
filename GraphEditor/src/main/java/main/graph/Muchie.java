package main.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

public class Muchie extends GrafAbstract {

	private Nod source, destination;
	private int outline;
	private double x1, y1, x2, y2;
	private boolean arrow;// daca e orientat sau nu

	public Muchie(String id, Nod source, Nod destination) {
		this.source = source;
		this.destination = destination;
		x1 = source.getPosX();
		y1 = source.getPosY();
		x2 = destination.getPosX();
		y2 = destination.getPosY();
		outline = 2;
		arrow = false;
	}

	public void draw(Graphics g) {
		update();
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(outline));
		Color c = g.getColor();
		g2.setPaint(Color.black);
		if (arrow && source.getId().equals(destination.getId())) {
			g2.drawArc((int) x1, (int) y1, 30, 70, 90, 360);
			drawArrow(g, (int) x1, (int) y1 + source.getRadius() / 2 + 1, (int) x1, (int) y1 + source.getRadius() / 2);
		} else {
			g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
			if (arrow)
				drawArrowHead(g);
			g.setColor(c);
		}

	}

	private void drawArrowHead(Graphics g) {
		double a = Math.max(destination.getRadius(), destination.getLgText()) / 2;
		double b = destination.getRadius() / 2;

		double X = destination.getCenterX();
		double Y = destination.getPosY();

		Point p = EllipseIntersectLine(g, (float) a, (float) b, (float) X, (float) Y, (float) x1, (float) y1,
				(float) x2, (float) y2);
		if (p != null) {
			drawArrow(g, (int) x1, (int) y1, (int) p.x, (int) p.y);
		}
	}

	@Override
	public void setPosition(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getPosX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getPosY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean collides(double x, double y) {
		if (x < Math.min(x1, x2) || x > Math.max(x1, x2)) {
			return false;
		}
		double a = y2 - y1;
		double b = x1 - x2;
		double c = y1 * x2 - x1 * y2;
		double dist = Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);
		return dist <= outline;
	}

	public void update() {
		x1 = source.getCenterX();
		x2 = destination.getCenterX();
		y1 = source.getPosY();
		y2 = destination.getPosY();
	}

	public String getSourceId() {
		return source.getId();
	}

	public String getDestinationId() {
		return destination.getId();
	}

	private Point EllipseIntersectLine(Graphics g, float a, float b, float h, float k, float x1, float y1, float x2,
			float y2) {
		float aa, bb, cc, m;

		float xi1 = 0;
		float xi2 = 0;
		float yi1 = 0;
		float yi2 = 0;

		m = (y2 - y1) / (x2 - x1);

		if (x1 != x2) {
			float c = y1 - m * x1;
//
			aa = b * b + a * a * m * m;
			bb = 2 * a * a * c * m - 2 * a * a * k * m - 2 * h * b * b;
			cc = b * b * h * h + a * a * c * c - 2 * a * a * k * c + a * a * k * k - a * a * b * b;
		} else {

			aa = a * a;
			bb = -2.0f * k * a * a;
			cc = -a * a * b * b + b * b * (x1 - h) * (x1 - h);
		}

		float d = bb * bb - 4 * aa * cc;
//
		Point p = new Point();
		Point p2 = new Point();
		Point o = new Point();
		o.x = (int) x1;
		o.y = (int) y1;
		if (d > 0.0) {
			if (x1 != x2) {
				xi1 = (float) ((-bb + Math.sqrt(d)) / (2 * aa));
				xi2 = (float) ((-bb - Math.sqrt(d)) / (2 * aa));
				yi1 = y1 + m * (xi1 - x1);
				yi2 = y1 + m * (xi2 - x1);
			} else {
				yi1 = (float) ((-bb + Math.sqrt(d)) / (2 * aa));
				yi2 = (float) ((-bb - Math.sqrt(d)) / (2 * aa));
				xi1 = x1;
				xi2 = x1;
			}

			p.x = (int) xi1;
			p.y = (int) yi1;

			p2.x = (int) xi2;
			p2.y = (int) yi2;

		} else {
			p.x = (int) h;
			p.y = (int) (k - b);
			p2.x = (int) h;
			p2.y = (int) (k + b);
		}
		if (xi1 - xi2 == 0) {
			p.x = (int) h;
			p.y = (int) (k - b);
			p2.x = (int) h;
			p2.y = (int) (k + b);
		}
		if (o.distance(p) < o.distance(p2)) {
			return p;

		} else
			return p2;
	}

	void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		Graphics2D g = (Graphics2D) g1.create();
		// desenez o linie cu un triunghi la capat si o rotesc
		final int ARR_SIZE = Math.max(12, outline * 2);
		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);
		g.drawLine(0, 0, len, 0);
		g.fillPolygon(new int[] { len, len - ARR_SIZE, len - ARR_SIZE, len }, new int[] { 0, -ARR_SIZE, ARR_SIZE, 0 },
				4);
	}

	public boolean isArrow() {
		return arrow;
	}

	public void setArrow(boolean arrow) {
		this.arrow = arrow;
	}

	public int getOutline() {
		return outline;
	}

	public void setOutline(int outline) {
		this.outline = outline;
	}

}
