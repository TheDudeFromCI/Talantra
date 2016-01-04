package org.wraithaven.games.Renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.text.NumberFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.wraith.engine.loop.MainLoop;
import org.wraith.engine.loop.RenderLoop;
import org.wraith.engine.loop.WindowInitalizer;
import org.wraith.engine.math.Matrix4f;
import org.wraith.engine.math.Vector3f;
import org.wraith.engine.rendering.TimeKeeper;

public class TalantraClient extends MainLoop implements RenderLoop{
	public static void main(String[] args){
		TalantraClient talantra = new TalantraClient();
		WindowInitalizer window = new WindowInitalizer(800, 600, false, false, "Talantra", talantra.getInput());
		talantra.buildWindow(window);
		talantra.begin(talantra, true);
	}
	private Input input;
	private TimeKeeper timeKeeper;
	private double lastFpsUpdate = -1;
	public TalantraClient(){
		input = new Input();
	}
	public Input getInput(){
		return input;
	}
	public void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL20.glEnableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 12, 0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		GL20.glDisableVertexAttribArray(0);
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
	}
	@Override
	protected void dispose(){
		GL15.glDeleteBuffers(vertexBuffer);
		GL30.glDeleteVertexArrays(vertexArrayId);
		GL20.glDeleteProgram(programId);
	}
	private int programId;
	private void loadShaders(String vertexShaderPath, String fragmentShaderPath){
		String vertexShaderCode = readFile(vertexShaderPath);
		int vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vertexShaderId, vertexShaderCode);
		GL20.glCompileShader(vertexShaderId);
		int vsStatus = GL20.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS);
		if(vsStatus!=GL11.GL_TRUE)
			throw new RuntimeException(GL20.glGetShaderInfoLog(vertexShaderId));
		String fragmentShaderCode = readFile(fragmentShaderPath);
		int fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fragmentShaderId, fragmentShaderCode);
		GL20.glCompileShader(fragmentShaderId);
		int fsStatus = GL20.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS);
		if(fsStatus!=GL11.GL_TRUE)
			throw new RuntimeException(GL20.glGetShaderInfoLog(fragmentShaderId));
		programId = GL20.glCreateProgram();
		GL20.glAttachShader(programId, vertexShaderId);
		GL20.glAttachShader(programId, fragmentShaderId);
		GL20.glLinkProgram(programId);
		GL20.glValidateProgram(programId);
		int pStatus = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS);
		if(pStatus!=GL11.GL_TRUE)
			throw new RuntimeException(GL20.glGetProgramInfoLog(programId));
		GL20.glDetachShader(programId, vertexShaderId);
		GL20.glDetachShader(programId, fragmentShaderId);
		GL20.glDeleteShader(vertexShaderId);
		GL20.glDeleteShader(fragmentShaderId);
	}
	private static String readFile(String path){
		File file = new File(path);
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String code = "";
			String line;
			while((line = in.readLine())!=null)
				code += line+"\n";
			in.close();
			return code;
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return null;
	}
	private int vertexBuffer;
	private int vertexArrayId;
	@Override
	protected void preloop(){
		// Initalize OpenGL
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f);
		GL11.glClearColor(0, 0, 0, 1);
		setFpsCap(60);
		//Time keeper
		timeKeeper = new TimeKeeper();
		//Build cube.
		vertexArrayId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vertexArrayId);
		FloatBuffer vertex_buffer_data = BufferUtils.createFloatBuffer(9);
		vertex_buffer_data.put(-1.0f).put(-1.0f).put(0.0f);
		vertex_buffer_data.put(1.0f).put(-1.0f).put(0.0f);
		vertex_buffer_data.put(0.0f).put(1.0f).put(0.0f);
		vertex_buffer_data.flip();
		vertexBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex_buffer_data, GL15.GL_STATIC_DRAW);
		//Load shader.
		loadShaders("C:/Users/TheDudeFromCI/Desktop/Vertex.txt", "C:/Users/TheDudeFromCI/Desktop/Fragment.txt");
		GL20.glUseProgram(programId);
		handleMatrixMath();
	}
	private void handleMatrixMath(){
		Matrix4f projection = Matrix4f.perspective(45, 4/3f, 0.1f, 100.0f);
		Matrix4f view = Matrix4f.lookAt(new Vector3f(4, 3, 3), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
		Matrix4f model = new Matrix4f();
		GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(programId, "projection"), false, projection.getBuffer());
		GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(programId, "view"), false, view.getBuffer());
		GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(programId, "model"), false, model.getBuffer());
	}
}
