/*
 * The MIT License (MIT) Copyright © 2015, Heiko Brumme Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.wraith.engine.math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 * This class represents a 4x4-Matrix. GLSL equivalent to mat4.
 *
 * @author Heiko Brumme
 */
public class Matrix4f{
	/**
	 * Creates a perspective projection matrix. Similar to <code>glFrustum(left, right, bottom, top, near, far)</code>.
	 *
	 * @param left
	 *            Coordinate for the left vertical clipping pane
	 * @param right
	 *            Coordinate for the right vertical clipping pane
	 * @param bottom
	 *            Coordinate for the bottom horizontal clipping pane
	 * @param top
	 *            Coordinate for the bottom horizontal clipping pane
	 * @param near
	 *            Coordinate for the near depth clipping pane, must be positive
	 * @param far
	 *            Coordinate for the far depth clipping pane, must be positive
	 * @return Perspective matrix
	 */
	public static Matrix4f frustum(float left, float right, float bottom, float top, float near, float far){
		Matrix4f frustum = new Matrix4f();
		float a = (right+left)/(right-left);
		float b = (top+bottom)/(top-bottom);
		float c = -(far+near)/(far-near);
		float d = -(2f*far*near)/(far-near);
		frustum.m00 = 2f*near/(right-left);
		frustum.m11 = 2f*near/(top-bottom);
		frustum.m02 = a;
		frustum.m12 = b;
		frustum.m22 = c;
		frustum.m32 = -1f;
		frustum.m23 = d;
		frustum.m33 = 0f;
		return frustum;
	}
	/**
	 * Creates a orthographic projection matrix. Similar to <code>glOrtho(left, right, bottom, top, near, far)</code>.
	 *
	 * @param left
	 *            Coordinate for the left vertical clipping pane
	 * @param right
	 *            Coordinate for the right vertical clipping pane
	 * @param bottom
	 *            Coordinate for the bottom horizontal clipping pane
	 * @param top
	 *            Coordinate for the bottom horizontal clipping pane
	 * @param near
	 *            Coordinate for the near depth clipping pane
	 * @param far
	 *            Coordinate for the far depth clipping pane
	 * @return Orthographic matrix
	 */
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far){
		Matrix4f ortho = new Matrix4f();
		float tx = -(right+left)/(right-left);
		float ty = -(top+bottom)/(top-bottom);
		float tz = -(far+near)/(far-near);
		ortho.m00 = 2f/(right-left);
		ortho.m11 = 2f/(top-bottom);
		ortho.m22 = -2f/(far-near);
		ortho.m03 = tx;
		ortho.m13 = ty;
		ortho.m23 = tz;
		return ortho;
	}
	/**
	 * Creates a perspective projection matrix. Similar to <code>gluPerspective(fovy, aspec, zNear, zFar)</code>.
	 *
	 * @param fovy
	 *            Field of view angle in degrees
	 * @param aspect
	 *            The aspect ratio is the ratio of width to height
	 * @param near
	 *            Distance from the viewer to the near clipping plane, must be positive
	 * @param far
	 *            Distance from the viewer to the far clipping plane, must be positive
	 * @return Perspective matrix
	 */
	public static Matrix4f perspective(float fovy, float aspect, float near, float far){
		Matrix4f perspective = new Matrix4f();
		float f = (float)(1f/Math.tan(Math.toRadians(fovy)/2f));
		perspective.m00 = f/aspect;
		perspective.m11 = f;
		perspective.m22 = (far+near)/(near-far);
		perspective.m32 = -1f;
		perspective.m23 = 2f*far*near/(near-far);
		perspective.m33 = 0f;
		return perspective;
	}
	/**
	 * Creates a rotation matrix. Similar to <code>glRotate(angle, x, y, z)</code>.
	 *
	 * @param angle
	 *            Angle of rotation in degrees
	 * @param x
	 *            x coordinate of the rotation vector
	 * @param y
	 *            y coordinate of the rotation vector
	 * @param z
	 *            z coordinate of the rotation vector
	 * @return Rotation matrix
	 */
	public static Matrix4f rotate(float angle, float x, float y, float z){
		Matrix4f rotation = new Matrix4f();
		float c = (float)Math.cos(Math.toRadians(angle));
		float s = (float)Math.sin(Math.toRadians(angle));
		Vector3f vec = new Vector3f(x, y, z);
		if(vec.length()!=1f){
			vec = vec.normalize();
			x = vec.x;
			y = vec.y;
			z = vec.z;
		}
		rotation.m00 = x*x*(1f-c)+c;
		rotation.m10 = y*x*(1f-c)+z*s;
		rotation.m20 = x*z*(1f-c)-y*s;
		rotation.m01 = x*y*(1f-c)-z*s;
		rotation.m11 = y*y*(1f-c)+c;
		rotation.m21 = y*z*(1f-c)+x*s;
		rotation.m02 = x*z*(1f-c)+y*s;
		rotation.m12 = y*z*(1f-c)-x*s;
		rotation.m22 = z*z*(1f-c)+c;
		return rotation;
	}
	/**
	 * Creates a scaling matrix. Similar to <code>glScale(x, y, z)</code>.
	 *
	 * @param x
	 *            Scale factor along the x coordinate
	 * @param y
	 *            Scale factor along the y coordinate
	 * @param z
	 *            Scale factor along the z coordinate
	 * @return Scaling matrix
	 */
	public static Matrix4f scale(float x, float y, float z){
		Matrix4f scaling = new Matrix4f();
		scaling.m00 = x;
		scaling.m11 = y;
		scaling.m22 = z;
		return scaling;
	}
	/**
	 * Creates a translation matrix. Similar to <code>glTranslate(x, y, z)</code>.
	 *
	 * @param x
	 *            x coordinate of translation vector
	 * @param y
	 *            y coordinate of translation vector
	 * @param z
	 *            z coordinate of translation vector
	 * @return Translation matrix
	 */
	public static Matrix4f translate(float x, float y, float z){
		Matrix4f translation = new Matrix4f();
		translation.m03 = x;
		translation.m13 = y;
		translation.m23 = z;
		return translation;
	}
	private float m00, m01, m02, m03;
	private float m10, m11, m12, m13;
	private float m20, m21, m22, m23;
	private float m30, m31, m32, m33;
	/**
	 * Creates a 4x4 identity matrix.
	 */
	public Matrix4f(){
		setIdentity();
	}
	/**
	 * Creates a 4x4 matrix with specified columns.
	 *
	 * @param col1
	 *            Vector with values of the first column
	 * @param col2
	 *            Vector with values of the second column
	 * @param col3
	 *            Vector with values of the third column
	 * @param col4
	 *            Vector with values of the fourth column
	 */
	public Matrix4f(Vector4f col1, Vector4f col2, Vector4f col3, Vector4f col4){
		m00 = col1.x;
		m10 = col1.y;
		m20 = col1.z;
		m30 = col1.w;
		m01 = col2.x;
		m11 = col2.y;
		m21 = col2.z;
		m31 = col2.w;
		m02 = col3.x;
		m12 = col3.y;
		m22 = col3.z;
		m32 = col3.w;
		m03 = col4.x;
		m13 = col4.y;
		m23 = col4.z;
		m33 = col4.w;
	}
	/**
	 * Adds this matrix to another matrix.
	 *
	 * @param other
	 *            The other matrix
	 * @return Sum of this + other
	 */
	public Matrix4f add(Matrix4f other){
		Matrix4f result = new Matrix4f();
		result.m00 = m00+other.m00;
		result.m10 = m10+other.m10;
		result.m20 = m20+other.m20;
		result.m30 = m30+other.m30;
		result.m01 = m01+other.m01;
		result.m11 = m11+other.m11;
		result.m21 = m21+other.m21;
		result.m31 = m31+other.m31;
		result.m02 = m02+other.m02;
		result.m12 = m12+other.m12;
		result.m22 = m22+other.m22;
		result.m32 = m32+other.m32;
		result.m03 = m03+other.m03;
		result.m13 = m13+other.m13;
		result.m23 = m23+other.m23;
		result.m33 = m33+other.m33;
		return result;
	}
	/**
	 * Returns the Buffer representation of this vector.
	 *
	 * @return Vector as FloatBuffer
	 */
	public FloatBuffer getBuffer(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(m00).put(m10).put(m20).put(m30);
		buffer.put(m01).put(m11).put(m21).put(m31);
		buffer.put(m02).put(m12).put(m22).put(m32);
		buffer.put(m03).put(m13).put(m23).put(m33);
		buffer.flip();
		return buffer;
	}
	/**
	 * Multiplies this matrix with a scalar.
	 *
	 * @param scalar
	 *            The scalar
	 * @return Scalar product of this * scalar
	 */
	public Matrix4f multiply(float scalar){
		Matrix4f result = new Matrix4f();
		result.m00 = m00*scalar;
		result.m10 = m10*scalar;
		result.m20 = m20*scalar;
		result.m30 = m30*scalar;
		result.m01 = m01*scalar;
		result.m11 = m11*scalar;
		result.m21 = m21*scalar;
		result.m31 = m31*scalar;
		result.m02 = m02*scalar;
		result.m12 = m12*scalar;
		result.m22 = m22*scalar;
		result.m32 = m32*scalar;
		result.m03 = m03*scalar;
		result.m13 = m13*scalar;
		result.m23 = m23*scalar;
		result.m33 = m33*scalar;
		return result;
	}
	/**
	 * Multiplies this matrix to another matrix.
	 *
	 * @param other
	 *            The other matrix
	 * @return Matrix product of this * other
	 */
	public Matrix4f multiply(Matrix4f other){
		Matrix4f result = new Matrix4f();
		result.m00 = m00*other.m00+m01*other.m10+m02*other.m20+m03*other.m30;
		result.m10 = m10*other.m00+m11*other.m10+m12*other.m20+m13*other.m30;
		result.m20 = m20*other.m00+m21*other.m10+m22*other.m20+m23*other.m30;
		result.m30 = m30*other.m00+m31*other.m10+m32*other.m20+m33*other.m30;
		result.m01 = m00*other.m01+m01*other.m11+m02*other.m21+m03*other.m31;
		result.m11 = m10*other.m01+m11*other.m11+m12*other.m21+m13*other.m31;
		result.m21 = m20*other.m01+m21*other.m11+m22*other.m21+m23*other.m31;
		result.m31 = m30*other.m01+m31*other.m11+m32*other.m21+m33*other.m31;
		result.m02 = m00*other.m02+m01*other.m12+m02*other.m22+m03*other.m32;
		result.m12 = m10*other.m02+m11*other.m12+m12*other.m22+m13*other.m32;
		result.m22 = m20*other.m02+m21*other.m12+m22*other.m22+m23*other.m32;
		result.m32 = m30*other.m02+m31*other.m12+m32*other.m22+m33*other.m32;
		result.m03 = m00*other.m03+m01*other.m13+m02*other.m23+m03*other.m33;
		result.m13 = m10*other.m03+m11*other.m13+m12*other.m23+m13*other.m33;
		result.m23 = m20*other.m03+m21*other.m13+m22*other.m23+m23*other.m33;
		result.m33 = m30*other.m03+m31*other.m13+m32*other.m23+m33*other.m33;
		return result;
	}
	/**
	 * Multiplies this matrix to a vector.
	 *
	 * @param vector
	 *            The vector
	 * @return Vector product of this * other
	 */
	public Vector4f multiply(Vector4f vector){
		float x = m00*vector.x+m01*vector.y+m02*vector.z+m03*vector.w;
		float y = m10*vector.x+m11*vector.y+m12*vector.z+m13*vector.w;
		float z = m20*vector.x+m21*vector.y+m22*vector.z+m23*vector.w;
		float w = m30*vector.x+m31*vector.y+m32*vector.z+m33*vector.w;
		return new Vector4f(x, y, z, w);
	}
	/**
	 * Negates this matrix.
	 *
	 * @return Negated matrix
	 */
	public Matrix4f negate(){
		return multiply(-1f);
	}
	/**
	 * Sets this matrix to the identity matrix.
	 */
	public final void setIdentity(){
		m00 = 1f;
		m11 = 1f;
		m22 = 1f;
		m33 = 1f;
		m01 = 0f;
		m02 = 0f;
		m03 = 0f;
		m10 = 0f;
		m12 = 0f;
		m13 = 0f;
		m20 = 0f;
		m21 = 0f;
		m23 = 0f;
		m30 = 0f;
		m31 = 0f;
		m32 = 0f;
	}
	/**
	 * Subtracts this matrix from another matrix.
	 *
	 * @param other
	 *            The other matrix
	 * @return Difference of this - other
	 */
	public Matrix4f subtract(Matrix4f other){
		return add(other.negate());
	}
	/**
	 * Transposes this matrix.
	 *
	 * @return Transposed matrix
	 */
	public Matrix4f transpose(){
		Matrix4f result = new Matrix4f();
		result.m00 = m00;
		result.m10 = m01;
		result.m20 = m02;
		result.m30 = m03;
		result.m01 = m10;
		result.m11 = m11;
		result.m21 = m12;
		result.m31 = m13;
		result.m02 = m20;
		result.m12 = m21;
		result.m22 = m22;
		result.m32 = m23;
		result.m03 = m30;
		result.m13 = m31;
		result.m23 = m32;
		result.m33 = m33;
		return result;
	}
}
