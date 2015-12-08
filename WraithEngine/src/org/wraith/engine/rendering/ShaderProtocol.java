package org.wraith.engine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderProtocol{
	private final ShaderProgram shader;
	private final ShaderProtocolVariable[] variables;
	private final int sizeInBytes;
	public ShaderProtocol(ShaderProgram shader, ShaderProtocolVariable[] variables, boolean prepareShader){
		this.shader = shader;
		this.variables = variables;
		{
			// Count total size in bytes;
			int b = 0;
			for(ShaderProtocolVariable v : variables){
				v.setOffset(b);
				if(v.getType()==GL11.GL_FLOAT)
					b += v.getSize()*4;
				if(v.getType()==GL11.GL_UNSIGNED_INT)
					b += v.getSize()*4;
				if(v.getType()==GL11.GL_UNSIGNED_SHORT)
					b += v.getSize()*2;
				if(v.getType()==GL11.GL_UNSIGNED_BYTE)
					b += v.getSize();
			}
			sizeInBytes = b;
		}
		{
			// Setup shader.
			if(prepareShader){
				String[] att = new String[variables.length];
				for(int i = 0; i<att.length; i++)
					att[i] = variables[i].getName();
				shader.loadAttributes(att);
			}
		}
	}
	public void bind(){
		shader.bind();
	}
	public ShaderProgram getShader(){
		return shader;
	}
	public void point(){
		for(int i = 0; i<variables.length; i++)
			GL20.glVertexAttribPointer(shader.getAttributeLocation(i), variables[i].getSize(), variables[i].getType(), variables[i].isNormalize(),
				sizeInBytes, variables[i].getOffset());
	}
	public void unbind(){
		shader.unbind();
	}
}
