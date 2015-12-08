package org.wraith.engine.rendering;

public class PostProcessor{
	private final FramePainter framePainter;
	protected ShaderProgram shader;
	public PostProcessor(int width, int height, ShaderProgram shader){
		framePainter = new FramePainter(width, height);
		this.shader = shader;
	}
	public final void beginFrame(){
		framePainter.beginFrameBuffer();
	}
	public void dispose(){
		framePainter.dispose();
		shader.dispose();
	}
	public final void endFrame(){
		FramePainter.endFrameBuffer();
		shader.bind();
		prepareRender();
		framePainter.renderToScreen(shader.getAttributeLocation(0), shader.getAttributeLocation(1));
		shader.unbind();
	}
	public void loadShader(ShaderProgram shader){
		if(shader!=null)
			shader.dispose();
		this.shader = shader;
	}
	protected void prepareRender(){}
}
