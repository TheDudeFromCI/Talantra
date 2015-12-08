package org.wraith.engine.loop;

import java.nio.ByteBuffer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

public abstract class MainLoop{
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWScrollCallback scrollCallback;
	private long window;
	private boolean windowOpen = false;
	private int fpsCap = 30;
	private boolean rebuildWindow = false;
	private WindowInitalizer nextWindowStats;
	public void begin(RenderLoop renderLoop, boolean exitGame){
		try{
			loop(renderLoop);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		cleanup();
		if(exitGame)
			System.exit(0);
	}
	public void buildWindow(WindowInitalizer windowInitalizer){
		if(windowOpen){
			rebuildWindow = true;
			nextWindowStats = windowInitalizer;
			return;
		}
		window(windowInitalizer);
	}
	public int getFpsCap(){
		return fpsCap;
	}
	public long getWindowId(){
		return window;
	}
	public void setFpsCap(int cap){
		fpsCap = cap;
	}
	private void cleanup(){
		dispose();
		keyCallback.release();
		mouseButtonCallback.release();
		cursorPosCallback.release();
		scrollCallback.release();
		errorCallback.release();
		destoryWindow();
	}
	private void destoryWindow(){
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
	private void loop(RenderLoop renderLoop){
		preloop();
		double lastFrameTime = 0;
		double currentFrameTime;
		double delta;
		long sleepTime;
		long yieldTime;
		long overSleep;
		long variableYieldTime = 0;
		long lastTime = 0;
		long t;
		do{
			rebuildWindow = false;
			while(GLFW.glfwWindowShouldClose(window)==GL11.GL_FALSE&&!rebuildWindow){
				currentFrameTime = GLFW.glfwGetTime();
				delta = currentFrameTime-lastFrameTime;
				lastFrameTime = currentFrameTime;
				renderLoop.update(delta, currentFrameTime);
				renderLoop.render();
				GLFW.glfwSwapBuffers(window);
				GLFW.glfwPollEvents();
				if(fpsCap>0){
					sleepTime = 1000000000/fpsCap;
					yieldTime = Math.min(sleepTime, variableYieldTime+sleepTime%(1000*1000));
					overSleep = 0;
					try{
						while(true){
							t = System.nanoTime()-lastTime;
							if(t<sleepTime-yieldTime)
								Thread.sleep(1);
							else if(t<sleepTime)
								Thread.yield();
							else{
								overSleep = t-sleepTime;
								break;
							}
						}
					}catch(InterruptedException e){
						// We don't need to hear you rant.
					}finally{
						lastTime = System.nanoTime()-Math.min(overSleep, sleepTime);
						if(overSleep>variableYieldTime)
							variableYieldTime = Math.min(variableYieldTime+200*1000, sleepTime);
						else if(overSleep<variableYieldTime-200*1000)
							variableYieldTime = Math.max(variableYieldTime-2*1000, 0);
					}
				}
			}
			if(rebuildWindow){
				destoryWindow();
				window(nextWindowStats);
			}
		}while(rebuildWindow);
	}
	private void window(final WindowInitalizer windowInitalizer){
		windowOpen = true;
		nextWindowStats = null;
		GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));
		if(GLFW.glfwInit()!=GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_FALSE);
		window = GLFW.glfwCreateWindow(windowInitalizer.getWindowWidth(), windowInitalizer.getWindowHeight(), windowInitalizer.getWindowTitle(),
			windowInitalizer.isFullscreen()?GLFW.glfwGetPrimaryMonitor():MemoryUtil.NULL, MemoryUtil.NULL);
		if(window==MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		GLFW.glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback(){
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods){
				windowInitalizer.getInputHandler().keyPressed(window, key, action);
			}
		});
		GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback(){
			@Override
			public void invoke(long window, int button, int action, int mods){
				windowInitalizer.getInputHandler().mouseClicked(window, button, action);
			}
		});
		GLFW.glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback(){
			@Override
			public void invoke(long window, double xpos, double ypos){
				windowInitalizer.getInputHandler().mouseMove(window, xpos, ypos);
			}
		});
		GLFW.glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback(){
			@Override
			public void invoke(long window, double xoffset, double yoffset){
				windowInitalizer.getInputHandler().mouseWheel(window, xoffset, yoffset);
			}
		});
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		if(!windowInitalizer.isFullscreen())
			GLFW.glfwSetWindowPos(window, (GLFWvidmode.width(vidmode)-windowInitalizer.getWindowWidth())/2,
				(GLFWvidmode.height(vidmode)-windowInitalizer.getWindowHeight())/2);
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(windowInitalizer.isvSync()?1:0);
		GLFW.glfwShowWindow(window);
		GLContext.createFromCurrent();
	}
	protected abstract void dispose();
	protected abstract void preloop();
}
