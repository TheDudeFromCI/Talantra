package org.wraithaven.games.Renderer;

import org.wraith.engine.rendering.PostProcessor;
import org.wraith.engine.rendering.ShaderProgram;

public class TimedPostProcessor extends PostProcessor{
	private float time;
	public TimedPostProcessor(int width, int height, ShaderProgram shader){
		super(width, height, shader);
	}
	public void update(double time){
		this.time = (float)time;
	}
	@Override
	protected void prepareRender(){
		shader.setUniform1f(0, time);
	}
}
