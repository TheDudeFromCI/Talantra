package org.wraith.engine.rendering;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class FramePainter{
	public static void endFrameBuffer(){
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	private final int textureId;
	private final int framebufferId;
	private final int renderbufferId;
	private int vboId = -1;
	public FramePainter(int width, int height){
		textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE,
			BufferUtils.createByteBuffer(width*height*4));
		framebufferId = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferId);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, textureId, 0);
		renderbufferId = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderbufferId);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, renderbufferId);
		if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER)!=GL30.GL_FRAMEBUFFER_COMPLETE)
			throw new RuntimeException("Graphics card does not support frame buffers!");
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	public void beginFrameBuffer(){
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferId);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderbufferId);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	public void dispose(){
		GL11.glDeleteTextures(textureId);
		GL30.glDeleteRenderbuffers(renderbufferId);
		GL30.glDeleteFramebuffers(framebufferId);
		GL30.glDeleteFramebuffers(framebufferId);
		if(vboId!=-1)
			GL15.glDeleteBuffers(vboId);
	}
	public int getTextureId(){
		return textureId;
	}
	public void renderToScreen(int vertexPositionAttrib, int textureCoordAttrib){
		if(vboId==-1){
			// This means that a vbo is only prepared the first time this method is called. If this method is never called, then a vbo is never created,
			// and no resources are consumed.
			vboId = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
			{
				FloatBuffer data = BufferUtils.createFloatBuffer(16);
				data.put(0.0f).put(0.0f).put(-1.0f).put(-1.0f);
				data.put(1.0f).put(0.0f).put(1.0f).put(-1.0f);
				data.put(0.0f).put(1.0f).put(-1.0f).put(1.0f);
				data.put(1.0f).put(1.0f).put(1.0f).put(1.0f);
				data.flip();
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
			}
		}else
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL20.glVertexAttribPointer(textureCoordAttrib, 2, GL11.GL_FLOAT, false, 16, 0);
		GL20.glVertexAttribPointer(vertexPositionAttrib, 2, GL11.GL_FLOAT, false, 16, 8);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
}
