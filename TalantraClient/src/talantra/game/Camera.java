package talantra.game;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera{
	private static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	private final Vector3f temp = new Vector3f();
	private float x;
	private float y;
	private float z;
	private float rx;
	private float ry;
	private Matrix4f mat;
	private Matrix4f proMat;
	private FloatBuffer buf;
	public Camera(){
		mat = new Matrix4f();
		buf = BufferUtils.createFloatBuffer(16);
		updateMatrix();
	}
	public void dumpToShader(int viewLocation, int projectionLocation){
		mat.store(buf);
		buf.flip();
		GL20.glUniformMatrix4(viewLocation, false, buf);
		proMat.store(buf);
		buf.flip();
		GL20.glUniformMatrix4(projectionLocation, false, buf);
	}
	public float getRX(){
		return rx;
	}
	public float getRY(){
		return ry;
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
	public void move(float x, float y, float z){
		this.x += x;
		this.y += y;
		this.z += z;
		updateMatrix();
	}
	public void moveTo(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		updateMatrix();
	}
	public void rotateTo(float rx, float ry){
		this.rx = rx;
		this.ry = ry;
		updateMatrix();
	}
	public void setOrthographic(float left, float right, float bottom, float top, float near, float far){
		float tx = -(right+left)/(right-left);
		float ty = -(top+bottom)/(top-bottom);
		float tz = -(far+near)/(far-near);
		proMat.setIdentity();
		proMat.m00 = 2f/(right-left);
		proMat.m11 = 2f/(top-bottom);
		proMat.m22 = -2f/(far-near);
		proMat.m03 = tx;
		proMat.m13 = ty;
		proMat.m23 = tz;
	}
	public void setPerspective(float fov, float aspect, float near, float far){
		float y_scale = (float)(1/Math.tan(Math.toRadians(fov/2f)));
		float x_scale = y_scale/aspect;
		float frustum_length = far-near;
		proMat.setIdentity();
		proMat.m00 = x_scale;
		proMat.m11 = y_scale;
		proMat.m22 = -((far+near)/frustum_length);
		proMat.m23 = -1;
		proMat.m32 = -(2*near*far/frustum_length);
		proMat.m33 = 0;
	}
	private void updateMatrix(){
		mat.setIdentity();
		temp.set(x, y, z);
		mat.translate(temp);
		mat.rotate((float)Math.toRadians(rx), X_AXIS);
		mat.rotate((float)Math.toRadians(ry), Y_AXIS);
	}
}
