package org.wraith.engine.rendering;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.wraith.engine.math.Matrix4f;

public class Model implements Comparable<Model>{
	private final int vbo;
	private final int ibo;
	private final int indexCount;
	private final ShaderProtocol protocol;
	private final Matrix4f modelViewProjectionMatrix;
	public Model(ShaderProtocol protocol, FloatBuffer vertexData, ShortBuffer indexData, Matrix4f modelViewProjectionMatrix){
		this.protocol = protocol;
		this.modelViewProjectionMatrix = modelViewProjectionMatrix;
		indexCount = indexData.capacity();
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	public int compareTo(Model m){
		return m.protocol.getShader()==protocol.getShader()?0:m.protocol.getShader().getId()>protocol.getShader().getId()?1:-1;
	}
	public void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
	}
	public ShaderProtocol getShader(){
		return protocol;
	}
	public void render(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		protocol.getShader().setUniformMat4(0, modelViewProjectionMatrix.getBuffer());
		protocol.point();
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_SHORT, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	public void update(){
		// Do nothing.
	}
}
