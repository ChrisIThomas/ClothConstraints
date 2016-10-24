package com.chrisithomas.clothtest.window;

import org.lwjgl.glfw.GLFW;

public interface KeyListener {
	public void keyEvent(Key key);
	
	public class Key {
		private final int key, action, mods;
		
		public Key(int key, int action, int mods) {
			this.key = key;
			this.mods = mods;
			this.action = action;
		}
		
		public int key() {
			return key;
		}
		
		public boolean isPressed() {
			return action == GLFW.GLFW_PRESS;
		}
		
		public boolean isReleased() {
			return action == GLFW.GLFW_RELEASE;
		}
		
		public boolean isHeld() {
			return action == GLFW.GLFW_REPEAT;
		}
		
		public boolean isShift() {
			return (mods & GLFW.GLFW_MOD_SHIFT) == GLFW.GLFW_MOD_SHIFT;
		}
		
		public boolean isAlt() {
			return (mods & GLFW.GLFW_MOD_ALT) == GLFW.GLFW_MOD_ALT;
		}
		
		public boolean isControl() {
			return (mods & GLFW.GLFW_MOD_CONTROL) == GLFW.GLFW_MOD_CONTROL;
		}
		
		public boolean isSuper() {
			return (mods & GLFW.GLFW_MOD_SUPER) == GLFW.GLFW_MOD_SUPER;
		}
		
		public String name() {
			return GLFW.glfwGetKeyName(key, 0);
		}
	}
}
