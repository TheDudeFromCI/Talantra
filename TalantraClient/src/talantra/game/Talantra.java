package talantra.game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

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
		// TODO Handle game loop.
		while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)&&!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			Display.sync(60);
			Display.update();
		}
	}
	public static void initalizeGame(){
		// TODO Initalize game.
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
}
