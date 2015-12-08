package org.wraith.engine.rendering.Shaders;

import org.wraith.engine.rendering.ShaderProgram;

public class BasicShader extends ShaderProgram{
	public BasicShader(){
		super(
			"#version 330\nin vec2 att_position;\nin vec2 att_uv;\nout vec2 uv;\nvoid main(){\ngl_Position=vec4(att_position,0.0,1.0);\nuv=att_uv;\n}",
			null,
			"#version 330\nuniform sampler2D colorTexture;\nin vec2 uv;\nout vec4 fragColor;\nvoid main(){\nfragColor=texture2D(colorTexture, uv);\n}");
		loadAttributes("att_position", "att_uv");
		loadUniforms("colorTexture");
	}
}
