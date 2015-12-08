package org.wraith.engine.loop;

public interface InputHandler{
	public void keyPressed(long window, int key, int action);
	public void mouseClicked(long window, int button, int action);
	public void mouseMove(long window, double x, double y);
	public void mouseWheel(long window, double x, double y);
}
