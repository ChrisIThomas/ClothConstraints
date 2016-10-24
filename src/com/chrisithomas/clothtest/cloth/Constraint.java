package com.chrisithomas.clothtest.cloth;

public abstract class Constraint {
	public abstract void solve();
	public abstract void render();
	public abstract boolean shouldBreak();
	public abstract boolean contains(Node n);
}
