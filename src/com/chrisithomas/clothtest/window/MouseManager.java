package com.chrisithomas.clothtest.window;

import org.lwjgl.glfw.GLFW;

public final class MouseManager {
	private boolean lmb, rmb, mmb;
	
	protected MouseManager() {}
	
	public void pressButton(int button) {
		switch (button) {
			case GLFW.GLFW_MOUSE_BUTTON_LEFT:
				lmb = true;
				break;
			case GLFW.GLFW_MOUSE_BUTTON_MIDDLE:
				mmb = true;
				break;
			case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
				rmb = true;
				break;
		}
	}
	
	public void releaseButton(int button) {
		switch (button) {
			case GLFW.GLFW_MOUSE_BUTTON_LEFT:
				lmb = false;
				break;
			case GLFW.GLFW_MOUSE_BUTTON_MIDDLE:
				mmb = false;
				break;
			case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
				rmb = false;
				break;
		}
	}
	
	public boolean isPressed(int button) {
		switch (button) {
			case GLFW.GLFW_MOUSE_BUTTON_LEFT:
				return lmb;
			case GLFW.GLFW_MOUSE_BUTTON_MIDDLE:
				return mmb;
			case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
				return rmb;
		}
		return false;
	}
}
