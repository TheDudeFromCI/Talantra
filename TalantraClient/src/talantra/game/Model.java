package talantra.game;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import talantra.game.PrimitiveGenerator.PrimitiveFlags;
import talantra.game.VAO.VaoArray;

public class Model{
	private final Matrix4f positionMatrix;
	private final VAO vao;
	public Model(){
		positionMatrix = new Matrix4f();
		VertexBuildData data = PrimitiveGenerator.generateBox(0.5f, 0.5f, 0.5f, new PrimitiveFlags(true, true));
		float[] vertLocations = data.getVertexLocations();
		float[] vertices = new float[vertLocations.length/5*10];
		for(int i = 0; i<vertices.length; i += 10){
			vertices[i+0] = vertLocations[i/10*5+0];
			vertices[i+1] = vertLocations[i/10*5+1];
			vertices[i+2] = vertLocations[i/10*5+2];
			vertices[i+3] = 1.0f;
			vertices[i+4] = (float)Math.random();
			vertices[i+5] = (float)Math.random();
			vertices[i+6] = (float)Math.random();
			vertices[i+7] = 1.0f;
			vertices[i+8] = vertLocations[i/10*5+3];
			vertices[i+9] = vertLocations[i/10*5+4];
		}
		VaoArray[] parts = new VaoArray[]{
			new VaoArray(4, false), new VaoArray(4, false), new VaoArray(2, false)
		};
		vao = new VAO(vertices, data.getIndexLocations(), parts);
	}
	public void dispose(){
		vao.dispose();
	}
	public Matrix4f getMatrix(){
		return positionMatrix;
	}
	public void render(){
		vao.render();
	}
	@SuppressWarnings("unused")
	public void update(double delta, double time){
		positionMatrix.rotate((float)Math.toRadians(1), new Vector3f(0, 1, 0));
	}
}
