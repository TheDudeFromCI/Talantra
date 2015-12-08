package org.wraith.engine.rendering;

public class TimeKeeper{
	private double time;
	private double delta;
	private double[] deltaTimes = new double[30];
	private int frameIndex = 0;
	private int frameCountSize = 0;
	public float getAverageFrameTime(){
		double total = 0;
		for(int i = 0; i<frameCountSize; i++)
			total += deltaTimes[i];
		return (float)(total/frameCountSize);
	}
	public double getFrameDelta(){
		return delta;
	}
	public double getFrameTime(){
		return time;
	}
	public void update(double delta, double time){
		this.time = time;
		this.delta = delta;
		deltaTimes[frameIndex] = delta;
		frameIndex = (frameIndex+1)%deltaTimes.length;
		if(frameCountSize<deltaTimes.length)
			frameCountSize++;
	}
}
