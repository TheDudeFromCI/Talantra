package org.wraithaven.games.Renderer;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.text.NumberFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.wraith.engine.gui.UserInterface;
import org.wraith.engine.gui.UserInterfaceComponent;
import org.wraith.engine.loop.MainLoop;
import org.wraith.engine.loop.RenderLoop;
import org.wraith.engine.loop.WindowInitalizer;
import org.wraith.engine.math.Matrix4f;
import org.wraith.engine.rendering.Model;
import org.wraith.engine.rendering.PrimitiveGenerator;
import org.wraith.engine.rendering.ShaderProgram;
import org.wraith.engine.rendering.ShaderProtocol;
import org.wraith.engine.rendering.ShaderProtocolVariable;
import org.wraith.engine.rendering.Texture;
import org.wraith.engine.rendering.TimeKeeper;
import org.wraith.engine.rendering.Universe;
import org.wraith.engine.rendering.VertexBuildData;

public class TalantraClient extends MainLoop implements RenderLoop{
	public static void main(String[] args){
		TalantraClient talantra = new TalantraClient();
		WindowInitalizer window = new WindowInitalizer(800, 600, false, false, "Talantra", talantra.getInput());
		talantra.buildWindow(window);
		talantra.begin(talantra, true);
	}
	private Input input;
	private TimeKeeper timeKeeper;
	private UserInterface ui;
	private TimedPostProcessor processor;
	private Universe universe;
	private double lastFpsUpdate = -1;
	public TalantraClient(){
		input = new Input();
	}
	public Input getInput(){
		return input;
	}
	public void render(){
		processor.beginFrame();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		universe.render();
		ui.render();
		processor.endFrame();
	}
	public void update(double delta, double time){
		timeKeeper.update(delta, time);
		if(time-lastFpsUpdate>=1){
			lastFpsUpdate = time;
			float frameTime = timeKeeper.getAverageFrameTime();
			GLFW.glfwSetWindowTitle(getWindowId(),
				"Talantra [Frametime: ~"+NumberFormat.getInstance().format(frameTime*1000)+"ms ("+Math.round(1/frameTime)+" Fps)]");
		}
		input.update(time);
		universe.update();
		processor.update(time);
	}
	@Override
	protected void dispose(){
		processor.dispose();
		ui.dispose();
		universe.dispose();
	}
	@Override
	protected void preloop(){
		// Initalize OpenGL
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f);
		GL11.glClearColor(0, 0, 0, 1);
		setFpsCap(60);
		// User Interface
		ui = new UserInterface();
		new UserInterfaceComponent(ui, new Texture(new File("/home/thedudefromci/Desktop/catgirl.jpg"), false), true);
		// Other classes
		{
			File vertex = new File("/home/thedudefromci/Desktop/Shader.vert");
			File frag = new File("/home/thedudefromci/Desktop/Shader.frag");
			ShaderProgram shader = new ShaderProgram(vertex, null, frag);
			shader.loadAttributes("att_position", "att_uv");
			shader.loadUniforms("uni_time");
			processor = new TimedPostProcessor(800, 600, shader);
		}
		timeKeeper = new TimeKeeper();
		universe = new Universe();
		{
			// Load cube.
			VertexBuildData build = PrimitiveGenerator.generateBox(0.5f, 0.5f, 0.5f);
			ModelShader s = new ModelShader();
			s.loadUniforms("uni_modelView", "uni_projection");
			ShaderProtocol protocol = new ShaderProtocol(s, new ShaderProtocolVariable[]{
				new ShaderProtocolVariable(3, false, GL11.GL_FLOAT, "att_pos")
			}, true);
			FloatBuffer vertexData = BufferUtils.createFloatBuffer(build.getVertexLocations().length);
			ShortBuffer indexData = BufferUtils.createShortBuffer(build.getIndexLocations().length);
			vertexData.put(build.getVertexLocations());
			indexData.put(build.getIndexLocations());
			vertexData.flip();
			indexData.flip();
			Matrix4f per = Matrix4f.perspective(70, 4/3f, 0.1f, 100.0f);
			s.setUniformMat4(1, per.getBuffer());
			Matrix4f mat = Matrix4f.translate(0, 0, -5);
			universe.addModel(new Model(protocol, vertexData, indexData, mat));
		}
	}
}
