package org.wraith.engine.rendering;

public class PrimitiveGenerator{
	public static VertexBuildData generateBox(float radiusX, float radiusY, float radiusZ){
		return new VertexBuildData(new float[]{
			-radiusX, -radiusY, -radiusZ, -radiusX, -radiusY, radiusZ, -radiusX, radiusY, -radiusZ, -radiusX, radiusY, radiusZ, radiusX, -radiusY,
			-radiusZ, radiusX, -radiusY, radiusZ, radiusX, radiusY, -radiusZ, radiusX, radiusY, radiusZ,
		}, new short[]{
			0, 5, 1, 0, 4, 5, 0, 6, 2, 0, 4, 6, 0, 1, 2, 1, 3, 2, 1, 3, 7, 1, 7, 5, 4, 5, 6, 5, 7, 6, 2, 7, 3, 2, 6, 7
		});
	}
}
