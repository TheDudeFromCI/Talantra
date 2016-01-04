package talantra.game;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class TestModel{
	private final Vector3f temp = new Vector3f();
	private final long time;
	private final Matrix4f mat;
	private final FloatBuffer buf;
	private VertexData[] vertices;
	private ByteBuffer verticesByteBuffer;
	private int indicesCount;
	private int vaoId;
	private int vboId;
	private int vboiId;
	private float pos;
	public TestModel(){
		time = System.currentTimeMillis();
		mat = new Matrix4f();
		buf = BufferUtils.createFloatBuffer(16);
		build();
	}
	public void dispose(){
		GL30.glBindVertexArray(vaoId);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboId);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboiId);
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
	}
	public float getY(){
		return pos;
	}
	public void render(int modelLocation){
		// Prepare
		mat.store(buf);
		buf.flip();
		GL20.glUniformMatrix4(modelLocation, false, buf);
		// Render
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	public void update(){
		pos = (System.currentTimeMillis()-time)/1000f;
		temp.set(0, -pos, 0);
		mat.setIdentity();
		mat.translate(temp);
	}
	private void build(){
		VertexData v0 = new VertexData();
		v0.setXYZ(-0.5f, 0.5f, 0);
		v0.setRGB(1, 0, 0);
		v0.setST(0, 0);
		VertexData v1 = new VertexData();
		v1.setXYZ(-0.5f, -0.5f, 0);
		v1.setRGB(0, 1, 0);
		v1.setST(0, 1);
		VertexData v2 = new VertexData();
		v2.setXYZ(0.5f, -0.5f, 0);
		v2.setRGB(0, 0, 1);
		v2.setST(1, 1);
		VertexData v3 = new VertexData();
		v3.setXYZ(0.5f, 0.5f, 0);
		v3.setRGB(1, 1, 1);
		v3.setST(1, 0);
		vertices = new VertexData[]{
			v0, v1, v2, v3
		};
		// Put each 'Vertex' in one FloatBuffer
		verticesByteBuffer = BufferUtils.createByteBuffer(vertices.length*VertexData.stride);
		FloatBuffer verticesFloatBuffer = verticesByteBuffer.asFloatBuffer();
		for(int i = 0; i<vertices.length; i++){
			// Add position, color and texture floats to the buffer
			verticesFloatBuffer.put(vertices[i].getElements());
		}
		verticesFloatBuffer.flip();
		// OpenGL expects to draw vertices in counter clockwise order by default
		byte[] indices = {
			0, 1, 2, 2, 3, 0
		};
		indicesCount = indices.length;
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		// Create a new Vertex Array Object in memory and select it (bind)
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		// Create a new Vertex Buffer Object in memory and select it (bind)
		vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer, GL15.GL_STREAM_DRAW);
		// Put the position coordinates in attribute list 0
		GL20.glVertexAttribPointer(0, VertexData.positionElementCount, GL11.GL_FLOAT, false, VertexData.stride, VertexData.positionByteOffset);
		// Put the color components in attribute list 1
		GL20.glVertexAttribPointer(1, VertexData.colorElementCount, GL11.GL_FLOAT, false, VertexData.stride, VertexData.colorByteOffset);
		// Put the texture coordinates in attribute list 2
		GL20.glVertexAttribPointer(2, VertexData.textureElementCount, GL11.GL_FLOAT, false, VertexData.stride, VertexData.textureByteOffset);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
		// Create a new VBO for the indices and select it (bind) - INDICES
		vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
}
