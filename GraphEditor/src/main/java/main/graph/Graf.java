package main.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Graf {

	private List<Nod> noduri;
	private List<Muchie> muchii;
	private boolean ordered = false;
	private Hashtable<NodePair, Muchie> config;
	private Hashtable<String, Nod> uniqueNodeIds;// verific ca id-urile sunt unice

	public Graf() {
		noduri = new CopyOnWriteArrayList<Nod>();
		muchii = new CopyOnWriteArrayList<Muchie>();
		config = new Hashtable<NodePair, Muchie>();
		uniqueNodeIds = new Hashtable<String, Nod>();
	}

	public boolean addNode(Nod n) {
		if (uniqueNodeIds.containsKey(n.getId())) {
			return false;
		}
		noduri.add(n);
		uniqueNodeIds.put(n.getId(), n);
		return true;
	}

	public void addEdge(Muchie e) {
		e.setArrow(ordered);
		muchii.add(e);

	}

	public boolean connect(Nod n1, Nod n2, Muchie e) {
		NodePair np = new NodePair();
		np.first = n1.getId();
		np.second = n2.getId();
		if (config.containsKey(np)) {
			return false;
		}
		config.put(np, e);
		return true;
	}

	public void draw(Graphics g) {
		for (Muchie e : muchii) {
			e.draw(g);
		}
		for (Nod n : noduri) {
			n.draw(g);
		}
	}

	public List<Nod> getNodes() {
		return noduri;
	}

	public List<Muchie> getEdges() {
		return muchii;
	}

	public void removeNode(Nod n) {
		noduri.remove(n);
		uniqueNodeIds.remove(n.getId());
	}

	private class NodePair {
		public String first;
		public String second;
	}

	public void deleteNode(Nod n) {
		Enumeration<NodePair> pairs = config.keys();
		while (pairs.hasMoreElements()) {
			NodePair p = pairs.nextElement();
			if (p.first.equals(n.getId()) || p.second.equals(n.getId())) {
				muchii.remove(config.get(p));
				config.remove(p);
			}
		}
		noduri.remove(n);
		uniqueNodeIds.remove(n.getId());
	}

	public void deleteEdge(Muchie e) {
		Enumeration<NodePair> pairs = config.keys();
		while (pairs.hasMoreElements()) {
			NodePair p = pairs.nextElement();
			boolean ok = false;
			if (p.first.equals(e.getSourceId()) && p.second.equals(e.getDestinationId())) {
				muchii.remove(e);
				config.remove(p);
				ok = true;
			}
			if (p.first.equals(e.getDestinationId()) && p.second.equals(e.getSourceId())) {
				muchii.remove(e);
				config.remove(p);
				ok = true;
			}
			if (ok)
				break;
		}
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		for (Muchie e : muchii) {
			e.setArrow(ordered);
		}
		this.ordered = ordered;
	}

	public void setNodesRadius(int radius) {
		for (Nod n : noduri)
			n.setRadius(radius);
	}

	public void setNodesColor(Color c) {
		for (Nod n : noduri)
			n.setNodeColor(c);
	}

	public void setEdgesOutline(int outline) {
		for (Muchie e : muchii)
			e.setOutline(outline);
	}

	public void clear() {
		muchii.clear();
		noduri.clear();
		config.clear();
		uniqueNodeIds.clear();
	}

	public boolean areConnected(Nod n1, Nod n2, boolean oriented) {
		NodePair np = new NodePair();
		if (!oriented) {
			NodePair np2 = new NodePair();
			np.first = n1.getId();
			np.second = n2.getId();
			Enumeration<NodePair> pairs = config.keys();
			while (pairs.hasMoreElements()) {
				NodePair p = pairs.nextElement();
				if ((p.first.equals(np.first) && p.second.equals(np.second))
						|| (p.first.equals(np.second) && p.second.equals(np.first))) {
					return true;
				}
			}
		} else {
			np.first = n1.getId();
			np.second = n2.getId();
			Enumeration<NodePair> pairs = config.keys();
			while (pairs.hasMoreElements()) {
				NodePair p = pairs.nextElement();
				if ((p.first.equals(np.first) && p.second.equals(np.second))) {
					return true;
				}
			}
		}
		return false;
	}

	public Muchie getEdge(Nod n1, Nod n2) {
		NodePair np = new NodePair();
		np.first = n1.getId();
		np.second = n2.getId();
		Enumeration<NodePair> pairs = config.keys();
		while (pairs.hasMoreElements()) {
			NodePair p = pairs.nextElement();
			if ((p.first.equals(np.first) && p.second.equals(np.second))
					|| (p.second.equals(np.first) && p.first.equals(np.second))) {
				return config.get(p);
			}
		}
		return null;
	}

	public boolean exists(String id) {
		Enumeration<String> pairs = uniqueNodeIds.keys();
		while (pairs.hasMoreElements()) {
			System.out.println(pairs.nextElement());
		}
		return uniqueNodeIds.containsKey(id);
	}

	public void modificaId(Nod n, String id) {
		uniqueNodeIds.remove(n.getId());
		Enumeration<NodePair> pairs = config.keys();
		while (pairs.hasMoreElements()) {
			NodePair p = pairs.nextElement();
			if (p.first.equals(n.getId())) {
				p.first = id;
			}
			if (p.second.equals(n.getId())) {
				p.second = id;
			}
		}
		n.setId(id);
		uniqueNodeIds.put(id, n);
	}
}
