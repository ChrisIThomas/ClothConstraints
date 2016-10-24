package com.chrisithomas.clothtest.cloth;

public class Point {
	public float x, y;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Point add(Point p) {
		this.x += p.x;
		this.y += p.y;
		return this;
	}
	
	public Point subtract(Point p) {
		this.x -= p.x;
		this.y -= p.y;
		return this;
	}
	
	public Point multiply(float f) {
		this.x *= f;
		this.y *= f;
		return this;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public static Point add(Point a, Point b) {
		return new Point(a.x + b.x, a.y + b.y);
	}
	
	public static Point subtract(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}
	
	public static Point multiply(Point p, float f) {
		return new Point(p.x * f, p.y * f);
	}
	
	public static float distance(Point a, Point b) {
		float x = a.x - b.x;
		float y = a.y - b.y;
		return (float) Math.sqrt(x * x + y * y);
	}
}
