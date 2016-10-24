package com.chrisithomas.clothtest.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;

class Renderer {
	private final static int SIZE = GL12.GL_MAX_ELEMENTS_VERTICES;
	
	private final FloatBuffer buffer;
	private float[] data = new float[SIZE * 8];
	private int vertCount = 0;
	
	Renderer() {
		this.buffer = BufferUtils.createFloatBuffer(data.length);
	}
	
	public void clear() {
		vertCount = 0;
	}
	
	public void addVertex(float x, float y, float tx, float ty, int r, int g, int b) {
		int vert = vertCount * 8;
		vertCount++;
		data[vert] = x;
		data[vert + 1] = y;
		data[vert + 2] = 0;
		data[vert + 3] = tx;
		data[vert + 4] = ty;
		data[vert + 5] = r / 255f;
		data[vert + 6] = g / 255f;
		data[vert + 7] = b / 255f;
		
		if (vertCount >= SIZE) {
			build();
			clear();
		}
	}
	
	public void addVertex(float x, float y, float tx, float ty, int rgb) {
		addVertex(x, y, tx, ty,
				(rgb >> 16) & 0xFF,
				(rgb >> 8) & 0xFF,
				rgb & 0xFF);
	}
	
	int begin(Integer glListId) {
		if (glListId == null) glListId = GL11.glGenLists(1);
		GL11.glNewList(glListId, GL11.GL_COMPILE);
		return glListId;
	}
	
	void end() {
		if (vertCount > 0) build();
		GL11.glEndList();
	}
	
	private void build() {
		buffer.position(0);
		buffer.put(data);
		buffer.flip();
		
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		buffer.position(0);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0);
		
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		buffer.position(3);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12);
		
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		buffer.position(5);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 32, 20);
		
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTexture());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertCount);
		
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
	}
}
