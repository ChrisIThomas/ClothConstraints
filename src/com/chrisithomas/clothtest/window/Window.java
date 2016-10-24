package com.chrisithomas.clothtest.window;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseCallback;
	private GLFWWindowSizeCallback resizeCallback;
	
	// The window handle
	private long window;
	
	private int width, height, lastWidth, lastHeight;
	private boolean open = false;
	private boolean fullscreen = false;
	private String title;
	private List<KeyListener> keyListeners = new LinkedList<KeyListener>();
	private List<MouseListener> mouseListeners = new LinkedList<MouseListener>();
	private List<WindowListener> windowListeners = new LinkedList<WindowListener>();
	private MouseManager mouseManager = new MouseManager();
	
	public Window() {
		title = "Window";
		width = 800;
		height = 600;
	}
	
	public Window(String title) {
		this();
		this.title = title;
	}
	
	public Window(int width, int height) {
		this();
		this.width = width;
		this.height = height;
	}
	
	public Window(String title, int width, int height) {
		this();
		this.title = title;
		this.width = width;
		this.height = height;
	}
	
	public void create() {
		//Initialize GLFW
		GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
		
		//Configure the window
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
		
		lastWidth = width;
		lastHeight = height;
		//Create the window
		window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window");
		
		initWindow();
		initGL();
		initCallbacks();
		open = true;
	}
	
	public void destroy() {
		GLFW.glfwDestroyWindow(window);
		keyCallback.free();
		mouseCallback.free();
		resizeCallback.free();
		open = false;
	}
	
	public void finalize() {
		if (open) destroy();
		GLFW.glfwTerminate();
		errorCallback.free();
	}
	
	private void initWindow() {
		if (!fullscreen) {
			//Get the resolution of the primary monitor
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			//Center our window
			GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		}
		
		//Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(window);
		//Enable v-sync
		GLFW.glfwSwapInterval(1);
		
		//Make the window visible
		GLFW.glfwShowWindow(window);
		//Mouse.lock(true);
	}
	
	private void initCallbacks() {
		//Setup a key callback.
		GLFW.glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				for (KeyListener k: keyListeners) k.keyEvent(new KeyListener.Key(key, action, mods));
			}
		});
		
		//Setup a mouse callback.
		GLFW.glfwSetMouseButtonCallback(window, mouseCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				double[] x = new double[1];
				double[] y = new double[1];
				GLFW.glfwGetCursorPos(window, x, y);
				if (action == GLFW.GLFW_PRESS) mouseManager.pressButton(button);
				if (action == GLFW.GLFW_RELEASE) mouseManager.releaseButton(button);
				for (MouseListener m: mouseListeners) m.mouseClickEvent(button, (int) x[0], (int) y[0]);
			}
		});
		
		GLFW.glfwSetWindowSizeCallback(window, resizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int newWidth, int newHeight) {
				width = newWidth;
				height = newHeight;
				for (WindowListener w: windowListeners) w.windowResizeEvent(width, height);
			}
		});
	}
	
	private void initGL() {
		//Initialize OpenGL here
		GL.createCapabilities();
		GL11.glClearColor(0f, 0f, 0f, 1f);
        
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NEAREST);
	}
	
	public void processInput() {
		boolean lmb = mouseManager.isPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT);
		boolean mmb = mouseManager.isPressed(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
		boolean rmb = mouseManager.isPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
		GLFW.glfwPollEvents();
		
		double[] x = new double[1];
		double[] y = new double[1];
		GLFW.glfwGetCursorPos(window, x, y);
		if (lmb && mouseManager.isPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT))
			for (MouseListener m: mouseListeners) m.mouseDragEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT, (int) x[0], (int) y[0]);
		if (mmb && mouseManager.isPressed(GLFW.GLFW_MOUSE_BUTTON_MIDDLE))
			for (MouseListener m: mouseListeners) m.mouseDragEvent(GLFW.GLFW_MOUSE_BUTTON_MIDDLE, (int) x[0], (int) y[0]);
		if (rmb && mouseManager.isPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT))
			for (MouseListener m: mouseListeners) m.mouseDragEvent(GLFW.GLFW_MOUSE_BUTTON_RIGHT, (int) x[0], (int) y[0]);
	}
	
	public void preRender() {
		GL11.glDepthMask(true);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_LEQUAL, 1);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		
		GL11.glLoadIdentity();
	}
	
	public void postRender() {
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void set2D() {
		GL11.glViewport(0, 0, width, height);
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	public void set3D() {
		double fov = 90.0;
		double zNear = 0.01f;
		double zFar = 150.0f;
		double ratio = ((double) getWindowWidth()) / ((double) getWindowHeight());
		double halfW, halfH;
		halfH = Math.tan(fov / 360 * Math.PI) * zNear;
		halfW = halfH * ratio;
		GL11.glFrustum(-halfW, halfW, -halfH, halfH, zNear, zFar);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	public void finishRendering() {
		GLFW.glfwSwapBuffers(window);
	}
	
	public void close() {
		GLFW.glfwSetWindowShouldClose(window, true);
		open = false;
	}
	
	public int getWindowWidth() {
		return width;
	}
	
	public int getWindowHeight() {
		return height;
	}
	
	public double time() {
		return GLFW.glfwGetTime();
	}
	
	public boolean isOpen() {
		if (GLFW.glfwWindowShouldClose(window)) open = false;
		return open;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String newTitle) {
		title = newTitle;
	}
	
	public void toggleFullscreen() {
		fullscreen = !fullscreen;
		open = false;
		if (fullscreen) {
			lastWidth = width;
			lastHeight = height;
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			width = vidMode.width();
			height = vidMode.height();
		} else {
			width = lastWidth;
			height = lastHeight;
		}
		long newWindow = GLFW.glfwCreateWindow(width, height, title, fullscreen ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL, window);
		GLFW.glfwDestroyWindow(window);
		window = newWindow;
		
		keyCallback.free();
		mouseCallback.free();
		resizeCallback.free();
		
		initWindow();
		initGL();
		initCallbacks();
		if (!fullscreen) {
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
		}
		open = true;
	}
	
	public void addKeyListener(KeyListener l) {
		keyListeners.add(l);
	}
	
	public void removeKeyListener(KeyListener l) {
		keyListeners.remove(l);
	}
	
	public void addMouseListener(MouseListener l) {
		mouseListeners.add(l);
	}
	
	public void removeMouseListener(MouseListener l) {
		mouseListeners.remove(l);
	}
	
	public void addWindowListener(WindowListener l) {
		windowListeners.add(l);
	}
	
	public void removeWindowListener(WindowListener l) {
		windowListeners.remove(l);
	}
}
