package org.wraith.engine.util;

public class Algorithms{
	public static int groupLocation(int x, int w){
		return x>=0?x/w*w:(x-(w-1))/w*w;
	}
}
