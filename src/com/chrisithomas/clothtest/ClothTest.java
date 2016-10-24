package com.chrisithomas.clothtest;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.chrisithomas.clothtest.cloth.Cloth;
import com.chrisithomas.clothtest.cloth.Node;
import com.chrisithomas.clothtest.cloth.RigidConstraint;
import com.chrisithomas.clothtest.window.KeyListener;
import com.chrisithomas.clothtest.window.Window;

public class ClothTest implements KeyListener {
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
		new ClothTest().run();
	}
	
	private Window window;
	private Cloth cloth;
	private int mode = 0;
	
	public void run() {
		window = new Window("Cloth Test");
		try {
			window.create();
			
			updateMode();
			window.addKeyListener(this);
			
			while (window.isOpen()) {
				window.processInput();
				
				cloth.update(0.1f);
				
				window.preRender(); 
				window.set2D();
				
				GL11.glPointSize(3f);
				GL11.glLineWidth(2f);
				GL11.glColor3f(1, 1, 1);
				cloth.render();
				
				window.postRender();
				window.finishRendering();
			}
		} finally {
			window.finalize();
		}
	}

	@Override
	public void keyEvent(Key key) {
		if (key.isReleased()) {
			int oldMode = mode;
			if (key.key() == GLFW.GLFW_KEY_LEFT) {
				mode--;
				if (mode < 0) mode = 1;
			} else if (key.key() == GLFW.GLFW_KEY_RIGHT) {
				mode++;
				if (mode > 1) mode = 0;
			}
			if (mode != oldMode) updateMode();
		}
	}
	
	private void updateMode() {
		int width = 40;
		int height = 20;
		if (cloth != null) window.removeMouseListener(cloth);
		cloth = new Cloth(50, 50, width, height, 15f);
		window.addMouseListener(cloth);
		if (mode == 1) {
			int w = width + 1;
			for (int x = 0; x <= width; x++) {
				for (int y = 0; y <= height; y++) {
					if (x < width && y < height) {
						Node a = cloth.nodes.get(x     + y * w);
						Node b = cloth.nodes.get(x + 1 + y * w + w);
						cloth.constraints.add(new RigidConstraint(a, b, 0.75f, 2f));
						a = cloth.nodes.get(x     + y * w + w);
						b = cloth.nodes.get(x + 1 + y * w);
						cloth.constraints.add(new RigidConstraint(a, b, 0.75f, 2f));
					}
				}
			}
		}
	}
}
