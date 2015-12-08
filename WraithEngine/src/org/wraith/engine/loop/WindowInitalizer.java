package org.wraith.engine.loop;

public class WindowInitalizer{
	private final int windowWidth;
	private final int windowHeight;
	private final boolean fullscreen;
	private final boolean vSync;
	private final String windowTitle;
	private final InputHandler inputHandler;
	public WindowInitalizer(
		int windowWidth, int windowHeight, boolean fullscreen, boolean vSync, String windowTitle,
		InputHandler inputHandler){
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.fullscreen = fullscreen;
		this.vSync = vSync;
		this.windowTitle = windowTitle;
		this.inputHandler = inputHandler;
	}
	public int getWindowHeight(){
		return windowHeight;
	}
	public String getWindowTitle(){
		return windowTitle;
	}
	public int getWindowWidth(){
		return windowWidth;
	}
	public boolean isFullscreen(){
		return fullscreen;
	}
	public boolean isvSync(){
		return vSync;
	}
	public InputHandler getInputHandler(){
		return inputHandler;
	}
}
