package com.chrisithomas.clothtest.cloth;

import org.lwjgl.opengl.GL11;

public class RigidConstraint extends Constraint {
	private Node a, b;
	private float dist, rigidity, strength;
	
	public RigidConstraint(Node a, Node b, float rigidity, float strength) {
		this.a = a;
		this.b = b;
		this.dist = Point.distance(a.current, b.current);
		this.rigidity = rigidity;
		this.strength = strength;
	}
	
	public RigidConstraint(Node a, Node b, float rigidity) {
		this(a, b, rigidity, 1.1f);
	}
	
	public RigidConstraint(Node a, Node b) {
		this(a, b, 0.5f);
	}
	
	@Override
	public void solve() {
		Point delta = Point.subtract(b.current, a.current);
		float len = delta.length();
		float diff = (len - dist) / len;
		delta.multiply(diff * rigidity);
		a.current.add(delta);
		b.current.subtract(delta);
	}
	
	@Override
	public void render() {
		float strain = Point.distance(a.current, b.current) / dist - 1;
		if (strain < 0) strain = 0;
		if (strain > 1) strain = 1;
		GL11.glColor3f(1, 1 - strain, strain);
		GL11.glVertex2f(a.current.x, a.current.y);
		GL11.glVertex2f(b.current.x, b.current.y);
	}
	
	@Override
	public boolean shouldBreak() {
		return Point.distance(a.current, b.current) > dist / rigidity * strength;
	}
	
	@Override
	public boolean contains(Node n) {
		return a == n || b == n;
	}
}
