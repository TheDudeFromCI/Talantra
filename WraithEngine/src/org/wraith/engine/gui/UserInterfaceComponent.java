package org.wraith.engine.gui;

import org.lwjgl.opengl.GL11;
import org.wraith.engine.rendering.Texture;

public class UserInterfaceComponent implements Comparable<UserInterfaceComponent>{
	private final UserInterface ui;
	private final Texture texture;
	private final boolean textureOwner;
	private float x;
	private float y;
	private float z;
	private float w;
	private float h;
	private float r;
	private float a;
	public UserInterfaceComponent(UserInterface ui, Texture texture){
		this.ui = ui;
		this.texture = texture;
		textureOwner = false;
		ui.addComponent(this);
	}
	public UserInterfaceComponent(UserInterface ui, Texture texture, boolean textureOwner){
		this.ui = ui;
		this.texture = texture;
		this.textureOwner = textureOwner;
		ui.addComponent(this);
	}
	public int compareTo(UserInterfaceComponent o){
		UserInterfaceComponent com = (UserInterfaceComponent)o;
		return z==com.z?0:z<com.z?1:-1;
	}
	public void dispose(){
		ui.removeComponent(this);
		if(textureOwner)
			texture.dispose();
	}
	public float getA(){
		return a;
	}
	public float getH(){
		return h;
	}
	public float getR(){
		return r;
	}
	public UserInterface getUi(){
		return ui;
	}
	public float getW(){
		return w;
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public float getZ(){
		return z;
	}
	public void render(){
		texture.bind();
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		Texture.unbind();
	}
	public void setA(float a){
		this.a = a;
	}
	public void setH(float h){
		this.h = h;
	}
	public void setR(float r){
		this.r = r;
	}
	public void setW(float w){
		this.w = w;
	}
	public void setX(float x){
		this.x = x;
	}
	public void setY(float y){
		this.y = y;
	}
	public void setZ(float z){
		this.z = z;
		ui.sortOrder();
	}
	public void update(){}
}
