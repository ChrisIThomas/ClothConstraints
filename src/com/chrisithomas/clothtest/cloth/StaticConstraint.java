package com.chrisithomas.clothtest.cloth;

public class StaticConstraint extends Constraint {
	private Node a;
	private Point p;
	
	public StaticConstraint(Node a) {
		this.a = a;
		this.p = new Point(a.current.x, a.current.y);
	}
	
	@Override
	public void solve() {
		a.current.x = p.x;
		a.current.y = p.y;
	}
	
	@Override
	public void render() {}
	
	@Override
	public boolean shouldBreak() {
		return false;
	}
	
	@Override
	public boolean contains(Node n) {
		return a == n;
	}
}
