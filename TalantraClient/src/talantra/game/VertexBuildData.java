package talantra.game;

public class VertexBuildData{
	private final float[] vertexLocations;
	private final short[] indexLocations;
	public VertexBuildData(float[] vertexLocations, short[] indexLocations){
		this.vertexLocations = vertexLocations;
		this.indexLocations = indexLocations;
	}
	public short[] getIndexLocations(){
		return indexLocations;
	}
	public float[] getVertexLocations(){
		return vertexLocations;
	}
}
