package com.chrisithomas.clothtest.cloth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.chrisithomas.clothtest.window.MouseListener;

public class Cloth implements MouseListener {
	public List<Node> nodes = new ArrayList<Node>();
	public List<Constraint> constraints = new ArrayList<Constraint>();
	public List<StaticConstraint> staticConstraints = new ArrayList<StaticConstraint>();
	
	public Cloth(int ox, int oy, int width, int height, float spacing) {
		for (int y = 0; y <= height; y++) {
			for (int x = 0; x <= width; x++) {
				nodes.add(new Node(ox + x * spacing, oy + y * spacing));
			}
		}
		int w = width + 1;
		for (int x = 0; x <= width; x++) {
			for (int y = 0; y <= height; y++) {
				if (x < width) {
					Node a = nodes.get(x     + y * w);
					Node b = nodes.get(x + 1 + y * w);
					constraints.add(new RigidConstraint(a, b, 0.5f));
				}
				if (y < height) {
					Node a = nodes.get(x + y * w);
					Node b = nodes.get(x + y * w + w);
					constraints.add(new RigidConstraint(a, b, 0.5f, 2));
				}
			}
		}
		staticConstraints.add(new StaticConstraint(nodes.get(0)));
		staticConstraints.add(new StaticConstraint(nodes.get(width)));
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			staticConstraints.add(new StaticConstraint(nodes.get(r.nextInt(width))));
		}
	}
	
	public void update(float dt) {
		for (Node n : nodes) {
			n.update(dt);
		}
		for (int i = 0; i < 10; i++) {
			for (Node n : nodes) {
				if (n.current.y > 580) n.current.y = 580;
			}
			for (Constraint c : constraints) {
				c.solve();
			}
			for (Constraint c : staticConstraints) {
				c.solve();
			}
		}
		for (int i = 0; i < constraints.size(); i++) {
			Constraint c = constraints.get(i);
			if (c.shouldBreak()) {
				constraints.remove(i);
				i--;
			}
		}
	}
	
	public void render() {
		GL11.glBegin(GL11.GL_LINES);
		for (Constraint c : constraints) {
			c.render();
		}
		GL11.glEnd();
		GL11.glColor3f(0.5f, 0, 0.5f);
		GL11.glBegin(GL11.GL_POINTS);
		for (Node n : nodes) {
			GL11.glVertex2f(n.current.x, n.current.y);
		}
		GL11.glEnd();
	}
	
	private void removeNode(Node n) {
		for (int i = 0; i < constraints.size(); i++) {
			if (constraints.get(i).contains(n)) {
				constraints.remove(i);
				i--;
			}
		}
		for (int i = 0; i < staticConstraints.size(); i++) {
			if (staticConstraints.get(i).contains(n)) {
				staticConstraints.remove(i);
				i--;
			}
		}
	}
	
	private void removeNodesNear(int x, int y) {
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			float dx = n.current.x - x;
			float dy = n.current.y - y;
			float d = (float) Math.sqrt(dx * dx + dy * dy);
			if (d <= 8) {
				nodes.remove(i);
				removeNode(n);
				i--;
			}
		}
	}
	
	@Override
	public void mouseClickEvent(int button, int x, int y) {
		removeNodesNear(x, y);
	}
	
	@Override
	public void mouseDragEvent(int button, int x, int y) {
		removeNodesNear(x, y);
	}
}
