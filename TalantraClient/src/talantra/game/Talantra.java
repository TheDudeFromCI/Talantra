package talantra.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Talantra{
	public static void buildDisplay(){
		try{
			System.out.println("Creating display.");
			PixelFormat pixelFormat = new PixelFormat();
			ContextAttribs contextAtrributes = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("Talantra");
			Display.create(pixelFormat, contextAtrributes);
			GL11.glViewport(0, 0, 800, 600);
			System.out.println("OpenGL version: "+GL11.glGetString(GL11.GL_VERSION));
		}catch(LWJGLException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	public static void gameLoop(){
		Camera camera = new Camera();
		{
			camera.setPerspective(70, 4/3f, 0.1f, 100.f);
			camera.moveTo(0, 1, 3);
			camera.lookAt(0, 0, 0);
		}
		File vertexShader = new File("C:/Users/TheDudeFromCI/Desktop/Vertex.txt");
		File fragmentShader = new File("C:/Users/TheDudeFromCI/Desktop/Fragment.txt");
		ShaderProgram shader = new ShaderProgram(vertexShader, null, fragmentShader);
		shader.loadUniforms("projectionMatrix", "viewMatrix", "modelMatrix");
		Universe uni = new Universe();
		{
			uni.setCamera(camera);
			uni.setShader(shader, 0, 1, 2);
			uni.addModel(new Model());
			uni.getFlags().setDepthTest(true);
			uni.getFlags().setTexture2D(true);
			uni.getFlags().setCullFace(false);
			uni.getFlags().setWireframe(false);
		}
		int texIds;
		{
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			texIds = GL11.glGenTextures();
			texIds = loadPNGTexture("C:/Users/TheDudeFromCI/Desktop/Texture1.png", GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIds);
		}
		while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)&&!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
			uni.update(0, 0);
			uni.render();
			Display.sync(60);
			Display.update();
		}
		{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glDeleteTextures(texIds);
		}
		shader.dispose();
	}
	public static void initalizeGame(){
		UniverseFlags.initalize();
	}
	public static void main(String[] args){
		System.out.println("Starting Talanta.");
		new CommandListener();
		// TODO Load config. Build display from properties listed in config.
		buildDisplay();
		initalizeGame();
		gameLoop();
		safeShutdown();
	}
	public static void safeShutdown(){
		Display.destroy();
		System.exit(0);
	}
	private static int loadPNGTexture(String filename, int textureUnit){
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;
		try{
			InputStream in = new FileInputStream(filename);
			PNGDecoder decoder = new PNGDecoder(in);
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();
			buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
			decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
			buf.flip();
			in.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, tWidth, tHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		return texId;
	}
}
