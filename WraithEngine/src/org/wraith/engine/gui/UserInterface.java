package org.wraith.engine.gui;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.wraith.engine.rendering.ShaderProgram;

public class UserInterface{
	private final ArrayList<UserInterfaceComponent> components = new ArrayList<>();
	private final ShaderProgram shader;
	private final int vboId;
	public UserInterface(){
		shader = new ShaderProgram("/home/thedudefromci/Desktop".replace('/', File.separatorChar), "Shader");
		shader.loadAttributes("att_uv", "att_position");
		shader.loadUniforms("colorTexture");
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
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	public void dispose(){
		while(components.size()>0)
			components.get(0).dispose();
		GL15.glDeleteBuffers(vboId);
		shader.dispose();
	}
	public void render(){
		if(components.isEmpty())
			return;
		shader.bind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL20.glVertexAttribPointer(shader.getAttributeLocation(0), 2, GL11.GL_FLOAT, false, 16, 0);
		GL20.glVertexAttribPointer(shader.getAttributeLocation(1), 2, GL11.GL_FLOAT, false, 16, 8);
		for(UserInterfaceComponent c : components)
			c.render();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		shader.unbind();
	}
	public void update(){
		for(UserInterfaceComponent c : components)
			c.update();
	}
	void addComponent(UserInterfaceComponent component){
		components.add(component);
		sortOrder();
	}
	void removeComponent(UserInterfaceComponent component){
		components.remove(component);
	}
	void sortOrder(){
		Collections.sort(components);
	}
}
