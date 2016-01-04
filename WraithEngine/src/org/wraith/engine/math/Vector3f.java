package org.wraith.engine.math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class Vector3f{
	public static Vector3f subtract(Vector3f left, Vector3f right){
		return new Vector3f(left.x-right.x, left.y-right.y, left.z-right.z);
	}
	public static Vector3f normalize(Vector3f vector){
		Vector3f n = vector.clone();
		n.normalize();
		return n;
	}
	public static Vector3f cross(Vector3f left, Vector3f right){
		Vector3f n = left.clone();
		n.cross(right);
		return n;
	}
	public static Vector3f lerp(Vector3f a, Vector3f b, float alpha){
		float i = 1-alpha;
		float x = a.x*i+b.x*alpha;
		float y = a.y*i+b.y*alpha;
		float z = a.z*i+b.z*alpha;
		return new Vector3f(x, y, z);
	}
	public float x;
	public float y;
	public float z;
	public Vector3f(){
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}
	public Vector3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3f add(Vector3f other){
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}
	public Vector3f cross(Vector3f other){
		float x = y*other.z-z*other.y;
		float y = z*other.x-this.x*other.z;
		float z = this.x*other.y-this.y*other.x;
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	public Vector3f divide(float scalar){
		x /= scalar;
		y /= scalar;
		z /= scalar;
		return this;
	}
	public float dot(Vector3f other){
		return x*other.x+y*other.y+z*other.z;
	}
	public FloatBuffer getBuffer(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		buffer.put(x).put(y).put(z);
		buffer.flip();
		return buffer;
	}
	public float length(){
		return (float)Math.sqrt(lengthSquared());
	}
	public float lengthSquared(){
		return x*x+y*y+z*z;
	}
	public Vector3f negate(){
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	public Vector3f normalize(){
		divide(length());
		return this;
	}
	public Vector3f multiply(float scalar){
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}
	public Vector3f clone(){
		return new Vector3f(x, y, z);
	}
	public Vector3f subtract(Vector3f other){
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}
}
