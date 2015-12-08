package org.wraith.engine.rendering.Shaders;

import org.wraith.engine.math.Vector4f;
import org.wraith.engine.rendering.ShaderProgram;

public class ScreenTintShader extends ShaderProgram{
	private final Vector4f color;
	private boolean changedColor;
	public ScreenTintShader(){
		super(
			"#version 330\nin vec2 att_position;\nin vec2 att_uv;\nout vec2 uv;\nvoid main(){\ngl_Position=vec4(att_position,0.0,1.0);\nuv=att_uv;\n}",
			null, "#version 330\n"+"uniform sampler2D colorTexture;\n"+"uniform vec4 uni_tintColor;\n"+"in vec2 uv;\n"+"out vec4 fragColor;\n"+"\n"
				+"void main(){\n"+"	fragColor = vec4(mix(texture2D(colorTexture, uv).rgb, uni_tintColor.rgb, uni_tintColor.a), 1.0);\n"+"}");
		loadAttributes("att_position", "att_uv");
		loadUniforms("colorTexture", "uni_tintColor");
		color = new Vector4f();
		setColor(0, 0, 0, 0);
	}
	@Override
	public void bind(){
		super.bind();
		if(changedColor){
			setUniform4f(1, color.x, color.y, color.z, color.w);
			changedColor = false;
		}
	}
	public void setColor(float r, float g, float b, float a){
		color.x = r;
		color.y = g;
		color.z = b;
		color.w = a;
		changedColor = true;
	}
}
