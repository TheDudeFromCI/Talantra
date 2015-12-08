package org.wraith.engine.rendering;

public class ShaderProtocolVariable{
	private final int size;
	private final boolean normalize;
	private final int type;
	private final String name;
	private int offset;
	public ShaderProtocolVariable(int size, boolean normalize, int type, String name){
		this.size = size;
		this.normalize = normalize;
		this.type = type;
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public int getSize(){
		return size;
	}
	public int getType(){
		return type;
	}
	public boolean isNormalize(){
		return normalize;
	}
	public int getOffset(){
		return offset;
	}
	public void setOffset(int offset){
		this.offset = offset;
	}
}
