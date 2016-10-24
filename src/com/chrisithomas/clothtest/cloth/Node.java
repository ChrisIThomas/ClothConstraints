package com.chrisithomas.clothtest.cloth;

public class Node {
	private static Point GRAVITY = new Point(0, 9.8f);
	public Point current, previous;
	
	protected Node(float x, float y) {
		current = new Point(x, y);
		previous = new Point(x, y);
	}
	
	public void update(float dt) {
		Point acc = Point.multiply(GRAVITY, dt * dt);
		float x = current.x;
		float y = current.y;
		current.multiply(1.99f).subtract(previous.multiply(0.99f)).add(acc);
		previous.x = x;
		previous.y = y;
	}
}
